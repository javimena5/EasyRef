package com.example.easyref.Adaptadores


import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.easyref.Modelo.JugadorEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R


class RecyclerAdapterJugadores(var c: List<JugadorEntity>,tipo:String) : RecyclerView.Adapter<SimpleViewHolderJugadores>(), OnClickListener, OnLongClickListener{
    lateinit var listener: OnClickListener
    lateinit var listenerLargo: OnLongClickListener
    var mandar : String = tipo
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleViewHolderJugadores {

        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_jugador_layout, parent, false)
        v.setOnClickListener(this)
        v.setOnLongClickListener(this)
        return SimpleViewHolderJugadores(v,mandar)
    }
    override fun onBindViewHolder(holder: SimpleViewHolderJugadores, position: Int) {
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
class SimpleViewHolderJugadores(itemView: View,tipo:String) :
    RecyclerView.ViewHolder(itemView)
{
    var nombre: TextView
    var apellidos: TextView
    var foto:ImageView
    var dorsal:TextView
    var comparar:String

    fun bind(dato: JugadorEntity) {
        nombre.setText(dato.nombreJugador.toString())
        apellidos.setText(dato.apellidosJugador.toString())
        if(comparar == "LOCAL")
            foto.setImageResource(R.drawable.jugador_local_avatar)
        else
            foto.setImageResource(R.drawable.jugador_visitante_avatar)
        dorsal.setText(dato.dorsal.toString())
        /*if(dato.fotoArbitro.toString().equals(""))
            foto.setImageResource(R.drawable.arbitro_avatar)
        else {
            var imagenUtilidad = ImagenUtilidad()
            foto.context
            foto = imagenUtilidad.BitmapToImageView(imagenUtilidad.descargaImagenToBitmap(dato.fotoArbitro),foto.context)
        }*/

    }
    init {
        nombre = itemView.findViewById(R.id.nombreJugador)
        apellidos = itemView.findViewById(R.id.apellidosJugador)
        foto = itemView.findViewById(R.id.fotoJugador)
        dorsal = itemView.findViewById(R.id.dorsalJugador)
        comparar = tipo
    }
}