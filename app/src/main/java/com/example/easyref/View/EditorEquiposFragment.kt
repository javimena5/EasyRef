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

class EditorEquiposFragment : Fragment() {
    lateinit var recycler: RecyclerView
    lateinit var lista: List<EquipoEntity>
    lateinit var adaptador : RecyclerAdapterEquipos
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.title = "Equipos"
        //activity?.actionBar!!.setDisplayHomeAsUpEnabled(true)
        var view = inflater.inflate(R.layout.lista_equipos_fragment, container, false)

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
            var navHost = NavHostFragment.findNavController(this@EditorEquiposFragment)
            navHost.navigate(R.id.action_editorEquiposFragment_to_datosVisorEquipoFragment)
        }

        return view
    }

    fun cargarAdapter(){
        var stringCambiar = datosViewModel.getEquipoCambiar.value
        adaptador = RecyclerAdapterEquipos(lista)
        adaptador.onLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                val popupMenu = PopupMenu(requireContext(),v)
                popupMenu.inflate(R.menu.lista_popup_menu)

                popupMenu.setOnMenuItemClickListener(
                    PopupMenu.
                    OnMenuItemClickListener
                    { item: MenuItem? ->
                        when (item!!.itemId) {
                            R.id.eliminar -> {
                                AlertDialog.Builder(requireContext()).setMessage(
                                    "¿Eliminar " +
                                            lista.get(recycler.getChildAdapterPosition(v!!)).nombreEquipo + "?"
                                )
                                    .setPositiveButton(
                                        "Eliminar",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            CoroutineScope(Dispatchers.IO).launch {
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
                            }
                            R.id.editar -> {
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
        adaptador.onClickListener(object : android.view.View.OnClickListener{
            override fun onClick(v: View?) {
                datosViewModel.setEquipoSeleccionado(lista.get(recycler.getChildAdapterPosition(v!!)))
                var navHost = NavHostFragment.findNavController(this@EditorEquiposFragment)
                navHost.navigate(R.id.action_editorEquiposFragment_to_visorJugadoresFragment)
            }
        })
        recycler.adapter = this.adaptador
        recycler.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }
}