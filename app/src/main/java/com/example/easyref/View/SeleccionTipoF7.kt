package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R

class SeleccionTipoF7 : Fragment() {
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        var view = inflater.inflate(R.layout.seleccion_tipof7, container, false)
        view.findViewById<Button>(R.id.futbol7).setOnClickListener {
            datosViewModel.setTipoPartido("7")
            var navHost = NavHostFragment.findNavController(this@SeleccionTipoF7)
            navHost.navigate(R.id.action_seleccionTipoF7_to_seleccionEquiposFragment)
        }

        view.findViewById<Button>(R.id.kingsleague).setOnClickListener {
            datosViewModel.setTipoPartido("kingsleague")
            var navHost = NavHostFragment.findNavController(this@SeleccionTipoF7)
            navHost.navigate(R.id.action_seleccionTipoF7_to_seleccionEquiposFragment)
        }
        return view
    }

}