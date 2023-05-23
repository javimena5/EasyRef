package com.example.easyref.View

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyref.Adaptadores.RecyclerAdapterArbitrarJugadores
import com.example.easyref.Adaptadores.RecyclerAdapterEquipos
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

class VisorJugadoresFragment : Fragment() {
    lateinit var recycler: RecyclerView
    lateinit var lista: List<JugadorEntity>
    lateinit var adaptador : RecyclerAdapterArbitrarJugadores
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        var view = inflater.inflate(R.layout.visor_jugadores, container, false)


        recycler = view.findViewById(R.id.recycler)
        lista = listOf()
        cargarAdapter()
        //RetrofitController.crearRetrofit()
        CoroutineScope(Dispatchers.IO).launch {
            lista = EasyRefController.getJugadores(datosViewModel.getEquipoSeleccionado.value!!.idEquipo)
            withContext(Dispatchers.Main){
                cargarAdapter()
            }
        }
        view.findViewById<TextView>(R.id.NombreEquipo).text = datosViewModel.getEquipoSeleccionado.value!!.nombreEquipo
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            var navHost = NavHostFragment.findNavController(this@VisorJugadoresFragment)
            navHost.navigate(R.id.action_visorJugadoresFragment_to_datosVisorJugadorFragment)
        }

        return view
    }

    fun cargarAdapter(){
        var stringCambiar = datosViewModel.getEquipoCambiar.value
        adaptador = RecyclerAdapterArbitrarJugadores(lista)
        adaptador.onLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                AlertDialog.Builder(requireContext()).setMessage(
                    "¿Eliminar " +
                            lista.get(recycler.getChildAdapterPosition(v!!)).nombreJugador + " " +
                            lista.get(recycler.getChildAdapterPosition(v!!)).apellidosJugador + "?"
                )
                    .setPositiveButton(
                        "Eliminar",
                        DialogInterface.OnClickListener { dialog, id ->
                            CoroutineScope(Dispatchers.IO).launch {
                                EasyRefController.deleteJugador(
                                    EasyRefController.getJugador(
                                        lista.get(recycler.getChildAdapterPosition(v!!)).idJugador
                                    )
                                )

                                lista = EasyRefController.getJugadores(datosViewModel.getEquipoSeleccionado.value!!.idEquipo)
                                withContext(Dispatchers.Main) {
                                    cargarAdapter()
                                }
                            }
                        })
                    .setNegativeButton(
                        "Cancelar",
                        DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        }).show()

                return true
            }
        })

        adaptador.onClickListener(object : android.view.View.OnClickListener{
            override fun onClick(v: View?) {
                Toast.makeText(requireContext(), lista.get(recycler.getChildAdapterPosition(v!!)).nombreJugador+" Editar", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        recycler.adapter = this.adaptador
        recycler.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }
}