package com.example.firefly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.StringReader;

public class MapView extends AppCompatActivity implements View.OnClickListener {
    SupportMapFragment smf;
    FusedLocationProviderClient client;
    private Button confirmLocationButton;
    private String Latitude,Longitude,customLat=null,customLong=null;

    //SENDING REALTIME DATA TO FIREBASE
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    GoogleMap gMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        confirmLocationButton=findViewById(R.id.confirmLocation);
        confirmLocationButton.setOnClickListener(this);
        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getMyLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Intent intent=new Intent(getApplicationContext(),Home.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        try {
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    smf.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            GoogleMap userLocation=googleMap;

                            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                            Latitude=String.valueOf(location.getLatitude());
                            Longitude=String.valueOf(location.getLongitude());
                            MarkerOptions userlocation=new MarkerOptions()
                                    .position(latLng)
                                    .title("Current Location")
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

                            userLocation.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            userLocation.addMarker(userlocation);

                            //For CustomLocation
                            gMap=googleMap;
                            //userLocation.clear();
                            gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLngFromCustomMarker) {

                                    MarkerOptions customLocationMarker=new MarkerOptions();
                                    customLocationMarker.position(latLngFromCustomMarker);
                                    customLong=String.valueOf(latLngFromCustomMarker.longitude);
                                    customLat= String.valueOf(latLngFromCustomMarker.latitude);
                                    customLocationMarker.title("Location picked here");
                                    gMap.clear();
                                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngFromCustomMarker,15));
                                    gMap.addMarker(customLocationMarker);
                                }
                            });

                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                        }
                    });
                }
            });
        }catch (Exception e){
            Log.e("Exception in map view", String.valueOf(e));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmLocation:

                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("Emergencies");
                reference.setValue(customLat);
                Log.i("User Current Location","Location of the user is "+Latitude+" "+Longitude);

                if(customLong!=null && customLong!=null){
                    Log.i("Custom Location","Location from custom marker is "+customLat+" "+customLong);
                }else{
                    Log.i("Custom Location","Unreachable");
                }

                //getMyLocation(); //use this line of code in gps button if possible
                Toast.makeText(getApplicationContext(), "Location Confirmed", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "Fire Services Alerted", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                break;

        }
    }
}