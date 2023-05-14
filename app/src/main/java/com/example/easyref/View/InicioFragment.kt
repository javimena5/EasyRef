package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController

class InicioFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        //EasyRefController.iniciarDB(requireActivity().application)
        var view = inflater.inflate(R.layout.inicio_fragment, container, false)
        view.findViewById<Button>(R.id.modoArbitro).setOnClickListener{
            var navHost = NavHostFragment.findNavController(this@InicioFragment)
            navHost.navigate(R.id.action_inicioFragment_to_listaArbitrosFragment)
        }

        view.findViewById<Button>(R.id.modoRapido).setOnClickListener{
            var navHost = NavHostFragment.findNavController(this@InicioFragment)
            navHost.navigate(R.id.action_inicioFragment_to_modoVarFragment)
        }
        return view
    }

}