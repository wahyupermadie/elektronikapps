package com.example.wahyupermadi.pembersihac.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.widget.Toolbar
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.view.admin.AdminFragment
import com.example.wahyupermadi.pembersihac.view.fragmentpembersih.PembersihFragment
import com.example.wahyupermadi.pembersihac.view.fragmentproduk.ProdukFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var toolbar: ActionBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = supportActionBar

        toolbar?.setTitle("Produk Ac")
        val produkFragment = ProdukFragment.newInstance()
        openFragment(produkFragment)

        navigationView.setOnNavigationItemSelectedListener(mOnNavigationSelectedListener)
    }

    private val mOnNavigationSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.nav_ac -> {
                toolbar?.title = "Produk AC"
                val produkFragment = ProdukFragment.newInstance()
                openFragment(produkFragment)
            }
            R.id.nav_clear -> {
                toolbar?.title = "Pembersih AC"
                val pembersihFragment = PembersihFragment.newInstance()
                openFragment(pembersihFragment)
            }
            R.id.nav_profil->{
                toolbar?.title = "Profil Toko"
                val profileFragment = AdminFragment.newInstance()
                openFragment(profileFragment)
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
