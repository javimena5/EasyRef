package com.example.easyref.Adaptadores


import android.graphics.Color.rgb
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.easyref.Modelo.JugadorEntity
import com.example.easyref.R
import kotlin.math.exp


class RecyclerAdapterArbitrarJugadores(var c: List<JugadorEntity>) : RecyclerView.Adapter<SimpleViewHolderArbitrarJugadores>(), OnClickListener, OnLongClickListener{
    lateinit var listener: OnClickListener
    lateinit var listenerLargo: OnLongClickListener
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleViewHolderArbitrarJugadores {

        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_arbitrar_jugador_layout, parent, false)
        v.setOnClickListener(this)
        v.setOnLongClickListener(this)
        return SimpleViewHolderArbitrarJugadores(v)
    }
    override fun onBindViewHolder(holder: SimpleViewHolderArbitrarJugadores, position: Int) {
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
class SimpleViewHolderArbitrarJugadores(itemView: View) :
    RecyclerView.ViewHolder(itemView)
{
    var nombre: TextView
    var apellidos: TextView
    var dorsal:TextView
    var expulsado:LinearLayout

    fun bind(dato: JugadorEntity) {
        nombre.setText(dato.nombreJugador.toString())
        apellidos.setText(dato.apellidosJugador.toString())
        dorsal.setText(dato.dorsal.toString())
        if(dato.expulsado==1)
            expulsado.setBackgroundColor(rgb(255,0,0))
        else
            expulsado.setBackgroundColor(rgb(255,255,255))
    }
    init {
        nombre = itemView.findViewById(R.id.nombreJugador)
        apellidos = itemView.findViewById(R.id.apellidosJugador)
        dorsal = itemView.findViewById(R.id.dorsalJugador)
        expulsado = itemView.findViewById(R.id.expulsado)
    }
}