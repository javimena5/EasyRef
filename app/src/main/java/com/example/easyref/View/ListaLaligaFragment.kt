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
import com.example.easyref.DataBase.ProveedorBD
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.JugadorEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.example.easyref.ViewModel.RetrofitController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ListaLaligaFragment : Fragment() {
    lateinit var recycler: RecyclerView
    var lista: List<EquipoEntity>? = listOf()
    lateinit var adaptador : RecyclerAdapterEquipos
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.lista_laliga, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = "LA LIGA"
        recycler = view.findViewById(R.id.recycler)
        cargarAdapter()
        //RetrofitController.crearRetrofit()
        val context=this
        var salida:String?
        val proveedorServicio: ProveedorBD = RetrofitController.crearRetrofit()
        CoroutineScope(Dispatchers.IO).launch {
                val response : Response<List<EquipoEntity>> = proveedorServicio.getEquiposAPI()
                if (response.isSuccessful) {
                    val usuariosResponse = response.body()
                    lista = response.body()
                    withContext(Dispatchers.Main){
                        cargarAdapter()
                    }
                }

        }

        return view
    }

    fun cargarAdapter(){
        var stringCambiar = datosViewModel.getEquipoCambiar.value
        adaptador = RecyclerAdapterEquipos(lista)
        adaptador.onClickListener(object : android.view.View.OnClickListener{
            override fun onClick(v: View?) {
                var idPulsado = lista!!.get(recycler.getChildAdapterPosition(v!!)).idEquipo
                AlertDialog.Builder(requireContext()).setMessage("¿Descargar "+
                        lista!!.get(recycler.getChildAdapterPosition(v!!)).nombreEquipo+"?")
                    .setPositiveButton("Si", DialogInterface.OnClickListener {
                            dialog, id ->
                                val context=this
                                var salida:String?
                                val proveedorServicio: ProveedorBD = RetrofitController.crearRetrofit()
                                CoroutineScope(Dispatchers.IO).launch {
                                        val response : Response<EquipoEntity> = proveedorServicio.getEquiposByIdAPI(idPulsado)
                                        if (response.isSuccessful) {
                                            val usuariosResponse = response.body()
                                            EasyRefController.insertEquipo(response.body()!!)
                                            val responseJugs : Response<List<JugadorEntity>> = proveedorServicio.getJugadoresByIdEquipoAPI(idPulsado)
                                            for(jug:JugadorEntity in responseJugs.body()!!)
                                                EasyRefController.insertJugador(jug)
                                        }

                                }

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