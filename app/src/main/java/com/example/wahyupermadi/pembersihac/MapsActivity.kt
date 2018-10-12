package com.example.wahyupermadi.pembersihac

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.example.wahyupermadi.pembersihac.model.User
import com.example.wahyupermadi.pembersihac.view.admin.AdminFragment
import com.example.wahyupermadi.pembersihac.view.pembersihdetail.PembersihDetailActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.admin_fragment.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.toast
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    lateinit var mMap : GoogleMap
    lateinit var mGoogleApiClient : GoogleApiClient
    lateinit var mLocationRequest : LocationRequest
    var mCurrLocationMarker : Marker? = null
    lateinit var mLastLocation : Location
    lateinit var markerOptions: MarkerOptions
    private var lng: Double = 0.toDouble()
    private var lat: Double = 0.toDouble()
    lateinit var latLng : LatLng
    lateinit var mMapView: MapView
    lateinit var geolocate : Geocoder
    lateinit var user : User
    lateinit var adminFragment: AdminFragment
    var address : MutableList<Address> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        user = intent.getParcelableExtra("user")
        supportActionBar?.setTitle("Pilih Lokasi")

        btn_select_location.setOnClickListener {
//            user.lat = lat.toString()
//            user.lng = lng.toString()
//
            intent.putExtra("lat", lat)
            intent.putExtra("lng", lng)
            intent.putExtra("address", address.get(0).getAddressLine(0))
            setResult(Activity.RESULT_OK, intent);
            finish()
        }
    }


    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    val MY_PERMISSIONS_REQUEST_LOCATION = 99;
    fun checkLocationPermission() : Boolean{
        if (ContextCompat.checkSelfPermission(ctx,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    requestPermissions(permissions,
                            MY_PERMISSIONS_REQUEST_LOCATION);
                } else {
                    requestPermissions( permissions,
                            MY_PERMISSIONS_REQUEST_LOCATION);
                }
            }
            return false;
        } else {
            return true;
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.size > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Izin diberikan.
                    if (ContextCompat.checkSelfPermission(ctx,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(ctx, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(ctx,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(location: Location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove();
        }
        latLng = LatLng(location.getLatitude(), location.getLongitude());
        lat = latLng.latitude;
        lng = latLng.longitude;


        val cameraPosition = CameraPosition.Builder().target(LatLng(latLng.latitude, latLng.longitude)).zoom(16F).build();
        geolocate = Geocoder(ctx, Locale.getDefault())
        address = geolocate.getFromLocation(latLng.latitude, latLng.longitude, 1)
        markerOptions = MarkerOptions().position(latLng).draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mMap.addMarker(markerOptions);

        //menghentikan pembaruan lokasi
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragEnd(marker: Marker) {

                latLng = marker.getPosition();
                lat = latLng.latitude;
                lng = latLng.longitude;

                geolocate = Geocoder(ctx, Locale.getDefault())
                address = geolocate.getFromLocation(latLng.latitude, latLng.longitude, 1)
                toast(address.get(0).getAddressLine(0))
            }

            override fun onMarkerDragStart(p0: Marker?) {

            }

            override fun onMarkerDrag(p0: Marker?) {

            }

        })
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
