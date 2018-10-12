package com.example.wahyupermadi.pembersihac.view.user.fragment_map

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.model.User
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
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.ctx
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    lateinit var mMap : GoogleMap
    var mGoogleApiClient: GoogleApiClient? = null
    lateinit var mMapView: MapView
    var mLastLocation: Location? = null
    var users : MutableList<User> = mutableListOf()
    lateinit var latLng : LatLng
    var lat = 0.0
    lateinit var markerOptions: MarkerOptions
    var mCurrLocationMarker : Marker? = null
    lateinit var mLocationRequest : LocationRequest
    var lng = 0.0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View
        view = inflater.inflate(R.layout.map_fragment, container, false)
        mMapView = view.findViewById(R.id.map_fragment)
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume();
        try {
            MapsInitializer.initialize(ctx);
        } catch (e : Exception) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val mToko = FirebaseDatabase.getInstance().getReference("user")
        mToko.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(e: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                users.clear()
                for (user : DataSnapshot in p.children){
                    Log.d(TAG,"UserList "+user.getValue())
                    users.add(user.getValue(User::class.java)!!)
                }
//                Log.d(TAG,"UsersList "+users)
//                val latitude = location?.getLatitude() as Int
//                val longitude = location?.getLongitude() as Int

//                mMapView.getMapAsync({
//                    mMap = it
//                    Log.d(TAG,"UserssMap"+users.size)
//                    for(i in users){
//                        if(i.lat != null && i.lng != null){
//                            Log.d(TAG,"UserMap"+i.lat)
//                            val toko = LatLng(i.lat!!.toDouble(),i.lng!!.toDouble())
//                            var pos = LatLng(latitude.toDouble(),longitude.toDouble())
//                            val cameraPosition = CameraPosition.Builder().target(pos).zoom(16F).build();
//                            mMap?.addMarker(MarkerOptions().position(toko).title(i.name))
//                            mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//                        }
//                    }
//                })
            }

        })
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
//        Log.d(TAG,"UserssMap"+users.size)
//        for(i in users){
//            Log.d(TAG,"UserMap"+i.name)
//            val toko = LatLng(i.lat!!.toDouble(),i.lng!!.toDouble())
//            val cameraPosition = CameraPosition.Builder().target(toko).zoom(16F).build();
//            mMap?.addMarker(MarkerOptions().position(toko).title(i.name))
//            mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//        }
    }

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        mGoogleApiClient?.connect()
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
        val latLng = LatLng(location.getLatitude(), location.getLongitude());
        lat = latLng.latitude;
        lng = latLng.longitude;

        val cameraPosition = CameraPosition.Builder().target(LatLng(latLng.latitude, latLng.longitude)).zoom(16F).build();
        markerOptions = MarkerOptions().position(latLng)
        for(i in users){
            if(i.lat == null && i.lng == null || i.lat == "" && i.lng == ""){

            }else{
                val toko = LatLng(i.lat!!.toDouble(),i.lng!!.toDouble())
                mMap.addMarker(MarkerOptions().position(toko).title(i.name))
            }
        }
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//        mMap.addMarker(markerOptions)
    }

    companion object {
        fun newInstance() : MapFragment = MapFragment()
    }

    override fun onStart() {
        if(mGoogleApiClient!=null){

            mGoogleApiClient?.connect();
        }
        super.onStart()
    }

}