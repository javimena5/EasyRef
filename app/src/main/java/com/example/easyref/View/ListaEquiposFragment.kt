package com.example.easyref.View

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyref.Adaptadores.RecyclerAdapterEquipos
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaEquiposFragment : Fragment() {
    lateinit var recycler: RecyclerView
    lateinit var lista: List<EquipoEntity>
    lateinit var adaptador : RecyclerAdapterEquipos
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.lista_equipos_fragment, container, false)
        view.findViewById<FloatingActionButton>(R.id.fabLaLiga).visibility = View.INVISIBLE
        (activity as AppCompatActivity).supportActionBar?.title = "EQUIPOS"
        (activity as AppCompatActivity).window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        recycler = view.findViewById(R.id.recycler)
        lista = listOf()
        cargarAdapter()
        //RetrofitController.crearRetrofit()
        CoroutineScope(Dispatchers.IO).launch {
            lista = EasyRefController.getEquipos()
            withContext(Dispatchers.Main){
                cargarAdapter()
            }
        }
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            var navHost = NavHostFragment.findNavController(this@ListaEquiposFragment)
            navHost.navigate(R.id.action_listaEquiposFragment_to_datosEquipoFragment)
        }

        return view
    }

    fun cargarAdapter(){
        var stringCambiar = datosViewModel.getEquipoCambiar.value
        adaptador = RecyclerAdapterEquipos(lista)
       adaptador.onLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                AlertDialog.Builder(requireContext()).setMessage(
                    "¿Eliminar " +
                            lista.get(recycler.getChildAdapterPosition(v!!)).nombreEquipo + "?"
                )
                    .setPositiveButton(
                        "Eliminar",
                        DialogInterface.OnClickListener { dialog, id ->
                            CoroutineScope(Dispatchers.IO).launch {
                                var listaJugadores = EasyRefController.getJugadores(lista.get(recycler.getChildAdapterPosition(v!!)).idEquipo)
                                for(jug in listaJugadores)
                                    EasyRefController.deleteJugador(jug)
                                EasyRefController.deleteEquipo(
                                    EasyRefController.getEquipo(
                                        lista.get(recycler.getChildAdapterPosition(v!!)).idEquipo
                                    )
                                )

                                lista = EasyRefController.getEquipos()
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
                AlertDialog.Builder(requireContext()).setMessage("¿Seleccionar "+
                        lista.get(recycler.getChildAdapterPosition(v!!)).nombreEquipo+" como "+stringCambiar.toString()+"?")
                    .setPositiveButton("Si", DialogInterface.OnClickListener {
                            dialog, id ->
                                    //Toast.makeText(requireActivity(), lista.get(recycler.getChildAdapterPosition(v!!)).nombreArbitro, Toast.LENGTH_SHORT).show()
                                    when (stringCambiar.toString()){
                                        "LOCAL" -> datosViewModel.setEquipoLocal(lista.get(recycler.getChildAdapterPosition(v!!)))
                                        "VISITANTE" -> datosViewModel.setEquipoVisitante(lista.get(recycler.getChildAdapterPosition(v!!)))
                                    }
                                    var navHost = NavHostFragment.findNavController(this@ListaEquiposFragment)
                                    navHost.navigate(R.id.action_listaEquiposFragment_to_seleccionEquiposFragment)

                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    }).show()
            }
        })
        recycler.adapter = this.adaptador
        recycler.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }
}