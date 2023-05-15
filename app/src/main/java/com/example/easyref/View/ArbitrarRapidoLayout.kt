package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easyref.Adaptadores.RecyclerAdapterArbitrarJugadores
import com.example.easyref.Adaptadores.RecyclerAdapterJugadores
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.JugadorEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.*

class ArbitrarRapidoLayout : Fragment() {
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    var delay:Long = 5


    var segundos = 0
    var segundosParar = 0
    var segundosSegunda = 0

    var minutos = 0
    var minutosParar = 0

    var partidoEmpezado = false
    var primeraParte = true
    var final = false
    var extraTime = 0
    private var dialog : AlertDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()

        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        // fuera del oncreateview se bloquea
        var duracionParte = datosViewModel.getDuracionParte.value!!
        var minutosSegunda = duracionParte

        var coroutineParar = CoroutineScope(Dispatchers.Main)
        var coroutinePrimera = CoroutineScope(Dispatchers.Main)
        var coroutineSegunda = CoroutineScope(Dispatchers.Main)
        var coroutineExtra= CoroutineScope(Dispatchers.Main)


        var view = inflater.inflate(R.layout.arbitrar_rapido_layout, container, false)
        var cronometroIniciado = false
        var descanso = datosViewModel.getDescanso.value!!


        // CON DESCANSO
        if(descanso){
            /// START CRONOMETRO
            view.findViewById<ImageButton>(R.id.startCrono).setOnClickListener{
                if(primeraParte && !final){
                    view.findViewById<LinearLayout>(R.id.cronoPrimera).visibility = View.VISIBLE
                    view.findViewById<LinearLayout>(R.id.cronoSegunda).visibility = View.GONE
                    if(!cronometroIniciado){
                        cronometroIniciado = true
                        coroutineParar?.launch {
                            while (isActive){
                                delay(delay)
                                segundosParar++
                                if(segundosParar == 60){
                                    segundosParar = 0
                                    minutosParar++
                                }
                                view.findViewById<TextView>(R.id.segundosParar).setText(segundosParar.toString().padStart(2, '0'))
                                view.findViewById<TextView>(R.id.minutosParar).setText(minutosParar.toString().padStart(2, '0'))
                            }
                        }
                        if(partidoEmpezado == false){
                            partidoEmpezado = true
                            coroutinePrimera?.launch {
                                while (isActive){
                                    delay(delay)
                                    segundos++
                                    if(segundos == 60){
                                        segundos = 0
                                        minutos++
                                        if(minutos == duracionParte){
                                            extraTime = minutos - minutosParar
                                            minutosParar = 0
                                            segundosParar = 0
                                            cronometroIniciado = false
                                            coroutinePrimera.cancel()
                                            coroutineParar.cancel()
                                            coroutinePrimera = CoroutineScope(Dispatchers.Main)
                                            coroutineParar = CoroutineScope(Dispatchers.Main)

                                            if(extraTime > 0){
                                                view.findViewById<TextView>(R.id.extraTime).text = "EXTRA: "+extraTime
                                                view.findViewById<TextView>(R.id.extraTime).visibility = View.VISIBLE

                                                coroutineExtra?.launch {
                                                    while (isActive){
                                                        delay(delay)
                                                        segundosParar++
                                                        if (segundosParar == 60) {
                                                            segundosParar = 0
                                                            minutosParar++
                                                            if(minutosParar >= extraTime){
                                                                view.findViewById<TextView>(R.id.extraTime).visibility = View.INVISIBLE
                                                                coroutineExtra.cancel()
                                                                coroutinePrimera.cancel()
                                                                coroutineParar.cancel()
                                                                coroutineExtra = CoroutineScope(Dispatchers.Main)
                                                                coroutinePrimera = CoroutineScope(Dispatchers.Main)
                                                                coroutineParar = CoroutineScope(Dispatchers.Main)
                                                                minutosParar = duracionParte
                                                                segundosParar = 0
                                                            }
                                                        }
                                                        view.findViewById<TextView>(R.id.segundosParar).setText(segundosParar.toString().padStart(2, '0'))
                                                        view.findViewById<TextView>(R.id.minutosParar).setText(minutosParar.toString().padStart(2, '0'))
                                                    }
                                                }
                                            }
                                            cronometroIniciado = false
                                            primeraParte = false
                                            partidoEmpezado = false
                                        }
                                    }
                                    view.findViewById<TextView>(R.id.segundos).setText(segundos.toString().padStart(2, '0'))
                                    view.findViewById<TextView>(R.id.minutos).setText(minutos.toString().padStart(2, '0'))
                                }
                            }
                        }
                    }
                }else if(!primeraParte && !final) {
                    view.findViewById<LinearLayout>(R.id.cronoPrimera).visibility = View.GONE
                    view.findViewById<LinearLayout>(R.id.cronoSegunda).visibility = View.VISIBLE

                    if(!cronometroIniciado){
                        cronometroIniciado = true
                        coroutineParar?.launch {
                            while (isActive){
                                delay(delay)
                                segundosParar++
                                if(segundosParar == 60){
                                    segundosParar = 0
                                    minutosParar++
                                }
                                view.findViewById<TextView>(R.id.segundosParar).setText(segundosParar.toString().padStart(2, '0'))
                                view.findViewById<TextView>(R.id.minutosParar).setText(minutosParar.toString().padStart(2, '0'))
                            }
                        }
                        if(partidoEmpezado == false){
                            partidoEmpezado = true
                            coroutineSegunda?.launch {
                                while (isActive){
                                    delay(delay)
                                    segundosSegunda++
                                    if(segundosSegunda == 60){
                                        segundosSegunda = 0
                                        minutosSegunda++
                                        if(minutosSegunda == (duracionParte*2)){
                                            extraTime = minutosSegunda - minutosParar
                                            minutosParar = 0
                                            segundosParar = 0
                                            cronometroIniciado = false
                                            coroutineSegunda.cancel()
                                            coroutineParar.cancel()
                                            coroutineSegunda = CoroutineScope(Dispatchers.Main)
                                            coroutineParar = CoroutineScope(Dispatchers.Main)

                                            if(extraTime > 0){
                                                view.findViewById<TextView>(R.id.extraTime).text = "EXTRA: "+extraTime
                                                view.findViewById<TextView>(R.id.extraTime).visibility = View.VISIBLE

                                                coroutineExtra?.launch {
                                                    while (isActive){
                                                        delay(delay)
                                                        segundosParar++
                                                        if (segundosParar == 60) {
                                                            segundosParar = 0
                                                            minutosParar++
                                                            if(minutosParar == extraTime){
                                                                view.findViewById<TextView>(R.id.extraTime).visibility = View.INVISIBLE
                                                                coroutineExtra.cancel()
                                                                coroutineSegunda.cancel()
                                                                coroutineParar.cancel()
                                                                coroutineExtra = CoroutineScope(Dispatchers.Main)
                                                                coroutineSegunda = CoroutineScope(Dispatchers.Main)
                                                                coroutineParar = CoroutineScope(Dispatchers.Main)
                                                                minutosParar = (duracionParte*2) + extraTime
                                                                segundosParar = 0
                                                            }
                                                        }
                                                        view.findViewById<TextView>(R.id.segundosParar).setText(segundosParar.toString().padStart(2, '0'))
                                                        view.findViewById<TextView>(R.id.minutosParar).setText(minutosParar.toString().padStart(2, '0'))
                                                    }
                                                }
                                            }
                                            cronometroIniciado = false
                                            final = true
                                            var navHost = NavHostFragment.findNavController(this@ArbitrarRapidoLayout)
                                            navHost.navigate(R.id.action_arbitrarRapidoLayout_to_infoSimpleLayout)
                                        }
                                    }
                                    view.findViewById<TextView>(R.id.segundosSegunda).setText(segundosSegunda.toString().padStart(2, '0'))
                                    view.findViewById<TextView>(R.id.minutosSegunda).setText(minutosSegunda.toString().padStart(2, '0'))
                                }
                            }
                        }
                    }
                }
            }

            // STOP CRONOMETRO
            view.findViewById<ImageButton>(R.id.pausaCrono).setOnClickListener{
                coroutineParar.cancel()
                coroutineExtra.cancel()
                coroutineParar = CoroutineScope(Dispatchers.Main)
                coroutineExtra = CoroutineScope(Dispatchers.Main)
                cronometroIniciado = false
            }
        }

