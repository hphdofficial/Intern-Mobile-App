package com.android.mobile;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.android.mobile.models.Club;
import com.android.mobile.models.MapsClubItem;
import com.android.mobile.models.MapsElement;
import com.android.mobile.models.MapsResponse;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClubApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.mapsforge.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubMapsFragment extends Fragment {
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private MapView map;
    private Handler handler = new Handler();
    private GeoPoint lastCenterPoint;
    private Marker currentMarker;
    private double latitude;
    private double longitude;
    private static boolean isCurrent = false;
    private BlankFragment loadingFragment;

    public static ClubMapsFragment newInstance(double latitude, double longitude, boolean current) {
        ClubMapsFragment fragment = new ClubMapsFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        isCurrent = current;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
        } else {
            latitude = 10.76833026;
            longitude = 106.67583063;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        map = rootView.findViewById(R.id.map);

        ViewCompat.setOnApplyWindowInsetsListener(rootView.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        File basePath = new File(requireContext().getCacheDir().getAbsolutePath(), "osmdroid");
        if (!basePath.exists()) {
            basePath.mkdirs();
        }
        File tileCache = new File(basePath, "tiles");
        if (!tileCache.exists()) {
            tileCache.mkdirs();
        }
        Configuration.getInstance().setOsmdroidBasePath(basePath);
        Configuration.getInstance().setOsmdroidTileCache(tileCache);

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        map.getController().setZoom(15.0);
        map.getController().setCenter(startPoint);
        lastCenterPoint = startPoint;

        CompassOverlay compassOverlay = new CompassOverlay(requireContext(), new InternalCompassOrientationProvider(requireContext()), map);
        compassOverlay.enableCompass();
        map.getOverlays().add(compassOverlay);

        if (isCurrent) {
            Marker startMarker = new Marker(map);
            startMarker.setPosition(startPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            startMarker.setIcon(resizeDrawable(getResources().getDrawable(R.drawable.icons8_marker_48), 60, 80));
            startMarker.setTitle("Vị trí hiện tại của bạn");

            CustomInfoWindow infoWindow = new CustomInfoWindow(map, "current");
            startMarker.setInfoWindow(infoWindow);

            startMarker.setOnMarkerClickListener((marker, mapView) -> {
                if (currentMarker != null) {
                    currentMarker.closeInfoWindow();
                }
                marker.showInfoWindow();
                currentMarker = marker;
                mapView.getController().animateTo(marker.getPosition());
                return true;
            });
            map.getOverlays().add(startMarker);
        }

        fetchMapData(startPoint);

        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if (currentMarker != null) {
                    currentMarker.closeInfoWindow();
                    currentMarker = null;
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(mapEventsReceiver);
        map.getOverlays().add(mapEventsOverlay);

        map.addMapListener(new MapListener() {
            @Override
            public boolean onScroll(ScrollEvent event) {
                return false;
            }

            @Override
            public boolean onZoom(ZoomEvent event) {
                if (currentMarker != null) {
                    currentMarker.closeInfoWindow();
                    currentMarker = null;
                }
                return true;
            }
        });

        fetchClubData();

        return rootView;
    }

    private void fetchMapData(GeoPoint center) {
        ClubApiService apiService = ApiServiceProvider.getMapsInstance().create(ClubApiService.class);
        double latSpan = 0.1;
        double lonSpan = 0.1;
        String overpassQuery = "[out:json];node(" + (center.getLatitude() - latSpan) + "," + (center.getLongitude() - lonSpan) + "," + (center.getLatitude() + latSpan) + "," + (center.getLongitude() + lonSpan) + ");out body;";
        Call<MapsResponse> call = apiService.getMapData(overpassQuery);
        call.enqueue(new Callback<MapsResponse>() {
            @Override
            public void onResponse(Call<MapsResponse> call, Response<MapsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MapsElement> elements = response.body().getElements();
                    map.getOverlays().clear();
                    for (MapsElement element : elements) {
                        if ("node".equals(element.getType())) {
                            GeoPoint point = new GeoPoint(element.getLat(), element.getLon());
                            Marker marker = new Marker(map);
                            marker.setPosition(point);
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                            marker.setTitle("ID: " + element.getId());

                            CustomInfoWindow infoWindow = new CustomInfoWindow(map);
                            marker.setInfoWindow(infoWindow);

                            marker.setOnMarkerClickListener((m, mapView) -> {
                                if (currentMarker != null) {
                                    currentMarker.closeInfoWindow();
                                }
                                m.showInfoWindow();
                                currentMarker = m;
                                mapView.getController().animateTo(m.getPosition());
                                return true;
                            });

                            map.getOverlays().add(marker);
                        }
                    }
                    CompassOverlay compassOverlay = new CompassOverlay(requireContext(), new InternalCompassOrientationProvider(requireContext()), map);
                    compassOverlay.enableCompass();
                    map.getOverlays().add(compassOverlay);
                    map.invalidate();
                }
            }

            @Override
            public void onFailure(Call<MapsResponse> call, Throwable t) {
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void fetchClubData() {
        showLoading();

        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<JsonObject> call = service.getListClubMap3(latitude + ", " + longitude);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    Type clubListType = new TypeToken<List<Club>>() {
                    }.getType();
                    List<Club> clubs = gson.fromJson(jsonObject.get("clubs"), clubListType);
                    List<MapsClubItem> clubItems = new ArrayList<>();
                    for (Club club : clubs) {
                        GeoPoint geoPoint = new GeoPoint(Double.parseDouble(club.getMap_lat()), Double.parseDouble(club.getMap_long()));
                        clubItems.add(new MapsClubItem(club.getId(), club.getTen(), club.getDiachi(), geoPoint));
                    }
                    Drawable marker = resizeDrawable(getResources().getDrawable(R.drawable.ic_marker_vovinam), 60, 80);
                    displayClubsOnMap(clubItems, marker);
//                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show();
                    if (clubs.isEmpty()) {
                        Toast.makeText(requireContext(), "Không tìm thấy câu lạc bộ nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Response error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideLoading();

                Log.e("Error", t.getMessage());
            }
        });
    }

    private void displayClubsOnMap(List<MapsClubItem> pList, Drawable pDefaultMarker) {
        ItemizedIconOverlay.OnItemGestureListener<MapsClubItem> gestureListener = new ItemizedIconOverlay.OnItemGestureListener<MapsClubItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, MapsClubItem item) {
                if (currentMarker != null) {
                    currentMarker.closeInfoWindow();
                }
                currentMarker = null;
                return true;
            }

            @Override
            public boolean onItemLongPress(int index, MapsClubItem item) {
                return true;
            }
        };
        ItemizedIconOverlay<MapsClubItem> overlay = new ItemizedIconOverlay<>(pList, pDefaultMarker, gestureListener, requireContext());
        map.getOverlays().add(overlay);

        for (MapsClubItem item : pList) {
            Marker marker = new Marker(map);
            marker.setPosition((GeoPoint) item.getPoint());
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setIcon(pDefaultMarker);
            marker.setTitle("Tên câu lạc bộ: " + item.getTitle() + "\n" + "Địa chỉ: " + item.getSnippet());

            CustomInfoWindow infoWindow = new CustomInfoWindow(map, item.getUid());
            marker.setInfoWindow(infoWindow);

            marker.setOnMarkerClickListener((m, mapView) -> {
                if (currentMarker != null) {
                    currentMarker.closeInfoWindow();
                }
                m.showInfoWindow();
                currentMarker = m;
                mapView.getController().animateTo(m.getPosition());
                return true;
            });
            map.getOverlays().add(marker);
        }
        map.invalidate();

        hideLoading();
    }

    private Drawable resizeDrawable(Drawable image, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        image.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        image.draw(canvas);
        return new BitmapDrawable(getResources(), bitmap);
    }


    private void showLoading() {
        loadingFragment = new BlankFragment();
        loadingFragment.show(getParentFragmentManager(), "loading");
    }

    private void hideLoading() {
        if (loadingFragment != null) {
            loadingFragment.dismiss();
            loadingFragment = null;
        }
    }
}