package com.example.assignment1;

import androidx.fragment.app.FragmentActivity;

import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.assignment1.databinding.ActivityGoogleMapBinding;

import java.util.Locale;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapBinding binding;

    private String eventLocation;
    private String categoryName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eventLocation = getIntent().getExtras().getString("eventLocation", "");
        categoryName = getIntent().getExtras().getString("categoryName", "");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**
         * Change map display type, feel free to explore other available map type:
         * MAP_TYPE_NORMAL: Basic map.
         * MAP_TYPE_SATELLITE: Satellite imagery.
         * MAP_TYPE_HYBRID: Satellite imagery with roads and labels.
         * MAP_TYPE_TERRAIN: Topographic data.
         * MAP_TYPE_NONE: No base map tiles
         */
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // initialise Geocode to search location using String
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        if (eventLocation == null || eventLocation.trim().isEmpty()) {
            Toast.makeText(GoogleMapActivity.this, "Category address not found", Toast.LENGTH_LONG).show();

            // getFromLocationName method works for API 33 and above
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            geocoder.getFromLocationName(eventLocation, 1, addresses -> {
                if (!addresses.isEmpty()) {
                    runOnUiThread(() -> {
                        LatLng newAddressLocation = new LatLng(
                                addresses.get(0).getLatitude(),
                                addresses.get(0).getLongitude()
                        );

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(newAddressLocation));
                        mMap.addMarker(new MarkerOptions()
                                .position(newAddressLocation)
                                .title(categoryName)
                        );
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10f));
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(GoogleMapActivity.this, "Category address not found", Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }
}