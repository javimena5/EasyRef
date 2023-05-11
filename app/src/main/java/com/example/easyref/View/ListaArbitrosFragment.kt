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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyref.Adaptadores.RecyclerAdapterArbitros
import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaArbitrosFragment : Fragment() {
    lateinit var recycler: RecyclerView
    lateinit var lista: List<ArbitroEntity>
    lateinit var adaptador : RecyclerAdapterArbitros
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.lista_arbitros_fragment, container, false)
        recycler = view.findViewById(R.id.recycler)
        lista = listOf()
        cargarAdapter(view)
        //RetrofitController.crearRetrofit()
        CoroutineScope(Dispatchers.IO).launch {
            lista = EasyRefController.getArbitros()
            withContext(Dispatchers.Main){
                cargarAdapter(view)
            }
        }
        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            var navHost = NavHostFragment.findNavController(this@ListaArbitrosFragment)
            navHost.navigate(R.id.action_listaArbitrosFragment_to_datosArbitroFragment)
        }
        return view
    }

    fun cargarAdapter(view: View) {
        adaptador = RecyclerAdapterArbitros(lista)
       adaptador.onLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                val popupMenu = PopupMenu(requireContext(),v)
                popupMenu.inflate(R.menu.lista_popup_menu)

                popupMenu.setOnMenuItemClickListener(PopupMenu.
                OnMenuItemClickListener
                { item: MenuItem? ->
                    when (item!!.itemId) {
                        R.id.eliminar -> {
                            AlertDialog.Builder(requireContext()).setMessage("¿Eliminar "+
                                    lista.get(recycler.getChildAdapterPosition(v!!)).nombreArbitro+" "+
                                    lista.get(recycler.getChildAdapterPosition(v!!)).apellidosArbitro+"?")
                                .setPositiveButton("Eliminar", DialogInterface.OnClickListener {
                        dialog, id -> CoroutineScope(Dispatchers.IO).launch {
                    EasyRefController.deleteArbitro(EasyRefController.getArbitro(lista.get(recycler.getChildAdapterPosition(v!!)).idArbitro))
                    //RetrofitController.retrofit.deleteArbitro(lista.get(recycler.getChildAdapterPosition(v!!)).idArbitro)
                    //lista = RetrofitController.retrofit.arbitros().body()!!

                    lista = EasyRefController.getArbitros()
                    withContext(Dispatchers.Main){
                        cargarAdapter(view)
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

        adaptador.onClickListener(object : android.view.View.OnClickListener{
            override fun onClick(v: View?) {
                AlertDialog.Builder(requireContext()).setMessage("¿Continuar como "+
                        lista.get(recycler.getChildAdapterPosition(v!!)).nombreArbitro+" "+
                        lista.get(recycler.getChildAdapterPosition(v!!)).apellidosArbitro+"?")
                    .setPositiveButton("Si", DialogInterface.OnClickListener {
                            dialog, id ->
                                    //Toast.makeText(requireActivity(), lista.get(recycler.getChildAdapterPosition(v!!)).nombreArbitro, Toast.LENGTH_SHORT).show()
                                    datosViewModel.setArbitro(lista.get(recycler.getChildAdapterPosition(v!!)))
                                    datosViewModel.setEquipoLocal(EquipoEntity(0,"LOCAL",""))
                                    datosViewModel.setEquipoVisitante(EquipoEntity(0,"VISITANTE",""))
                                    var navHost = NavHostFragment.findNavController(this@ListaArbitrosFragment)
                                    navHost.navigate(R.id.action_listaArbitrosFragment_to_seleccionModo)

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