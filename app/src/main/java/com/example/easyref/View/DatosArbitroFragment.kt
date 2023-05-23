package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.example.easyref.ViewModel.RetrofitController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatosArbitroFragment : Fragment() {
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
        var view = inflater.inflate(R.layout.datos_arbitro_fragment, container, false)

        view.findViewById<Button>(R.id.siguiente).setOnClickListener{
            var newNombre = view.findViewById<EditText>(R.id.nombreArbitro)
            var newApellidos = view.findViewById<EditText>(R.id.apellidosArbitro)

            var newArbitro = ArbitroEntity(0,newNombre.text.toString(),newApellidos.text.toString(),"")
            CoroutineScope(Dispatchers.IO).launch {
                EasyRefController.insertArbitro(newArbitro)
            }
            view.findViewById<EditText>(R.id.nombreArbitro).setText("")
            view.findViewById<EditText>(R.id.apellidosArbitro).setText("")
            var navHost = NavHostFragment.findNavController(this@DatosArbitroFragment)
            navHost.navigate(R.id.action_datosArbitroFragment_to_listaArbitrosFragment)
            /*var navHost = NavHostFragment.findNavController(this@DatosArbitroFragment)
            navHost.navigate(R.id.action_datosArbitroFragment_to_seleccionEquiposFragment)*/
        }
        return view
    }

}