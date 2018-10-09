package com.example.wahyupermadi.pembersihac.view.fragmentproduk

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import android.widget.SearchView
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.adapter.PembersihAdapter
import com.example.wahyupermadi.pembersihac.adapter.ProdukAdapter
import com.example.wahyupermadi.pembersihac.model.Pembersih
import com.example.wahyupermadi.pembersihac.model.Produk
import com.example.wahyupermadi.pembersihac.view.fragmentpembersih.PembersihFragment
import com.example.wahyupermadi.pembersihac.view.fragmentpembersih.PembersihPresenter
import com.example.wahyupermadi.pembersihac.view.tambah_produk.TambahProdukActivity
import com.example.wahyupermadi.pembersihac.view.tambahpembersih.TambahPembersihActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.permbersih_fragment.*
import kotlinx.android.synthetic.main.produk_fragment.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.startActivity

class ProdukFragment : Fragment(), ProdukContact.View{

    var firebaseAuth = FirebaseAuth.getInstance()
    lateinit var presenter: ProdukPresenter
    var data : ArrayList<Produk> = ArrayList()
    lateinit var dialog : ProgressDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.produk_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        val uid = firebaseAuth.currentUser?.uid
        produk_tambah.setOnClickListener {
            startActivity<TambahProdukActivity>()
        }

        dialog = ProgressDialog(ctx)
        dialog.setMessage("Waiting.....")
        dialog.setCancelable(false)

        presenter = ProdukPresenter(this)
        presenter.loadProduk(uid!!)

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.search_menu, menu)
        val searchView = menu?.findItem(R.id.searchMenu)?.actionView as SearchView

        searchView.queryHint = "Search Produk"
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

    override fun showProduk(produk: List<Produk>) {
        data.clear()
        data.addAll(produk)
        val layoutManager = GridLayoutManager(ctx, 2, GridLayoutManager.VERTICAL, false)
        rv_produk?.layoutManager = layoutManager
        rv_produk?.adapter = ProdukAdapter(ctx,data)
    }
    override fun hideProgressBar() {
        dialog.hide()
    }
    override fun showProgressBar() {
        dialog.show()
    }

    companion object {
        fun newInstance() : ProdukFragment = ProdukFragment()
    }
}