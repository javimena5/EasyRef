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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SeleccionModo : Fragment() {
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        var view = inflater.inflate(R.layout.seleccion_modo_layout, container, false)
        view.findViewById<Button>(R.id.f7).setOnClickListener {
            var navHost = NavHostFragment.findNavController(this@SeleccionModo)
            navHost.navigate(R.id.action_seleccionModo_to_seleccionTipoF7)
        }
        view.findViewById<Button>(R.id.f11).setOnClickListener {
            datosViewModel.setTipoPartido("11")
            var navHost = NavHostFragment.findNavController(this@SeleccionModo)
            navHost.navigate(R.id.action_seleccionModo_to_seleccionEquiposFragment)
        }
        view.findViewById<Button>(R.id.f5).setOnClickListener {
            datosViewModel.setTipoPartido("5")
            var navHost = NavHostFragment.findNavController(this@SeleccionModo)
            navHost.navigate(R.id.action_seleccionModo_to_seleccionEquiposFragment)
        }
        return view
    }

}