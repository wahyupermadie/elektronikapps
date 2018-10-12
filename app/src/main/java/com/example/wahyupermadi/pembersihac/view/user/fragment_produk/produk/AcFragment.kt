package com.example.wahyupermadi.pembersihac.view.user.fragment_produk.produk

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.adapter.ProdukUserAdapter
import com.example.wahyupermadi.pembersihac.model.Produk
import kotlinx.android.synthetic.main.ac_fragment.*
import org.jetbrains.anko.support.v4.ctx
import kotlin.math.log

class AcFragment : Fragment(), AcContract.View{
    lateinit var acPresenter: AcPresenter
    lateinit var dialog : ProgressDialog
    var produks : MutableList<Produk> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ac_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog = ProgressDialog(ctx)
        dialog.setMessage("Loading....")
        dialog.setCancelable(false)
        acPresenter = AcPresenter(this)
        acPresenter.getProductList()
    }

    override fun hideProgressBar() {
        dialog.hide()
    }

    override fun showProgressBar() {
        dialog.show()
    }

    override fun ShowAc(produk: List<Produk>) {
        produks.clear()
        Log.d(TAG,"Helllll "+produk)
        produks.addAll(produk)
        val layoutManager = GridLayoutManager(ctx, 2, GridLayoutManager.VERTICAL, false)
        rv_ac.layoutManager = layoutManager
        rv_ac.adapter = ProdukUserAdapter(ctx, produks)
    }
}