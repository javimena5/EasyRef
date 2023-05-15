package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.graphics.Color.rgb
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.JugadorEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.example.easyref.ViewModel.RetrofitController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatosVisorJugadorFragment : Fragment() {
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        var dorsalAdd : Int = 0
        val equipoActual : EquipoEntity = datosViewModel.getEquipoSeleccionado.value!!
        var listaDorsalesDB : MutableList<Int> = mutableListOf()
        var dorsalesUsables : MutableList<Int> = mutableListOf()
        var view = inflater.inflate(R.layout.datos_visor_jugadores, container, false)
        var newDorsal = view.findViewById<Spinner>(R.id.dorsalJugador)

        CoroutineScope(Dispatchers.IO).launch {
            listaDorsalesDB = EasyRefController.getDorsales(equipoActual.idEquipo).toMutableList()

            for (i in 0..99) {
                dorsalesUsables.add(i)
            }

            dorsalesUsables.removeAll(listaDorsalesDB.toList())
            val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dorsalesUsables) as SpinnerAdapter
            newDorsal!!.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    dorsalAdd = 0
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    dorsalAdd = parent?.getItemAtPosition(position).toString().toInt()
                    (view!! as TextView).setTextColor(rgb(255,255,255))
                    newDorsal.setSelection(position)
                }

            }
            newDorsal!!.adapter = adaptador
        }





        //newDorsal.setOnItemSelectedListener()
        view.findViewById<Button>(R.id.siguiente).setOnClickListener{
            var newNombre = view.findViewById<EditText>(R.id.nombreJugador)
            var newApellidos = view.findViewById<EditText>(R.id.apellidosJugador)

            var newJugador = JugadorEntity(0,newNombre.text.toString(),newApellidos.text.toString(),dorsalAdd,"",equipoActual.idEquipo,0,0)
            CoroutineScope(Dispatchers.IO).launch {
                EasyRefController.insertJugador(newJugador)
            }
            view.findViewById<EditText>(R.id.nombreJugador).setText("")
            view.findViewById<EditText>(R.id.apellidosJugador).setText("")
            var navHost = NavHostFragment.findNavController(this@DatosVisorJugadorFragment)
            navHost.navigate(R.id.action_datosVisorJugadorFragment_to_visorJugadoresFragment)
        }
        return view
    }

}