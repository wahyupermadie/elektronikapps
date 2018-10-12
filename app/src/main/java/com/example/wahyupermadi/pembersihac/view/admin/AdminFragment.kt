package com.example.wahyupermadi.pembersihac.view.admin

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.wahyupermadi.pembersihac.LoginActivity
import com.example.wahyupermadi.pembersihac.MapsActivity
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.R.drawable.ic_user
import com.example.wahyupermadi.pembersihac.R.drawable.man
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.model.User
import com.example.wahyupermadi.pembersihac.view.editpembersih.EditPembersih
import com.example.wahyupermadi.pembersihac.view.produkdetail.ProdukDetailActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_tambah_pembersih.*
import kotlinx.android.synthetic.main.admin_fragment.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import java.lang.Exception
import java.util.*

class AdminFragment : Fragment(), OnMapReadyCallback{
    var user : User? = null
    var filePathUri: Uri? = null
    lateinit var intent: Intent
    private val IMAGE_REQUEST_CODE = 7
    var mMap : GoogleMap? = null
    private var lng: Double = 0.toDouble()
    private var lat: Double = 0.toDouble()
    lateinit var mDatabase : DatabaseReference
    lateinit var mImageStorage : StorageReference
    var uid : String? = null
    lateinit var mMapView: MapView
    lateinit var markerOptions: MarkerOptions;
    val mAuth = FirebaseAuth.getInstance()
    lateinit var dialog : ProgressDialog
    private val REQUEST_GET_DATA_FROM_SOME_ACTIVITY = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View
        val mToko = FirebaseDatabase.getInstance().getReference("user").child(mAuth.currentUser!!.uid)
        mToko.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(e: DatabaseError) {

            }

            override fun onDataChange(p: DataSnapshot) {
//                toast(""+p.getValue()+mAuth.currentUser!!.uid)
                user = p.getValue(User::class.java)!!
                setData()
            }

        })
        view = inflater.inflate(R.layout.admin_fragment, container, false)
        mMapView = view.findViewById(R.id.map_select)
        mMapView.onCreate(savedInstanceState)
        mMapView.onResume();
        try {
            MapsInitializer.initialize(ctx);
        } catch (e : Exception) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        return view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        dialog = ProgressDialog(ctx)
        dialog.setMessage("Updating...")
        dialog.setCancelable(false)

        uid = mAuth.currentUser?.uid
        mDatabase = FirebaseDatabase.getInstance().getReference("user").child(uid!!)
        mImageStorage = FirebaseStorage.getInstance().getReference("toko/")
        save_profile.setOnClickListener {
            updateToko()
        }

        profile_toko.setOnClickListener {
            intent = Intent()

            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), IMAGE_REQUEST_CODE);
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.logout_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.id_logout -> {
                mAuth.signOut()
                val intent = Intent(ctx, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun updateToko() {
        dialog.show()
        val name = ed_namatoko.text.toString().trim()
        val alamat = ed_alamattoko.text.toString().trim()
        val lat = lat_toko.text.toString()
        val lng = lng_toko.text.toString().trim()
        val telepon = ed_telepontoko.text.toString().trim()
        val time = System.currentTimeMillis().toString()
        if(filePathUri != null)
        {
            val path =  time + "." + GetFileExtension(filePathUri!!)
            val mImageStorage2 = mImageStorage.child(path)
            val uploadTask = mImageStorage2.putFile(filePathUri!!)
            uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>>{
                override fun then(uploadTask: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                    if (!uploadTask.isSuccessful){
                        throw uploadTask.exception!!
                    }

                    return mImageStorage2.downloadUrl
                }
            }).addOnCompleteListener(object : OnCompleteListener<Uri>{
                override fun onComplete(task: Task<Uri>) {
                    if(task.isSuccessful){
                        val downloadUrl = task.getResult()
                        val uploadData = User(downloadUrl.toString(),name,mAuth.currentUser?.email,telepon,mAuth.currentUser?.uid
                        ,lng,lat,alamat)

                        mDatabase.setValue(uploadData)
                        dialog.hide()
                    }
                }
            })
        }else{
            val uploadData = User(user?.image,name,mAuth.currentUser?.email,telepon,mAuth.currentUser?.uid
                    ,lng,lat,alamat)
            mDatabase.setValue(uploadData)
            dialog.hide()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_GET_DATA_FROM_SOME_ACTIVITY && resultCode == Activity.RESULT_OK) {
            val extras : Bundle
            extras = data!!.getExtras();
            val mLat = extras.get("lat")
            val mLng = extras.get("lng")
            lat = mLat as Double
            lng = mLng as Double
            lat_toko.setText(lat.toString())
            lng_toko.setText(lng.toString())
            val address = extras.get("address")
            ed_alamattoko.setText(address.toString())
        }

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            filePathUri = data.data
            try {
                // Getting selected image into Bitmap.
                val bitmap = MediaStore.Images.Media.getBitmap(ctx.contentResolver, filePathUri)
                Log.d(TAG, "Cek Cek"+filePathUri)
                // Setting up bitmap selected image into ImageView.
//                Glide.with(ctx).load(bitmap).into(profile_toko)
                profile_toko.setImageBitmap(bitmap)
            }catch (e : Exception){
                e.printStackTrace()
                Log.d(TAG, "Cek CekS")
            }
        }

        logout_admin.setOnClickListener {
            mAuth.signOut()
            val intent = Intent(ctx, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun GetFileExtension(uri : Uri): String? {
        val contentResolver : ContentResolver
        contentResolver = getActivity()!!.getContentResolver()
        val mimeTypeMap = MimeTypeMap.getSingleton()

        return mimeTypeMap.getExtensionFromMimeType(contentResolver?.getType(uri))
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        if (user?.lat == null && user?.lng == null || user?.lat == "" && user?.lng == ""){
            lat = -13.000
            lng = 25.000
        }else{
            lat = user?.lat!!.toDouble()
            lng = user?.lng!!.toDouble()
        }
        val toko = LatLng(lat,lng)
        val cameraPosition = CameraPosition.Builder().target(toko).zoom(16F).build();
        mMap?.addMarker(MarkerOptions().position(toko).title(user?.name))
        mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResume() {
        super.onResume()
        if(mMap != null) { //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            mMap?.clear();
            val toko = LatLng(lat,lng)
            val cameraPosition = CameraPosition.Builder().target(toko).zoom(16F).build();
            mMap?.addMarker(MarkerOptions().position(toko).title(user?.name))
            mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }
    private fun setData() {
        if (user?.lat != null && user?.lng != null){
            lat = lat
            lng = lng
        }else{
            lat = user?.lat!!.toDouble()
            lng = user?.lng!!.toDouble()
        }
        ed_namatoko.setText(user?.name.toString())
        name_toko.setText(user?.name.toString())
        ed_telepontoko.setText(user?.phone)
        lat_toko.setText(user?.lat.toString())

        lng_toko.setText(user?.lng.toString())
        if (user?.image.equals("") || user?.image == null)
        {
            Glide.with(context!!).load(man).into(profile_toko)
        }else{
            Glide.with(context!!).load(user?.image).into(profile_toko)
        }
        email.setText(mAuth.currentUser?.email)
        ed_alamattoko.setText(user?.alamat)
        ed_alamattoko.setOnClickListener {
            val intent = Intent(ctx, MapsActivity::class.java)
            intent.putExtra("user", user)
            startActivityForResult(intent, REQUEST_GET_DATA_FROM_SOME_ACTIVITY)
        }
    }

    companion object {
        fun newInstance() : AdminFragment = AdminFragment()
    }
}
