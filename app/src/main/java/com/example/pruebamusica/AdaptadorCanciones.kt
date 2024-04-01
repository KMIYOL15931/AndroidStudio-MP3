package com.example.pruebamusica

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebamusica.databinding.RowSongBinding

class AdaptadorCanciones(val elementos:List<String>, val con:MainActivity):
        RecyclerView.Adapter<AdaptadorCanciones.ViewHolder>() {

    class ViewHolder(val bind: RowSongBinding) : RecyclerView.ViewHolder(bind.root)

    var selected = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = RowSongBinding.inflate (LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elem = elementos[position]
        with(holder.bind){
            rowNombreCancion.text = elem
            if (position==selected){
                rowCancion.setBackgroundColor(Color.LTGRAY)
            }
            else{
                rowCancion.setBackgroundColor(Color.WHITE)
            }
            rowCancion.setOnClickListener{
                con.cancionActualIndex = position
                con.refreshSong()
                selected = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return elementos.size
    }
}