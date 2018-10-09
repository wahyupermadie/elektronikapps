package com.example.wahyupermadi.pembersihac.view.admin

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.model.User
import com.example.wahyupermadi.pembersihac.view.fragmentpembersih.PembersihFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.admin_fragment.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import java.lang.Exception

class AdminFragment : Fragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener{

    lateinit var user : User
    lateinit var mMap : GoogleMap
    lateinit var mGoogleApiClient : GoogleApiClient
    lateinit var mLocationRequest : LocationRequest
    lateinit var mCurrLocationMarker : Marker
    lateinit var mLastLocation : Location ;
    lateinit var markerOptions: MarkerOptions;
    private var lng: Double = 0.toDouble()
    private var lat: Double = 0.toDouble()
    lateinit var latLng : LatLng
    lateinit var mMapView: MapView
    val mAuth = FirebaseAuth.getInstance()

    lateinit var supportMapFragment: SupportMapFragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.admin_fragment, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mMapView = view!!.findViewById(R.id.map_select)
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(ctx);
        } catch (e : Exception) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

        supportMapFragment.fragmentManager!!.findFragmentById(R.id.maps_current)
        val mToko = FirebaseDatabase.getInstance().getReference("user").child(mAuth.currentUser!!.uid)
        mToko.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(e: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
                toast(""+p.getValue()+mAuth.currentUser!!.uid)
                user = p.getValue(User::class.java)!!
                setData()
            }

        })

    }
    private fun setData() {
        ed_namatoko.setText(user.name)
        ed_telepontoko.setText(user.phone)
        Glide.with(context!!).load(user.image).into(profile)
        email.setText(mAuth.currentUser?.email)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
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
            }

            override fun onMarkerDragStart(p0: Marker?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onMarkerDrag(p0: Marker?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLocationChanged(p0: Location?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        fun newInstance() : AdminFragment = AdminFragment()
    }
}
