package com.example.easyref.View

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.graphics.Color.rgb
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyref.Adaptadores.RecyclerAdapterJugadores
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.JugadorEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SeleccionJugadoresFragment : Fragment() {
    lateinit var recyclerLocales: RecyclerView
    lateinit var recyclerVisitantes: RecyclerView
    lateinit var listaLocales: List<JugadorEntity>
    lateinit var listaVisitantes: List<JugadorEntity>
    lateinit var adaptadorLocales : RecyclerAdapterJugadores
    lateinit var adaptadorVisitantes : RecyclerAdapterJugadores
    var contadorLocales = 0
    var contadorVisitantes = 0
    var listaIdTitularesVisitantes:List<Int> = listOf()
    var listaIdTitularesLocales:List<Int> = listOf()
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        (activity as AppCompatActivity).supportActionBar?.title = "Selección de titulares"

        var view = inflater.inflate(R.layout.seleccion_titulares_layout, container, false)
        recyclerLocales = view.findViewById(R.id.recyclerLocales)
        recyclerVisitantes = view.findViewById(R.id.recyclerVisitantes)
        listaLocales = listOf()
        listaVisitantes = listOf()
        cargarAdapterLocales()
        cargarAdapterVisitantes()
        //RetrofitController.crearRetrofit()

        CoroutineScope(Dispatchers.IO).launch {
            EasyRefController.updateTitulares()
            listaLocales = EasyRefController.getJugadores(datosViewModel.getEquipoLocal.value?.idEquipo)
            listaVisitantes = EasyRefController.getJugadores(datosViewModel.getEquipoVisitante.value?.idEquipo)

            withContext(Dispatchers.Main) {
                cargarAdapterLocales()
                cargarAdapterVisitantes()
            }
        }
        view.findViewById<TextView>(R.id.equipoLocal).setText(datosViewModel.getEquipoLocal.value!!.nombreEquipo)
        view.findViewById<TextView>(R.id.equipoVisitante).setText(datosViewModel.getEquipoVisitante.value!!.nombreEquipo)
        view.findViewById<TextView>(R.id.contadorVisitantes).setText("0/"+datosViewModel.getNumeroJugadores.value)
        view.findViewById<TextView>(R.id.contadorLocales).setText("0/"+datosViewModel.getNumeroJugadores.value)

        view.findViewById<Button>(R.id.siguiente).setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                listaIdTitularesVisitantes = EasyRefController.getIdTitulares(datosViewModel.getEquipoVisitante.value?.idEquipo)
                listaIdTitularesLocales = EasyRefController.getIdTitulares(datosViewModel.getEquipoLocal.value?.idEquipo)
            }
            if(contadorLocales < datosViewModel.getNumeroJugadores.value!! ||
                contadorVisitantes < datosViewModel.getNumeroJugadores.value!!){
                Toast.makeText(requireContext(),"Debe seleccionar primero los titulares del partido.",Toast.LENGTH_SHORT).show()
            }else if(contadorLocales > datosViewModel.getNumeroJugadores.value!! ||
            contadorVisitantes > datosViewModel.getNumeroJugadores.value!!){
            Toast.makeText(requireContext(),"No se pueden seleccionar mas de "+datosViewModel.getNumeroJugadores.value!!+" jugadores.",Toast.LENGTH_SHORT).show()
        }
            else {
                var navHost = NavHostFragment.findNavController(this@SeleccionJugadoresFragment)
                navHost.navigate(R.id.action_seleccionJugadoresFragment_to_arbitrarPartidoLayout)
            }

        }

        return view
    }

    fun cargarAdapterVisitantes(){
        var equipoVisitante:EquipoEntity = datosViewModel.getEquipoVisitante.value!!
        CoroutineScope(Dispatchers.IO).launch {
            listaVisitantes = EasyRefController.getJugadores(equipoVisitante.idEquipo)
            listaIdTitularesVisitantes = EasyRefController.getIdTitulares(equipoVisitante.idEquipo)
        }
        adaptadorVisitantes = RecyclerAdapterJugadores(listaVisitantes,"VISITANTE")
        adaptadorVisitantes.onLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                val popupMenu = PopupMenu(requireContext(),v)
                popupMenu.inflate(R.menu.lista_popup_menu)

                popupMenu.setOnMenuItemClickListener(
                    PopupMenu.
                    OnMenuItemClickListener
                    { item: MenuItem? ->
                        when (item!!.itemId) {
                            R.id.eliminar -> {
                                AlertDialog.Builder(requireContext()).setMessage("¿Eliminar "+
                                        listaVisitantes.get(recyclerVisitantes.getChildAdapterPosition(v!!)).nombreJugador+" "+
                                        listaVisitantes.get(recyclerVisitantes.getChildAdapterPosition(v!!)).apellidosJugador+"?")
                                    .setPositiveButton("Eliminar", DialogInterface.OnClickListener {
                                            dialog, id -> CoroutineScope(Dispatchers.IO).launch {
                                        EasyRefController.deleteJugador(EasyRefController.getJugador(listaVisitantes.get(recyclerVisitantes.getChildAdapterPosition(v!!)).idJugador))

                                        listaVisitantes = EasyRefController.getJugadores(equipoVisitante.idEquipo)
                                        withContext(Dispatchers.Main){
                                            cargarAdapterVisitantes()
                                        }
                                    }
                                    })
                                    .setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                                            dialog, id -> dialog.cancel()
                                    }).show()
                            }
                            R.id.editar-> {
                                Toast.makeText(requireContext(),item.title,Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        true
                    })
                popupMenu.show()
                return true
            }
        })

        adaptadorVisitantes.onClickListener(object : android.view.View.OnClickListener{
            override fun onClick(v: View?) {
                var jugador = listaVisitantes.get(recyclerVisitantes.getChildAdapterPosition(v!!)) //-----------



                if(listaIdTitularesVisitantes.contains(jugador.idJugador)){
                    v!!.findViewById<CardView>(R.id.tarjeta).setCardBackgroundColor(rgb(93, 93, 106))
                    contadorVisitantes--


                    jugador.esTitular = 0
                    CoroutineScope(Dispatchers.IO).launch {
                        EasyRefController.updateJugador(jugador)
                    }
                }else {
                    contadorVisitantes++
                    v!!.findViewById<CardView>(R.id.tarjeta).setCardBackgroundColor(rgb(250, 173, 125))
                    var jugador = listaVisitantes.get(recyclerVisitantes.getChildAdapterPosition(v!!))
                    jugador.esTitular = 1
                    CoroutineScope(Dispatchers.IO).launch {
                        EasyRefController.updateJugador(jugador)
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    listaIdTitularesVisitantes = EasyRefController.getIdTitulares(equipoVisitante.idEquipo)
                    withContext(Dispatchers.Main){
                        cargarAdapterVisitantes()
                    }
                }
                view!!.findViewById<TextView>(R.id.contadorVisitantes).setText(contadorVisitantes.toString()+"/"+datosViewModel.getNumeroJugadores.value)
            }
        })

        recyclerVisitantes.adapter = this.adaptadorVisitantes
        recyclerVisitantes.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    fun cargarAdapterLocales(){
        var equipoLocal:EquipoEntity = datosViewModel.getEquipoLocal.value!!
        CoroutineScope(Dispatchers.IO).launch {
            listaLocales = EasyRefController.getJugadores(equipoLocal.idEquipo)
            listaIdTitularesLocales = EasyRefController.getIdTitulares(equipoLocal.idEquipo)
        }
        adaptadorLocales = RecyclerAdapterJugadores(listaLocales,"LOCAL")
        adaptadorLocales.onLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                val popupMenu = PopupMenu(requireContext(),v)
                popupMenu.inflate(R.menu.lista_popup_menu)

                popupMenu.setOnMenuItemClickListener(
                    PopupMenu.
                    OnMenuItemClickListener
                    { item: MenuItem? ->
                        when (item!!.itemId) {
                            R.id.eliminar -> {
                                AlertDialog.Builder(requireContext()).setMessage("¿Eliminar "+
                                        listaLocales.get(recyclerLocales.getChildAdapterPosition(v!!)).nombreJugador+" "+
                                        listaLocales.get(recyclerLocales.getChildAdapterPosition(v!!)).apellidosJugador+"?")
                                    .setPositiveButton("Eliminar", DialogInterface.OnClickListener {
                                            dialog, id -> CoroutineScope(Dispatchers.IO).launch {
                                        EasyRefController.deleteJugador(EasyRefController.getJugador(listaLocales.get(recyclerLocales.getChildAdapterPosition(v!!)).idJugador))

                                        listaLocales = EasyRefController.getJugadores(equipoLocal.idEquipo)
                                        withContext(Dispatchers.Main){
                                            cargarAdapterLocales()
                                        }
                                    }
                                    })
                                    .setNegativeButton("Cancelar", DialogInterface.OnClickListener {
                                            dialog, id -> dialog.cancel()
                                    }).show()
                            }
                            R.id.editar-> {
                                Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        true
                    })
                popupMenu.show()
                return true
            }
        })

        adaptadorLocales.onClickListener(object : android.view.View.OnClickListener{
            override fun onClick(v: View?) {
                var jugador = listaLocales.get(recyclerLocales.getChildAdapterPosition(v!!)) //-----------



                if(listaIdTitularesLocales.contains(jugador.idJugador)){
                    //v!!.findViewById<CardView>(R.id.tarjeta).setCardBackgroundColor(rgb(255, 255, 255))
                    contadorLocales--


                    jugador.esTitular = 0
                    CoroutineScope(Dispatchers.IO).launch {
                        EasyRefController.updateJugador(jugador)
                    }
                }else {
                    contadorLocales++
                    //v!!.findViewById<CardView>(R.id.tarjeta).setCardBackgroundColor(rgb(106, 161, 119))
                    var jugador = listaLocales.get(recyclerLocales.getChildAdapterPosition(v!!))
                    jugador.esTitular = 1
                    CoroutineScope(Dispatchers.IO).launch {
                        EasyRefController.updateJugador(jugador)
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    listaIdTitularesLocales = EasyRefController.getIdTitulares(equipoLocal.idEquipo)
                    withContext(Dispatchers.Main){
                        cargarAdapterLocales()
                    }
                }
                view!!.findViewById<TextView>(R.id.contadorLocales).setText(contadorLocales.toString()+"/"+datosViewModel.getNumeroJugadores.value)
            }
        })

        recyclerLocales.adapter = this.adaptadorLocales
        recyclerLocales.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }
}