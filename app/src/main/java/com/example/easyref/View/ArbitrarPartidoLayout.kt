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

class ArbitrarPartidoLayout : Fragment() {
    lateinit var dialogBuilder:MaterialAlertDialogBuilder
    lateinit var adaptador : RecyclerAdapterArbitrarJugadores
    lateinit var recycler: RecyclerView
    lateinit var jugadoresMostrar: MutableList<JugadorEntity>
    var listaJugadoresLocales: MutableList<JugadorEntity> = mutableListOf()
    var listaJugadoresVisitantes: MutableList<JugadorEntity> = mutableListOf()
    var listaJugadoresTitularesLocal: MutableList<JugadorEntity> = mutableListOf()
    var listaJugadoresTitularesVisitante: MutableList<JugadorEntity> = mutableListOf()
    var listaJugadoresSuplentesLocal: MutableList<JugadorEntity> = mutableListOf()
    var listaJugadoresSuplentesVisitante: MutableList<JugadorEntity> = mutableListOf()
    lateinit var listaJugadoresAmarilla: MutableList<JugadorEntity>
    lateinit var listaJugadoresRoja: MutableList<JugadorEntity>
    var jugadorSale: JugadorEntity? = null
    var jugadorEntra: JugadorEntity? = null
    var seleccionado: JugadorEntity? = null
    private val datosViewModel : PasarDatosViewModel by activityViewModels()
    var infoPartido:String = ""
    var infoSucesos:String = ""
    var delay:Long = 5
    var golesLocal = 0
    var golesVisitante = 0


    var segundos = 0
    var segundosParar = 0
    var segundosSegunda = 0

    var minutos = 0
    var minutosParar = 0

    var partidoEmpezado = false
    var pausaReglamentaria = false
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

        var listaIdLocales : List<Int> = listOf<Int>()
        var listaIdVisitantes : List<Int> = listOf<Int>()
        var listaIdTitularesLocalesDB : List<Int> = listOf<Int>()
        var listaIdTitularesVisitantesDB : List<Int> = listOf<Int>()
        var listaIdSuplentesLocalesDB : List<Int> = listOf<Int>()
        var listaIdSuplentesVisitantesDB : List<Int> = listOf<Int>()


        CoroutineScope(Dispatchers.IO).launch {
            listaIdLocales = EasyRefController.getIdJugadores(datosViewModel.getEquipoLocal.value?.idEquipo)
            listaIdTitularesLocalesDB = EasyRefController.getIdTitulares(datosViewModel.getEquipoLocal.value?.idEquipo)
            listaIdSuplentesLocalesDB = EasyRefController.getIdSuplentes(datosViewModel.getEquipoLocal.value?.idEquipo)
            listaIdVisitantes= EasyRefController.getIdJugadores(datosViewModel.getEquipoVisitante.value?.idEquipo)
            listaIdTitularesVisitantesDB = EasyRefController.getIdTitulares(datosViewModel.getEquipoVisitante.value?.idEquipo)
            listaIdSuplentesVisitantesDB = EasyRefController.getIdSuplentes(datosViewModel.getEquipoVisitante.value?.idEquipo)

            for(id in listaIdLocales){
                var jug = EasyRefController.getJugador(id)
                withContext(Dispatchers.Main){
                    listaJugadoresLocales.add(jug)
                }
            }
            for(id in listaIdTitularesLocalesDB){
                var jug = EasyRefController.getJugador(id)
                withContext(Dispatchers.Main){
                    listaJugadoresTitularesLocal.add(jug)
                }
            }
            for(id in listaIdSuplentesLocalesDB){
                var jug = EasyRefController.getJugador(id)
                withContext(Dispatchers.Main){
                    listaJugadoresSuplentesLocal.add(jug)
                }
            }

            for(id in listaIdVisitantes){
                var jug = EasyRefController.getJugador(id)
                withContext(Dispatchers.Main){
                    listaJugadoresVisitantes.add(jug)
                }
            }
            for(id in listaIdTitularesVisitantesDB){
                var jug = EasyRefController.getJugador(id)
                withContext(Dispatchers.Main){
                    listaJugadoresTitularesVisitante.add(jug)
                }
            }
            for(id in listaIdSuplentesVisitantesDB){
                var jug = EasyRefController.getJugador(id)
                withContext(Dispatchers.Main){
                    listaJugadoresSuplentesVisitante.add(jug)
                }
            }
        }

