package com.example.wahyupermadi.pembersihac.view.user

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.example.wahyupermadi.pembersihac.LoginActivity
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.RegisterActivity
import com.example.wahyupermadi.pembersihac.view.admin.AdminFragment
import com.example.wahyupermadi.pembersihac.view.fragmentpembersih.PembersihFragment
import com.example.wahyupermadi.pembersihac.view.fragmentproduk.ProdukFragment
import com.example.wahyupermadi.pembersihac.view.user.fragment_map.MapFragment
import com.example.wahyupermadi.pembersihac.view.user.fragment_toko.FragmentToko
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationSelectedListener)

        supportActionBar?.title = "Daftar Produk"
        val produkFragment = com.example.wahyupermadi.pembersihac.view.user.fragment_produk.ProdukFragment.newInstance()
        openFragment(produkFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_login, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.id_login -> {
                startActivity<LoginActivity>()
            }
            R.id.id_register->{
                startActivity<RegisterActivity>()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val mOnNavigationSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.nav_item -> {
                supportActionBar?.title = "Daftar Produk"
                val produkFragment = com.example.wahyupermadi.pembersihac.view.user.fragment_produk.ProdukFragment.newInstance()
                openFragment(produkFragment)
            }
            R.id.nav_map -> {
                supportActionBar?.title = "Lokasi Toko"
                val mapFragment = MapFragment.newInstance()
                openFragment(mapFragment)
            }
            R.id.nav_store->{
                supportActionBar?.title = "Daftar Toko"
                val tokoFragment = FragmentToko.newInstance()
                openFragment(tokoFragment)
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
