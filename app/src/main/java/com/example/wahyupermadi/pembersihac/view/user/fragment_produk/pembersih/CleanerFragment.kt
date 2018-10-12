package com.example.wahyupermadi.pembersihac.view.user.fragment_produk.pembersih

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.adapter.PembersihUserAdapter
import com.example.wahyupermadi.pembersihac.model.Pembersih
import kotlinx.android.synthetic.main.cleaner_fragment.*
import org.jetbrains.anko.support.v4.ctx

class CleanerFragment : Fragment(), CleanerContract.View{
    var pembersihs : MutableList<Pembersih> = mutableListOf()
    lateinit var presenter : CleanerPresenter
    lateinit var dialog : ProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cleaner_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog = ProgressDialog(ctx)
        dialog.setMessage("Loading....")
        dialog.setCancelable(false)

        presenter = CleanerPresenter(this)
        presenter.getPembersihList()
    }

    override fun hideProgressBar() {
        dialog.hide()
    }

    override fun showProgressBar() {
        dialog.show()
    }

    override fun ShowPembersih(pembersih: List<Pembersih>) {
        pembersihs.clear()
        Log.d(TAG,"Helllll "+pembersih)
        pembersihs.addAll(pembersih)
        val layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
        rv_cleaner.layoutManager = layoutManager
        rv_cleaner.adapter = PembersihUserAdapter(ctx, pembersihs)
    }
}