        activity?.apply {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        jugadoresMostrar = mutableListOf()
        listaJugadoresRoja = mutableListOf()
        listaJugadoresAmarilla = mutableListOf()

        // fuera del oncreateview se bloquea
        var duracionParte = datosViewModel.getDuracionParte.value!!
        var minutosSegunda = duracionParte

        var coroutineParar = CoroutineScope(Dispatchers.Main)
        var coroutinePrimera = CoroutineScope(Dispatchers.Main)
        var coroutineSegunda = CoroutineScope(Dispatchers.Main)
        var coroutineExtra= CoroutineScope(Dispatchers.Main)


        var view = inflater.inflate(R.layout.arbitrar_layout, container, false)

        // idTitularesLocal|idSuplentesLocal|idTitularesVisitante|idSuplentesVisitante|
        var cronometroIniciado = false
        for(jug in listaIdTitularesLocalesDB)
            infoPartido += jug.toString()+"$"
        infoPartido += "|"
        for(jug in listaIdSuplentesLocalesDB)
            infoPartido += jug.toString()+"$"
        infoPartido += "|"
        for(jug in listaIdTitularesVisitantesDB)
            infoPartido += jug.toString()+"$"
        infoPartido += "|"
        for(jug in listaIdSuplentesVisitantesDB)
            infoPartido += jug.toString()+"$"
        infoPartido += "|"

        /// START CRONOMETRO
        view.findViewById<ImageButton>(R.id.startCrono).setOnClickListener{
            view.findViewById<TextView>(R.id.marcador).text = ""+golesLocal + " - "+ golesVisitante
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
                                        pausaReglamentaria = false
                                        coroutinePrimera.cancel()
                                        coroutineParar.cancel()
                                        coroutinePrimera = CoroutineScope(Dispatchers.Main)
                                        coroutineParar = CoroutineScope(Dispatchers.Main)

                                        if(extraTime > 0){
                                            infoPartido += "Descuento: "+extraTime+"|"
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
                                        pausaReglamentaria = false
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
                        if(!pausaReglamentaria)minutosParar = duracionParte
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
                                        pausaReglamentaria = false
                                        coroutineSegunda.cancel()
                                        coroutineParar.cancel()
                                        coroutineSegunda = CoroutineScope(Dispatchers.Main)
                                        coroutineParar = CoroutineScope(Dispatchers.Main)

                                        if(extraTime > 0){
                                            infoPartido += "Descuento: "+extraTime+"|"
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
                                        pausaReglamentaria = false
                                        final = true
                                        var navHost = NavHostFragment.findNavController(this@ArbitrarPartidoLayout)
                                        datosViewModel.setInfoPartido(infoPartido)
                                        datosViewModel.setInfoSucesos(infoSucesos)
                                        datosViewModel.setInfoResultado(""+golesLocal+" - "+golesVisitante)
                                        navHost.navigate(R.id.action_arbitrarPartidoLayout_to_infoPartidoLayout)
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

        view.findViewById<Button>(R.id.amarillaJugador).setOnClickListener{
            coroutineParar.cancel()
            coroutineExtra.cancel()
            coroutinePrimera.cancel()
            coroutineSegunda.cancel()
            coroutineParar = CoroutineScope(Dispatchers.Main)
            coroutineExtra = CoroutineScope(Dispatchers.Main)
            coroutinePrimera = CoroutineScope(Dispatchers.Main)
            coroutineSegunda = CoroutineScope(Dispatchers.Main)
            pausaReglamentaria = true
            cronometroIniciado = false
            partidoEmpezado = false

            val dialogBuilder = createEquiposDialogo(minutosSegunda,"amarilla")
            dialog = dialogBuilder.setCancelable(false).show()
        }

        view.findViewById<Button>(R.id.rojaJugador).setOnClickListener{
            coroutineParar.cancel()
            coroutineExtra.cancel()
            coroutinePrimera.cancel()
            coroutineSegunda.cancel()
            coroutineParar = CoroutineScope(Dispatchers.Main)
            coroutineExtra = CoroutineScope(Dispatchers.Main)
            coroutinePrimera = CoroutineScope(Dispatchers.Main)
            coroutineSegunda = CoroutineScope(Dispatchers.Main)
            pausaReglamentaria = true
            cronometroIniciado = false
            partidoEmpezado = false

            val dialogBuilder = createEquiposDialogo(minutosSegunda,"roja")
            dialog = dialogBuilder.setCancelable(false).show()
        }

        view.findViewById<Button>(R.id.golJugador).setOnClickListener{
            coroutineParar.cancel()
            coroutineExtra.cancel()
            coroutinePrimera.cancel()
            coroutineSegunda.cancel()
            coroutineParar = CoroutineScope(Dispatchers.Main)
            coroutineExtra = CoroutineScope(Dispatchers.Main)
            coroutinePrimera = CoroutineScope(Dispatchers.Main)
            coroutineSegunda = CoroutineScope(Dispatchers.Main)
            pausaReglamentaria = true
            cronometroIniciado = false
            partidoEmpezado = false

            val dialogBuilder = createEquiposDialogo(minutosSegunda,"gol")
            dialog = dialogBuilder.setCancelable(false).show()

        }

        view.findViewById<Button>(R.id.cambiarJugador).setOnClickListener{
            coroutineParar.cancel()
            coroutineExtra.cancel()
            coroutinePrimera.cancel()
            coroutineSegunda.cancel()
            coroutineParar = CoroutineScope(Dispatchers.Main)
            coroutineExtra = CoroutineScope(Dispatchers.Main)
            coroutinePrimera = CoroutineScope(Dispatchers.Main)
            coroutineSegunda = CoroutineScope(Dispatchers.Main)
            pausaReglamentaria = true
            cronometroIniciado = false
            partidoEmpezado = false

            val dialogBuilder = createEquiposDialogo(minutosSegunda,"cambio")
            dialog = dialogBuilder.setCancelable(false).show()
        }

        return view
    }

    fun createEquiposDialogo(minutosSegunda: Int,suceso:String) : MaterialAlertDialogBuilder {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = this.layoutInflater.inflate(R.layout.equipos_dialog, null)
        builder.setView(view)
        var equipoLocal:EquipoEntity = datosViewModel.getEquipoLocal.value!!
        var equipoVisitante:EquipoEntity = datosViewModel.getEquipoVisitante.value!!

        val localButton = view.findViewById<Button>(R.id.local)
        localButton.setText(equipoLocal.nombreEquipo)
        localButton.setOnClickListener{
            when(suceso){
                "gol" -> {
                    golesLocal++
                    jugadoresMostrar = listaJugadoresTitularesLocal
                    dialogBuilder = createDorsalDialogo(minutosSegunda,"LOCAL",suceso)
                }
                "amarilla","roja" -> {
                    jugadoresMostrar = listaJugadoresLocales
                    dialogBuilder = createDorsalDialogo(minutosSegunda,"LOCAL",suceso)
                }
                "cambio" -> {
                    jugadoresMostrar = listaJugadoresTitularesLocal
                    jugadoresMostrar.removeAll(listaJugadoresRoja)
                    dialogBuilder = createCambioSaleDialogo(minutosSegunda,"LOCAL")
                }
            }
            cargarAdapter("LOCAL",minutosSegunda,suceso)
            dialog?.dismiss()
            dialog = dialogBuilder.setCancelable(false).show()
        }

        val visitanteButton =view.findViewById<Button>(R.id.visitante)
        visitanteButton.setText(equipoVisitante.nombreEquipo)
        visitanteButton.setOnClickListener{
            when(suceso){
                "gol" -> {
                    golesVisitante++
                    jugadoresMostrar = listaJugadoresTitularesVisitante
                    dialogBuilder = createDorsalDialogo(minutosSegunda,"VISITANTE",suceso)
                }
                "amarilla","roja" -> {
                    jugadoresMostrar = listaJugadoresVisitantes
                    dialogBuilder = createDorsalDialogo(minutosSegunda,"VISITANTE",suceso)
                }
                "cambio" -> {
                    jugadoresMostrar = listaJugadoresTitularesVisitante
                    jugadoresMostrar.removeAll(listaJugadoresRoja)
                    dialogBuilder = createCambioSaleDialogo(minutosSegunda,"VISITANTE")
                }
            }
            cargarAdapter("VISITANTE",minutosSegunda,suceso)
            dialog?.dismiss()
            dialog = dialogBuilder.setCancelable(false).show()
        }

        return builder
    }

    fun createDorsalDialogo(minutosSegunda: Int,tipo:String,suceso:String) : MaterialAlertDialogBuilder {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = this.layoutInflater.inflate(R.layout.dorsal_dialog, null)
        builder.setView(view)
        recycler = view.findViewById(R.id.recyclerJugadores)
        return builder
    }

    fun createCambioSaleDialogo(minutosSegunda: Int,tipo:String) : MaterialAlertDialogBuilder {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = this.layoutInflater.inflate(R.layout.cambio_sale_dialog, null)
        builder.setView(view)
        recycler = view.findViewById(R.id.recyclerJugadores)
        return builder
    }

    fun createCambioDialogo(minutosSegunda: Int,tipo:String) : MaterialAlertDialogBuilder {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = this.layoutInflater.inflate(R.layout.cambio_suplentes_dialog, null)
        builder.setView(view)
        recycler = view.findViewById(R.id.recyclerJugadores)
        return builder
    }

    fun cargarAdapter(tipo:String,minutosSegunda: Int,suceso: String){
        var salir:Boolean = true
        adaptador = RecyclerAdapterArbitrarJugadores(jugadoresMostrar) // la lista esta vacia pero no sale
        adaptador.onClickListener(object : android.view.View.OnClickListener{
            override fun onClick(v: View?) {
                seleccionado = jugadoresMostrar.get(recycler.getChildAdapterPosition(v!!))
                if(primeraParte) {
                    when(suceso){
                        "gol"-> {
                            if(listaJugadoresSuplentesLocal.contains(seleccionado)||listaJugadoresSuplentesVisitante.contains(seleccionado)){
                                Toast.makeText(requireContext(),"Acción inválida. El jugador está en el banquillo",Toast.LENGTH_SHORT)
                                salir = false
                            }
                            else{
                                salir = true
                                infoSucesos += "GOL: " + seleccionado!!.nombreJugador + " " + seleccionado!!.apellidosJugador + " (" +
                                        minutos.toString().padStart(2, '0') + ":" + segundos.toString()
                                    .padStart(2, '0') + ")|"
                            }

                        }
                        "amarilla"->  {
                            if(listaJugadoresAmarilla.contains(seleccionado)){
                                listaJugadoresRoja.add(seleccionado!!)
                                listaJugadoresLocales.remove(seleccionado)
                                listaJugadoresVisitantes.remove(seleccionado)
                                listaJugadoresTitularesLocal.remove(seleccionado)
                                listaJugadoresTitularesVisitante.remove(seleccionado)
                                listaJugadoresSuplentesLocal.remove(seleccionado)
                                listaJugadoresSuplentesVisitante.remove(seleccionado)
                                jugadoresMostrar.remove(seleccionado)
                                seleccionado!!.expulsado = 1
                            }else if(listaJugadoresRoja.contains(seleccionado)){
                                Toast.makeText(requireContext(),"Acción inválida. El jugador está expulsado",Toast.LENGTH_SHORT)
                                salir = false
                            }
                            else
                                listaJugadoresAmarilla.add(seleccionado!!)
                            infoSucesos += "AMARILLA: " + seleccionado!!.nombreJugador + " " + seleccionado!!.apellidosJugador + " (" +
                                    minutos.toString().padStart(2, '0') + ":" + segundos.toString()
                                .padStart(2, '0') + ")|"
                        }
                        "roja"->  {
                            if(listaJugadoresRoja.contains(seleccionado)){
                                Toast.makeText(requireContext(),"Acción inválida. El jugador está expulsado",Toast.LENGTH_SHORT)
                                salir = false
                            }else{
                                listaJugadoresRoja.add(seleccionado!!)
                                listaJugadoresLocales.remove(seleccionado)
                                listaJugadoresVisitantes.remove(seleccionado)
                                listaJugadoresTitularesLocal.remove(seleccionado)
                                listaJugadoresTitularesVisitante.remove(seleccionado)
                                listaJugadoresSuplentesLocal.remove(seleccionado)
                                listaJugadoresSuplentesVisitante.remove(seleccionado)
                                jugadoresMostrar.remove(seleccionado)
                                seleccionado!!.expulsado = 1
                                infoSucesos += "ROJA: " + seleccionado!!.nombreJugador + " " + seleccionado!!.apellidosJugador + " (" +
                                        minutos.toString().padStart(2, '0') + ":" + segundos.toString()
                                    .padStart(2, '0') + ")|"
                            }
                        }
                        "cambio"->  {
                            var selecExpuls = seleccionado
                            selecExpuls!!.expulsado = 1
                            if(listaJugadoresRoja.contains(seleccionado) || listaJugadoresRoja.contains(selecExpuls)){
                                Toast.makeText(requireContext(),"Acción inválida. El jugador está expulsado",Toast.LENGTH_SHORT)
                                salir = false
                            }else{
                                seleccionado =
                                    jugadoresMostrar.get(recycler.getChildAdapterPosition(v!!))
                                jugadorSale = seleccionado
                                dialogBuilder = createCambioDialogo(minutosSegunda, tipo)
                            }
                        }
                    }
                }else {
                    when (suceso){
                        "gol"->{
                            if(listaJugadoresSuplentesLocal.contains(seleccionado)||listaJugadoresSuplentesVisitante.contains(seleccionado)) {
                                salir = false
                                Toast.makeText(requireContext(), "Acción inválida. El jugador está en el banquillo", Toast.LENGTH_SHORT)
                            }else {
                                salir = true
                                infoSucesos += "GOL: " + seleccionado!!.nombreJugador + " " + seleccionado!!.apellidosJugador + " (" +
                                        minutosSegunda.toString()
                                            .padStart(2, '0') + ":" + segundosSegunda.toString()
                                    .padStart(2, '0') + ")|"
                            }
                        }
                        "amarilla"-> {
                            if(listaJugadoresAmarilla.contains(seleccionado)){
                                listaJugadoresRoja.add(seleccionado!!)
                                listaJugadoresLocales.remove(seleccionado)
                                listaJugadoresVisitantes.remove(seleccionado)
                                listaJugadoresTitularesLocal.remove(seleccionado)
                                listaJugadoresTitularesVisitante.remove(seleccionado)
                                listaJugadoresSuplentesLocal.remove(seleccionado)
                                listaJugadoresSuplentesVisitante.remove(seleccionado)
                                jugadoresMostrar.remove(seleccionado)
                                seleccionado!!.expulsado = 1
                            }else if(listaJugadoresRoja.contains(seleccionado)){
                                Toast.makeText(requireContext(),"Acción inválida. El jugador está expulsado",Toast.LENGTH_SHORT)
                                salir = false
                            }
                            else
                                listaJugadoresAmarilla.add(seleccionado!!)
                            infoSucesos += "AMARILLA: " + seleccionado!!.nombreJugador + " " + seleccionado!!.apellidosJugador + " (" +
                                    minutosSegunda.toString()
                                        .padStart(2, '0') + ":" + segundosSegunda.toString()
                                .padStart(2, '0') + ")|"
                        }
                        "roja"-> {
                            if(listaJugadoresRoja.contains(seleccionado)){
                                Toast.makeText(requireContext(),"Acción inválida. El jugador está expulsado",Toast.LENGTH_SHORT)
                                salir = false
                            }else{
                                listaJugadoresRoja.add(seleccionado!!)
                                listaJugadoresLocales.remove(seleccionado)
                                listaJugadoresVisitantes.remove(seleccionado)
                                listaJugadoresTitularesLocal.remove(seleccionado)
                                listaJugadoresTitularesVisitante.remove(seleccionado)
                                listaJugadoresSuplentesLocal.remove(seleccionado)
                                listaJugadoresSuplentesVisitante.remove(seleccionado)
                                jugadoresMostrar.remove(seleccionado)
                                seleccionado!!.expulsado = 1
                                infoSucesos += "ROJA: " + seleccionado!!.nombreJugador + " " + seleccionado!!.apellidosJugador + " (" +
                                        minutosSegunda.toString()
                                            .padStart(2, '0') + ":" + segundosSegunda.toString()
                                    .padStart(2, '0') + ")|"
                            }
                        }
                        "cambio"-> {
                            var selecExpuls = seleccionado
                            selecExpuls!!.expulsado = 1
                            if(listaJugadoresRoja.contains(seleccionado) || listaJugadoresRoja.contains(selecExpuls)){
                                Toast.makeText(requireContext(),"Acción inválida. El jugador está expulsado",Toast.LENGTH_SHORT)
                                salir = false
                            }else{
                                seleccionado =
                                    jugadoresMostrar.get(recycler.getChildAdapterPosition(v!!))
                                jugadorSale = seleccionado
                                dialogBuilder = createCambioDialogo(minutosSegunda, tipo)
                            }
                        }
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    EasyRefController.updateJugador(seleccionado!!)
                }
                if (salir){
                    dialog?.dismiss()
                    if(suceso.equals("cambio")){
                        cargarAdapterSuplentes(tipo, minutosSegunda)
                        dialog = dialogBuilder.setCancelable(false).show()
                    }
                }
            }
        })
        adaptador.notifyDataSetChanged()
        recycler.adapter = adaptador
        recycler.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
    }

    fun cargarAdapterSuplentes(tipo:String,minutosSegunda: Int){
        if(tipo.equals("LOCAL")){
            jugadoresMostrar = listaJugadoresSuplentesLocal
        }else{
            jugadoresMostrar = listaJugadoresSuplentesVisitante
        }

        adaptador = RecyclerAdapterArbitrarJugadores(jugadoresMostrar)
        adaptador.onClickListener(object : android.view.View.OnClickListener{
            override fun onClick(v: View?) {
                seleccionado = jugadoresMostrar.get(recycler.getChildAdapterPosition(v!!))
                jugadorEntra = seleccionado
                if(primeraParte){
                    infoSucesos += "CAMBIO: "+jugadorEntra!!.nombreJugador+" "+ jugadorEntra!!.apellidosJugador+" <-> "+
                            jugadorSale!!.nombreJugador+" "+ jugadorSale!!.apellidosJugador + " (" +
                            minutos.toString().padStart(2, '0')+":"+segundos.toString().padStart(2, '0')+")|"
                }else {
                    infoSucesos += "CAMBIO: "+jugadorEntra!!.nombreJugador+" "+ jugadorEntra!!.apellidosJugador+" <-> "+
                            jugadorSale!!.nombreJugador+" "+ jugadorSale!!.apellidosJugador + " (" +
                                minutosSegunda.toString().padStart(2, '0')+":"+segundosSegunda.toString().padStart(2, '0')+")|"
                }
                if(tipo.equals("LOCAL")){
                    listaJugadoresSuplentesLocal.remove(jugadorEntra)
                    listaJugadoresSuplentesLocal.add(jugadorSale!!)
                    listaJugadoresTitularesLocal.remove(jugadorSale)
                    listaJugadoresTitularesLocal.add(jugadorEntra!!)
                }else{
                    listaJugadoresSuplentesVisitante.remove(jugadorEntra)
                    listaJugadoresSuplentesVisitante.add(jugadorSale!!)
                    listaJugadoresTitularesVisitante.remove(jugadorSale)
                    listaJugadoresTitularesVisitante.add(jugadorEntra!!)
                }
                jugadorSale!!.esTitular = 0
                jugadorEntra!!.esTitular = 1
                CoroutineScope(Dispatchers.IO).launch {
                    EasyRefController.updateJugador(jugadorSale!!)
                    EasyRefController.updateJugador(jugadorEntra!!)
                }
                dialog?.dismiss()
            }
        })
        adaptador.notifyDataSetChanged()
        recycler.adapter = this.adaptador
        recycler.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
    }
}