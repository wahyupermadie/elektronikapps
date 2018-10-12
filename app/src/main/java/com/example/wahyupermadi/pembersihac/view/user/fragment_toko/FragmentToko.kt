package com.example.wahyupermadi.pembersihac.view.user.fragment_toko

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.adapter.TokoAdapter
import com.example.wahyupermadi.pembersihac.model.User
import kotlinx.android.synthetic.main.fragment_toko.*
import org.jetbrains.anko.support.v4.ctx


class FragmentToko : Fragment(), TokoContract.View{
    lateinit var presenter: TokoPresenter
    var users : MutableList<User> = mutableListOf()
    lateinit var dialog : ProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_toko, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog = ProgressDialog(ctx)
        dialog.setMessage("Loading...")
        dialog.setCancelable(false)

        presenter = TokoPresenter(this)
        presenter.getToko()
    }

    override fun hideLoading() {
        dialog.hide()
    }

    override fun showToko(toko: List<User>) {
        users.clear()
        users.addAll(toko)
        val layoutManager = LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL, false)
        rv_toko.layoutManager = layoutManager
        rv_toko.adapter = TokoAdapter(ctx,users)
    }

    override fun showLoading() {
        dialog.show()
    }
    companion object {
        fun newInstance() : FragmentToko = FragmentToko()
    }
}
