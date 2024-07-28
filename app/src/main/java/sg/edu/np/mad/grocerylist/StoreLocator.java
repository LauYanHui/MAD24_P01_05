package sg.edu.np.mad.grocerylist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
//import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreLocator extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                            // Optionally add a marker for the user's location
                            mMap.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
                        }
                    });
        } else {
            requestLocationPermission();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                            // Optionally add a marker for the user's location
                            mMap.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
                        }
                    });

            // Load store data and display markers
            List<Store> stores = loadStoreData();
            for (Store store : stores) {
                LatLng storeLocation = new LatLng(store.getLat(), store.getLng());
                mMap.addMarker(new MarkerOptions().position(storeLocation).title(store.getName()).snippet(store.getAddress()));
            }

            requestLocationUpdates();
        } else {
            requestLocationPermission();
        }
    }

    private void requestLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000); // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (checkLocationPermission()) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } else {
            requestLocationPermission();
        }
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }
            for (Location location : locationResult.getLocations()) {
                // Update map with new location
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            }
        }
    };

    private List<Store> loadStoreData() {
        List<Store> storeList = new ArrayList<>();
        try {
            InputStream is = getResources().openRawResource(R.raw.stores);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String json = builder.toString();
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject storeObject = jsonArray.getJSONObject(i);
                String name = storeObject.getString("name");
                double lat = storeObject.getDouble("lat");
                double lng = storeObject.getDouble("lng");
                String address = storeObject.getString("address");
                storeList.add(new Store(name, lat, lng, address));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return storeList;
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    public class Store {
        private String name;
        private double lat;
        private double lng;
        private String address;

        public Store(String name, double lat, double lng, String address) {
            this.name = name;
            this.lat = lat;
            this.lng = lng;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public String getAddress() {
            return address;
        }
    }
}
