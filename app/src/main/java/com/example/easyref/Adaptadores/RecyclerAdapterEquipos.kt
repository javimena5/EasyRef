package com.example.easyref.Adaptadores


import android.content.Context
import android.graphics.Bitmap
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.createBitmap
import com.ejemplos.b10.myapplication.ImagenUtilidad
import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.R


class RecyclerAdapterEquipos(var c: List<EquipoEntity>) : RecyclerView.Adapter<SimpleViewHolderEquipos>(), OnClickListener, OnLongClickListener{
    lateinit var listener: OnClickListener
    lateinit var listenerLargo: OnLongClickListener
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleViewHolderEquipos {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_equipo_layout, parent, false)
        v.setOnClickListener(this)
        v.setOnLongClickListener(this)
        return SimpleViewHolderEquipos(v)
    }
    override fun onBindViewHolder(holder: SimpleViewHolderEquipos, position: Int) {
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
class SimpleViewHolderEquipos(itemView: View) :
    RecyclerView.ViewHolder(itemView)
{
    var nombre: TextView
    var foto:ImageView

    fun bind(dato: EquipoEntity) {
        nombre.setText(dato.nombreEquipo.toString())
        foto.setImageResource(R.drawable.icono_sin)
        /*if(dato.fotoArbitro.toString().equals(""))
            foto.setImageResource(R.drawable.arbitro_avatar)
        else {
            var imagenUtilidad = ImagenUtilidad()
            foto.context
            foto = imagenUtilidad.BitmapToImageView(imagenUtilidad.descargaImagenToBitmap(dato.fotoArbitro),foto.context)
        }*/

    }
    init {
        nombre = itemView.findViewById(R.id.NombreEquipo)
        foto = itemView.findViewById(R.id.escudoEquipo)
    }
}