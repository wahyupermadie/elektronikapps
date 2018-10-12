package com.example.wahyupermadi.pembersihac.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.wahyupermadi.pembersihac.R
import com.example.wahyupermadi.pembersihac.model.User
import kotlinx.android.synthetic.main.toko_list.view.*

class TokoAdapter(val context: Context, val users : List<User>) : RecyclerView.Adapter<TokoViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoViewHolder {
        return TokoViewHolder(LayoutInflater.from(context).inflate(R.layout.toko_list, parent, false))
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: TokoViewHolder, position: Int) {
        holder.bindView(users[position])
    }

}

class TokoViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    fun bindView(user: User) {
        Glide.with(itemView.context).load(user.image).into(itemView.toko_logo)
        itemView.email_toko.text = user.email
        itemView.nama_toko.text = user.name
        itemView.telepon_toko.text = user.phone
    }
}
