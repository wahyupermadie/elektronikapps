package com.example.wahyupermadi.pembersihac.view.user.fragment_produk

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.view.user.fragment_produk.pembersih.CleanerFragment
import com.example.wahyupermadi.pembersihac.view.user.fragment_produk.produk.AcFragment
import kotlinx.android.synthetic.main.produk_user_fragment.*

class ProdukFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.produk_user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = MyPagerAdapter(fragmentManager)
        viewpager_main.adapter=adapter
        tabs_main.setupWithViewPager(viewpager_main)
    }

    private class MyPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            val fragment:Fragment
            when (position) {
                0 -> {
                    fragment = AcFragment()
                }
                else -> {
                    fragment = CleanerFragment()
                }
            }
            return fragment
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence {
            return when (position) {
                0 -> "Produk Ac"
                else -> {
                    return "Cleaner"
                }
            }
        }
    }

    companion object {
        fun newInstance() : ProdukFragment = ProdukFragment()
    }

}

