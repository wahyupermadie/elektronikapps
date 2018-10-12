package com.example.wahyupermadi.pembersihac.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.model.Produk
import com.example.wahyupermadi.pembersihac.view.produkdetail.ProdukDetailActivity
import kotlinx.android.synthetic.main.list_produk.view.*
import org.jetbrains.anko.startActivity

class ProdukUserAdapter(private val context: Context, private val produk : List<Produk>): RecyclerView.Adapter<ProdukHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukHolder {
        return  ProdukHolder(LayoutInflater.from(context).inflate(R.layout.list_produk, parent, false))
    }

    override fun getItemCount(): Int {
        return produk.size
    }

    override fun onBindViewHolder(holder: ProdukHolder, position: Int) {
        holder.bindItem(produk[position])
    }


}

class ProdukHolder(view: View?) : RecyclerView.ViewHolder(view) {
    fun bindItem(produk: Produk){

        itemView.tv_namatokoproduk.text = produk.toko
        itemView.tv_hargaproduk.text = produk.price
        itemView.tv_produk.text = produk.name
        Glide.with(itemView.context).load(produk.image).into(itemView.iv_produk)

        itemView.setOnClickListener {
//            itemView.context.startActivity<ProdukDetailActivity>("produk" to produk)
        }
    }
}