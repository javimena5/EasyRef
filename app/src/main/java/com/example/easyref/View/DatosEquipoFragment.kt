package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.example.easyref.ViewModel.RetrofitController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatosEquipoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        RetrofitController.crearRetrofit()
        var view = inflater.inflate(R.layout.datos_equipo_fragment, container, false)

        view.findViewById<Button>(R.id.siguiente).setOnClickListener{
            var newNombre = view.findViewById<EditText>(R.id.NombreEquipo)

            var newEquipo = EquipoEntity(0,newNombre.text.toString(),"")
            CoroutineScope(Dispatchers.IO).launch {
                EasyRefController.insertEquipo(newEquipo)
            }
            view.findViewById<EditText>(R.id.NombreEquipo).setText("")
            var navHost = NavHostFragment.findNavController(this@DatosEquipoFragment)
            navHost.navigate(R.id.action_datosEquipoFragment_to_listaEquiposFragment)
            /*var navHost = NavHostFragment.findNavController(this@DatosArbitroFragment)
            navHost.navigate(R.id.action_datosArbitroFragment_to_seleccionEquiposFragment)*/
        }
        return view
    }

}