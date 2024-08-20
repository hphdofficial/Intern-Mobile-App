package fascon.vovinam.vn.View;

import fascon.vovinam.vn.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences;
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

import fascon.vovinam.vn.Model.Club;
import fascon.vovinam.vn.Model.MapsClubItem;
import fascon.vovinam.vn.Model.MapsElement;
import fascon.vovinam.vn.Model.MapsResponse;
import fascon.vovinam.vn.Model.network.ApiServiceProvider;
import fascon.vovinam.vn.Model.services.ClubApiService;

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
    private static int countryId = 0;
    private static int cityId = 0;
    private BlankFragment loadingFragment;
    private String languageS;

    public static ClubMapsFragment newInstance(double latitude, double longitude, boolean current, int country, int city) {
        ClubMapsFragment fragment = new ClubMapsFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        isCurrent = current;
        countryId = country;
        cityId = city;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_prefs", Context.MODE_PRIVATE);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
        } else if (sharedPreferences.getString("location_lat", null) != null && sharedPreferences.getString("location_long", null) != null) {
            latitude = Double.parseDouble(sharedPreferences.getString("location_lat", null));
            longitude = Double.parseDouble(sharedPreferences.getString("location_long", null));
            isCurrent = true;
        } else {
            // Location Viet Nam
            latitude = 10.779686;
            longitude = 106.694241;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        map = rootView.findViewById(R.id.map);
        SharedPreferences shared = getActivity().getSharedPreferences("login_prefs", getContext().MODE_PRIVATE);
        languageS = shared.getString("language", null);
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
            if (languageS != null) {
                if (languageS.contains("en")) {
                    startMarker.setTitle("Your current location");
                }
            }
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
//        Call<JsonObject> call = service.getListClubMap3(latitude + ", " + longitude, languageS.equals("vn") ? "vi" : "en");
        Call<JsonObject> call = service.getListClubMap1(230, languageS.equals("vn") ? "vi" : "en");

        if (isCurrent) {
            call = service.getListClubMap3(latitude + ", " + longitude, languageS.equals("vn") ? "vi" : "en");
        } else if (countryId != 0 && cityId == 0) {
            call = service.getListClubMap1(countryId, languageS.equals("vn") ? "vi" : "en");
        } else if (countryId != 0 && cityId != 0) {
            call = service.getListClubMap2(countryId, cityId, languageS.equals("vn") ? "vi" : "en");
        }


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
                        if (languageS != null) {
                            if (languageS.contains("en")) {
                                Toast.makeText(requireContext(), "No clubs found", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Không tìm thấy câu lạc bộ nào", Toast.LENGTH_SHORT).show();
                            }
                        }
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
            if (languageS != null) {
                if (languageS.contains("en")) {
                    marker.setTitle("Club name: " + item.getTitle() + "\n" + "Address: " + item.getSnippet());
                }
            }

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