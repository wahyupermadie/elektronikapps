package com.example.wahyupermadi.pembersihac.view.fragmentpembersih

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.SearchView
import com.example.wahyupermadi.pembersihac.adapter.PembersihAdapter
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.view.pembersihdetail.PembersihDetailActivity
import com.example.wahyupermadi.pembersihac.view.tambahpembersih.TambahPembersihActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.permbersih_fragment.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity

class PembersihFragment : Fragment(), PembersihContract.View{
    var firebaseAuth = FirebaseAuth.getInstance()

    private var key: String? = null
    lateinit var presenter: PembersihPresenter
    private var mDatabase: DatabaseReference? = null
    private var mFirebaseDatabase: FirebaseDatabase? = null
    var data : ArrayList<Pembersih> = ArrayList()
    lateinit var dialog : ProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.permbersih_fragment, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        val uid = firebaseAuth.currentUser?.uid
        pembersih_tambah.setOnClickListener {
            startActivity<TambahPembersihActivity>()
        }
        dialog = ProgressDialog(ctx)
        dialog.setMessage("Waiting.....")
        dialog.setCancelable(false)

        presenter = PembersihPresenter(this)
        presenter.loadPembersih(uid!!)

    }

    override fun hideProgressBar() {
        dialog.hide()
    }

    override fun showProgressBar() {
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search_menu, menu)
        val searchView = menu?.findItem(R.id.searchMenu)?.actionView as SearchView

        searchView.queryHint = "Search Pembersih"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
//                presenter.loadPembersih()
                return false
            }
        })

        searchView.setOnCloseListener(object: SearchView.OnCloseListener{
            override fun onClose(): Boolean {

                return true
            }
        })
    }

    override fun showPembersih(pembersih: List<Pembersih>) {
        data.clear()
        data.addAll(pembersih)
        val layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
        rv_pembersih?.layoutManager = layoutManager
        rv_pembersih?.adapter = PembersihAdapter(ctx,data)
    }

    companion object {
        fun newInstance() : PembersihFragment = PembersihFragment()
    }
}