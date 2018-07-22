package edu.admu.cs298s28.attendancechecker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

@EActivity(R.layout.activity_maps)
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Realm realm;
    private static final int DEFAULT_ZOOM = 15;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters


    @AfterViews
    protected void init() {
        realm = MyRealm.getRealm();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...

                    LatLng newLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    //mMap.addMarker(new MarkerOptions().position(newLoc).title("I am here"));
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(newLoc));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLoc,DEFAULT_ZOOM));


                }
            }
        };

      /* mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(point.latitude, point.longitude)).title("New Marker");

                mMap.addMarker(marker);

                System.out.println(point.latitude+"---"+ point.longitude);
            }
        });*/



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
    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            public void onMapLongClick(LatLng latLng) {
                Intent i = new Intent();
                i.putExtra("lat", latLng.latitude);
                i.putExtra("long", latLng.longitude);

                setResult(100, i);
                realm.close();
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        AddMarkers();
        setMapLongClick(mMap);
    }


    private void AddMarkers() {
        RealmResults<ScheduleData> scheduleData = realm.where(ScheduleData.class)
                .findAllAsync();

        scheduleData.addChangeListener(new RealmChangeListener<RealmResults<ScheduleData>>() {
            Marker m;
            @Override
            public void onChange(RealmResults<ScheduleData> hazardReports) {
                mMap.clear();
                for(ScheduleData hr:hazardReports){
                    if(hr.getSubject_lat() != null && hr.getSubject_long() != null) {
                        if(!hr.getSubject_lat().trim().equals("")
                                && !hr.getSubject_long().trim().equals("")) {
                            m = mMap.addMarker(new MarkerOptions()
                                    .title(hr.getSubject_title())
                                    .position(new LatLng(Double.parseDouble(hr.getSubject_lat())
                                            , Double.parseDouble(hr.getSubject_long()))));
                            m.setTag((ScheduleData) hr);
                            m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        }
                    }
                }
            }
        });
    }
    public void onStart()
    {
        super.onStart();

        try {
            if (mFusedLocationClient == null) {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(UPDATE_INTERVAL);
                mLocationRequest.setFastestInterval(FATEST_INTERVAL);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
            }
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

        }
        catch(SecurityException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onPause()
    {
        super.onPause();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
