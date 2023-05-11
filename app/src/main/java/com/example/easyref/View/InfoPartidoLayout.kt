package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.easyref.Modelo.JugadorEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InfoPartidoLayout : Fragment() {
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        var listaJugadores = listOf<JugadorEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            listaJugadores = EasyRefController.getJugadores()
        }
        for(jug in listaJugadores){
            jug.esTitular=0
            CoroutineScope(Dispatchers.IO).launch {
                EasyRefController.updateJugador(jug)
            }
        }
        var view = inflater.inflate(R.layout.info_partido_layout, container, false)
        var stringInfo = datosViewModel.getInfoPartido.value
        view.findViewById<TextView>(R.id.infoPartido).setText(stringInfo!!.replace("|","\n"))

        view.findViewById<Button>(R.id.nuevoPartido).setOnClickListener {
            var navHost = NavHostFragment.findNavController(this@InfoPartidoLayout)
            navHost.navigate(R.id.action_infoPartidoLayout_to_seleccionModo)
        }
        return view
    }
}