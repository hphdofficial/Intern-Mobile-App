package com.android.mobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.mobile.adapter.BaseActivity;
import com.android.mobile.models.CityModel;
import com.android.mobile.models.Club;
import com.android.mobile.models.CountryModel;
import com.android.mobile.network.ApiServiceProvider;
import com.android.mobile.services.ClubApiService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubActivity extends BaseActivity {
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SearchView searchView;
    private ClubListFragment clubListFragment;
    private MapsFragment mapsFragment;
    private Spinner viewOptionsSpinner;
    private Spinner spinnerCountry;
    private Spinner spinnerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_club);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences myContent = getSharedPreferences("myContent", Context.MODE_PRIVATE);
        SharedPreferences.Editor myContentE = myContent.edit();
        myContentE.putString("title", "Danh sách câu lạc bộ");
        myContentE.apply();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new titleFragment());
        fragmentTransaction.commit();

        searchView = findViewById(R.id.search_location);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchClub(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Button btnLocation = findViewById(R.id.btn_current_location);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ClubActivity.this);

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ClubActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    checkLocationSettings();
                }

                spinnerCountry.setSelection(0);
                spinnerCity.setSelection(0);
            }
        });

        clubListFragment = new ClubListFragment();
        mapsFragment = new MapsFragment();

        FragmentTransaction fragmentTransactionClubList = getSupportFragmentManager().beginTransaction();
        fragmentTransactionClubList.replace(R.id.fragment_container_club, mapsFragment);
        fragmentTransactionClubList.commit();

        viewOptionsSpinner = findViewById(R.id.spinner_view_options);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.view_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewOptionsSpinner.setAdapter(adapter);
        viewOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (position) {
                    case 0:
                        transaction.replace(R.id.fragment_container_club, mapsFragment);
                        break;
                    case 1:
                        transaction.replace(R.id.fragment_container_club, clubListFragment);
                        break;
                }
                transaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerCountry = findViewById(R.id.spinner_country);
        spinnerCity = findViewById(R.id.spinner_city);

        loadListClubDefault();
        showListCountry();
        showListCity();
    }

    public void searchClub(String text) {
        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<List<Club>> call = service.searchClub(text);

        call.enqueue(new Callback<List<Club>>() {
            @Override
            public void onResponse(Call<List<Club>> call, Response<List<Club>> response) {
                if (response.isSuccessful()) {
                    List<Club> clubs = response.body();
                    clubListFragment.setData(clubs);
                    Toast.makeText(ClubActivity.this, "Tìm kiếm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Club>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setNumUpdates(1).setInterval(10000).setFastestInterval(5000);

                fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null) {
                            Toast.makeText(ClubActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            loadClubListByLocation(latitude, longitude);
                            Log.e("current_location", latitude + ", " + longitude);
                            Toast.makeText(ClubActivity.this, "Truy cập vị trí hiện tại thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ClubActivity.this, "Không lấy được vị trí hiện tại", Toast.LENGTH_SHORT).show();
                        }
                        fusedLocationProviderClient.removeLocationUpdates(this);
                    }
                }, Looper.getMainLooper());
            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(this, "SecurityException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            ActivityCompat.requestPermissions(ClubActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    private void checkLocationSettings() {
        LocationRequest locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    getCurrentLocation();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                resolvable.startResolutionForResult(ClubActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                            } catch (ClassCastException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Bạn đã hủy truy cập vị trí hiện tại", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationSettings();
            } else {
                Toast.makeText(this, "Bạn đã từ chối truy cập vị trí hiện tại", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadListClubDefault() {
        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<JsonObject> call = service.getListClubMap2(230, 50);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    Type clubListType = new TypeToken<List<Club>>() {
                    }.getType();
                    List<Club> clubs = gson.fromJson(jsonObject.get("clubs"), clubListType);
                    clubListFragment.setData(clubs);
                    Toast.makeText(ClubActivity.this, "Tải dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    if (clubs.isEmpty()) {
                        Toast.makeText(ClubActivity.this, "Không tìm thấy câu lạc bộ nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void loadClubListByLocation(double latitude, double longitude) {
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
                    clubListFragment.setData(clubs);
                    Toast.makeText(ClubActivity.this, "Success " + response.message(), Toast.LENGTH_SHORT).show();
                    if (clubs.isEmpty()) {
                        Toast.makeText(ClubActivity.this, "Không tìm thấy câu lạc bộ nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void loadClubListByCountry(int countryId) {
        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<JsonObject> call = service.getListClubMap1(countryId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    Type clubListType = new TypeToken<List<Club>>() {
                    }.getType();
                    List<Club> clubs = gson.fromJson(jsonObject.get("clubs"), clubListType);
                    clubListFragment.setData(clubs);
                    Toast.makeText(ClubActivity.this, "Success " + response.message(), Toast.LENGTH_SHORT).show();
                    if (clubs.isEmpty()) {
                        Toast.makeText(ClubActivity.this, "Không tìm thấy câu lạc bộ nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void loadClubListByCity(int cityId) {
        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<JsonObject> call = service.getListClubMap2(230, cityId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    Gson gson = new Gson();
                    Type clubListType = new TypeToken<List<Club>>() {
                    }.getType();
                    List<Club> clubs = gson.fromJson(jsonObject.get("clubs"), clubListType);
                    clubListFragment.setData(clubs);
                    Toast.makeText(ClubActivity.this, "Success " + response.message(), Toast.LENGTH_SHORT).show();
                    if (clubs.isEmpty()) {
                        Toast.makeText(ClubActivity.this, "Không tìm thấy câu lạc bộ nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void showListCountry() {
        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<List<CountryModel>> call = service.getListCountry();

        call.enqueue(new Callback<List<CountryModel>>() {
            @Override
            public void onResponse(Call<List<CountryModel>> call, Response<List<CountryModel>> response) {
                if (response.isSuccessful()) {
                    List<CountryModel> countryList = response.body();
                    List<CountryModel> countries = new ArrayList<>();
                    countries.add(new CountryModel(0, "Quốc gia"));
                    for (CountryModel country : countryList) {
                        countries.add(new CountryModel(country.getId(), country.getTen()));
                    }
                    ArrayAdapter<CountryModel> adapter = new ArrayAdapter<>(ClubActivity.this, android.R.layout.simple_spinner_item, countries);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCountry.setAdapter(adapter);
                    spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            CountryModel selectedCountry = (CountryModel) parent.getItemAtPosition(position);
                            int countryId = selectedCountry.getId();
                            if (position > 0) {
                                Toast.makeText(ClubActivity.this, "Selected: " + selectedCountry.getTen(), Toast.LENGTH_SHORT).show();
                                loadClubListByCountry(countryId);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<CountryModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void showListCity() {
        ClubApiService service = ApiServiceProvider.getClubApiService();
        Call<List<CityModel>> call = service.getListCity(230);

        call.enqueue(new Callback<List<CityModel>>() {
            @Override
            public void onResponse(Call<List<CityModel>> call, Response<List<CityModel>> response) {
                if (response.isSuccessful()) {
                    List<CityModel> cityList = response.body();
                    List<CityModel> cities = new ArrayList<>();
                    cities.add(new CityModel(0, "Thành phố"));
                    for (CityModel city : cityList) {
                        cities.add(new CityModel(city.getId(), city.getTen()));
                    }
                    ArrayAdapter<CityModel> adapter = new ArrayAdapter<>(ClubActivity.this, android.R.layout.simple_spinner_item, cities);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCity.setAdapter(adapter);
                    spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            CityModel selectedCity = (CityModel) parent.getItemAtPosition(position);
                            int cityId = selectedCity.getId();
                            if (position > 0) {
                                Toast.makeText(ClubActivity.this, "Selected: " + selectedCity.getTen(), Toast.LENGTH_SHORT).show();
                                loadClubListByCity(cityId);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                } else {
                    System.err.println("Response error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<CityModel>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}