        //SIN DESCANSO
        else{
            view.findViewById<ImageButton>(R.id.startCrono).setOnClickListener{
                    view.findViewById<LinearLayout>(R.id.cronoPrimera).visibility = View.VISIBLE
                    view.findViewById<LinearLayout>(R.id.cronoSegunda).visibility = View.GONE
                    if(!cronometroIniciado){
                        cronometroIniciado = true
                        coroutineParar?.launch {
                            while (isActive){
                                delay(delay)
                                segundosParar++
                                if(segundosParar == 60){
                                    segundosParar = 0
                                    minutosParar++
                                }
                                view.findViewById<TextView>(R.id.segundosParar).setText(segundosParar.toString().padStart(2, '0'))
                                view.findViewById<TextView>(R.id.minutosParar).setText(minutosParar.toString().padStart(2, '0'))
                            }
                        }
                        if(partidoEmpezado == false){
                            partidoEmpezado = true
                            coroutinePrimera?.launch {
                                while (isActive){
                                    delay(delay)
                                    segundos++
                                    if(segundos == 60){
                                        segundos = 0
                                        minutos++
                                        if(minutos == duracionParte){
                                            extraTime = minutos - minutosParar
                                            minutosParar = 0
                                            segundosParar = 0
                                            cronometroIniciado = false
                                            coroutinePrimera.cancel()
                                            coroutineParar.cancel()
                                            coroutinePrimera = CoroutineScope(Dispatchers.Main)
                                            coroutineParar = CoroutineScope(Dispatchers.Main)

                                            if(extraTime > 0){
                                                view.findViewById<TextView>(R.id.extraTime).text = "EXTRA: "+extraTime
                                                view.findViewById<TextView>(R.id.extraTime).visibility = View.VISIBLE

                                                coroutineExtra?.launch {
                                                    while (isActive){
                                                        delay(delay)
                                                        segundosParar++
                                                        if (segundosParar == 60) {
                                                            segundosParar = 0
                                                            minutosParar++
                                                            if(minutosParar >= extraTime){
                                                                view.findViewById<TextView>(R.id.extraTime).visibility = View.INVISIBLE
                                                                coroutineExtra.cancel()
                                                                coroutinePrimera.cancel()
                                                                coroutineParar.cancel()
                                                                coroutineExtra = CoroutineScope(Dispatchers.Main)
                                                                coroutinePrimera = CoroutineScope(Dispatchers.Main)
                                                                coroutineParar = CoroutineScope(Dispatchers.Main)
                                                                minutosParar = duracionParte
                                                                segundosParar = 0
                                                            }
                                                        }
                                                        view.findViewById<TextView>(R.id.segundosParar).setText(segundosParar.toString().padStart(2, '0'))
                                                        view.findViewById<TextView>(R.id.minutosParar).setText(minutosParar.toString().padStart(2, '0'))
                                                    }
                                                }
                                            }
                                            cronometroIniciado = false
                                            primeraParte = false
                                            partidoEmpezado = false
                                            final=true
                                            var navHost = NavHostFragment.findNavController(this@ArbitrarRapidoLayout)
                                            navHost.navigate(R.id.action_arbitrarRapidoLayout_to_infoSimpleLayout)
                                        }
                                    }
                                    view.findViewById<TextView>(R.id.segundos).setText(segundos.toString().padStart(2, '0'))
                                    view.findViewById<TextView>(R.id.minutos).setText(minutos.toString().padStart(2, '0'))
                                }
                            }
                        }
                    }
                }


            // STOP CRONOMETRO
            view.findViewById<ImageButton>(R.id.pausaCrono).setOnClickListener{
                coroutineParar.cancel()
                coroutineExtra.cancel()
                coroutineParar = CoroutineScope(Dispatchers.Main)
                coroutineExtra = CoroutineScope(Dispatchers.Main)
                cronometroIniciado = false
            }
        }


        return view

    }
}