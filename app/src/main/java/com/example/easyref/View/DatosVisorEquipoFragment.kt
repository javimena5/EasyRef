package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.example.easyref.ViewModel.RetrofitController
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatosVisorEquipoFragment : Fragment() {
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        (activity as AppCompatActivity).window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        RetrofitController.crearRetrofit()
        var view = inflater.inflate(R.layout.datos_equipo_fragment, container, false)

        view.findViewById<Button>(R.id.siguiente).setOnClickListener{
            var newNombre = view.findViewById<EditText>(R.id.NombreEquipo)
            var newEscudo = view.findViewById<EditText>(R.id.urlEscudo)
            if(!newEscudo.text.equals("") && !newEscudo.text.isEmpty()){
                Picasso.with(requireContext())
                    .load(newEscudo.text.toString())
                    .into(view.findViewById<ImageView>(R.id.escudoEquipo))
            }else
                view.findViewById<ImageView>(R.id.escudoEquipo).setImageResource(R.drawable.icono_sin)

            var newEquipo = EquipoEntity(0,newNombre.text.toString(),newEscudo.text.toString())
            CoroutineScope(Dispatchers.IO).launch {
                EasyRefController.insertEquipo(newEquipo)
            }
            datosViewModel.setEquipoSeleccionado(newEquipo)
            var navHost = NavHostFragment.findNavController(this@DatosVisorEquipoFragment)
            navHost.navigate(R.id.action_datosVisorEquipoFragment_to_editorEquiposFragment)
        }
        return view
    }

}