package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SeleccionEquiposFragment : Fragment() {
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        var view = inflater.inflate(R.layout.seleccion_equipos_fragment, container, false)

        view.findViewById<TextView>(R.id.nombreLocal).text = datosViewModel.getEquipoLocal.value?.nombreEquipo

        view.findViewById<TextView>(R.id.nombreVisitante).text = datosViewModel.getEquipoVisitante.value?.nombreEquipo
        if(!(datosViewModel.getEquipoLocal.value == null)) {
            var escudoLocal = datosViewModel.getEquipoLocal.value?.escudoEquipo
            if (!escudoLocal.equals("") || !escudoLocal!!.isEmpty())
                Picasso.with(requireContext())
                    .load(datosViewModel.getEquipoLocal.value?.escudoEquipo)
                    .into(view.findViewById<ImageView>(R.id.escudoLocal))
        }
        if(!(datosViewModel.getEquipoVisitante.value == null)) {
            var escudoVisitante = datosViewModel.getEquipoVisitante.value?.escudoEquipo
            if (!escudoVisitante.equals("") || !escudoVisitante!!.isEmpty())
                Picasso.with(requireContext())
                    .load(datosViewModel.getEquipoVisitante.value?.escudoEquipo)
                    .into(view.findViewById<ImageView>(R.id.escudoVisitante))
        }
        view.findViewById<Button>(R.id.cambiarLocal).setOnClickListener {
            datosViewModel.setEquipoCambiar("LOCAL")
            var navHost = NavHostFragment.findNavController(this@SeleccionEquiposFragment)
            navHost.navigate(R.id.action_seleccionEquiposFragment_to_listaEquiposFragment)
        }

        view.findViewById<Button>(R.id.cambiarVisitante).setOnClickListener {
            datosViewModel.setEquipoCambiar("VISITANTE")
            var navHost = NavHostFragment.findNavController(this@SeleccionEquiposFragment)
            navHost.navigate(R.id.action_seleccionEquiposFragment_to_listaEquiposFragment)
        }

        view.findViewById<Button>(R.id.siguiente).setOnClickListener {
            if(datosViewModel.getEquipoLocal.value!!.idEquipo.equals(datosViewModel.getEquipoVisitante.value!!.idEquipo))
                Toast.makeText(requireActivity(), "El LOCAL y el VISITANTE no pueden ser el mismo equipo.", Toast.LENGTH_SHORT).show()
            else{
                CoroutineScope(Dispatchers.IO).launch {
                    var locales = EasyRefController.getJugadores(datosViewModel.getEquipoLocal.value!!.idEquipo).toMutableList()
                    var visitantes = EasyRefController.getJugadores(datosViewModel.getEquipoVisitante.value!!.idEquipo).toMutableList()

                    withContext(Dispatchers.Main){
                        datosViewModel.setJugadoresLocal(locales)
                        datosViewModel.setJugadoresVisitante(visitantes)
                    }

                }
                var navHost = NavHostFragment.findNavController(this@SeleccionEquiposFragment)
                navHost.navigate(R.id.action_seleccionEquiposFragment_to_seleccionJugadoresFragment)
            }
        }

        return view
    }

}