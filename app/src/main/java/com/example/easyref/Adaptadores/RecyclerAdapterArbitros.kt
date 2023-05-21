package com.example.easyref.Adaptadores

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.R
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso


class RecyclerAdapterArbitros(var c: List<ArbitroEntity>) : RecyclerView.Adapter<SimpleViewHolderArbitros>(), OnClickListener, OnLongClickListener{
    lateinit var listener: OnClickListener
    lateinit var listenerLargo: OnLongClickListener
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleViewHolderArbitros {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_arbi_layout, parent, false)
        v.setOnClickListener(this)
        v.setOnLongClickListener(this)
        return SimpleViewHolderArbitros(v)
    }
    override fun onBindViewHolder(holder: SimpleViewHolderArbitros, position: Int) {
        holder.bind(c.get(position))
    }

    override fun getItemCount(): Int {
        return c.size
    }

    fun onClickListener(listener: OnClickListener)
    {
        this.listener=listener
    }

    fun onLongClickListener(listener: OnLongClickListener)
    {
        this.listenerLargo=listener
    }
    override fun onClick(v: View?) {
        listener.onClick(v)
    }

    override fun onLongClick(v: View?): Boolean {
        listenerLargo.onLongClick(v)
        return true
    }


}
class SimpleViewHolderArbitros(itemView: View) :
    RecyclerView.ViewHolder(itemView)
{
    var nombre: TextView
    var apellidos: TextView
    var foto:ImageView

    fun bind(dato: ArbitroEntity) {
        nombre.setText(dato.nombreArbitro.toString())
        apellidos.setText(dato.apellidosArbitro.toString())
        if(!dato.fotoArbitro.equals("")){
            Picasso.with(itemView.context)
                .load(dato.fotoArbitro)
                .into(foto)
        }else
            foto.setImageResource(R.drawable.icono_sin)

        //foto.setImageResource(R.drawable.arbitro_avatar)
        /*if(dato.fotoArbitro.toString().equals(""))
            foto.setImageResource(R.drawable.arbitro_avatar)
        else {
            var imagenUtilidad = ImagenUtilidad()
            foto.context
            foto = imagenUtilidad.BitmapToImageView(imagenUtilidad.descargaImagenToBitmap(dato.fotoArbitro),foto.context)
        }*/

    }
    init {
        nombre = itemView.findViewById(R.id.nombreArbitro)
        apellidos = itemView.findViewById(R.id.apellidosArbitro)
        foto = itemView.findViewById(R.id.fotoArbitro)
    }
}

