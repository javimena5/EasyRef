package com.example.easyref.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R

class InfoRapidoLayout : Fragment() {
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title = "PARTIDO RÁPIDO"
        (activity as AppCompatActivity).window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        var view = inflater.inflate(R.layout.info_rapido_layout, container, false)




        view.findViewById<Button>(R.id.empezar).setOnClickListener{
            var descanso = view.findViewById<CheckBox>(R.id.descanso).isChecked
            datosViewModel.setDescanso(descanso)
            var duracion:Int = view.findViewById<EditText>(R.id.duracion).text.toString().toInt()
            if(descanso)
                datosViewModel.setDuracionParte(duracion/2)
            else
                datosViewModel.setDuracionParte(duracion)
            if(duracion >= 1){
                var navHost = NavHostFragment.findNavController(this@InfoRapidoLayout)
                navHost.navigate(R.id.action_infoRapidoLayout_to_arbitrarRapidoLayout)
            } else
                Toast.makeText(requireActivity(), "Indique la duración del partido.", Toast.LENGTH_SHORT).show()
        }
        return view
    }
}