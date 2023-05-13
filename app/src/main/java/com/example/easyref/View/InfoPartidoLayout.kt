package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.easyref.Modelo.JugadorEntity
import com.example.easyref.Modelo.PasarDatosViewModel
import com.example.easyref.R
import com.example.easyref.ViewModel.EasyRefController
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.*

class InfoPartidoLayout : Fragment() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    var idTitularesLocal: String = ""
    var idSuplentesLocal: String = ""
    var idTitularesVisitante: String = ""
    var idSuplentesVisitante: String = ""
    var listaJugadoresTitularesLocal: MutableList<JugadorEntity> = mutableListOf()
    var listaJugadoresTitularesVisitante: MutableList<JugadorEntity> = mutableListOf()
    var listaJugadoresSuplentesLocal: MutableList<JugadorEntity> = mutableListOf()
    var listaJugadoresSuplentesVisitante: MutableList<JugadorEntity> = mutableListOf()
    lateinit var jugL:JugadorEntity
    lateinit var jugV:JugadorEntity
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
        CoroutineScope(Dispatchers.IO).launch {
            idTitularesLocal = stringInfo!!.split("|").toTypedArray()[0]
            idSuplentesLocal = stringInfo!!.split("|").toTypedArray()[1]
            idTitularesVisitante = stringInfo!!.split("|").toTypedArray()[2]
            idSuplentesVisitante = stringInfo!!.split("|").toTypedArray()[3]

            var jug:JugadorEntity?=null
            for(id in idTitularesLocal.split("$").toTypedArray()){
                if(!id.toString().equals("")){
                    jug = EasyRefController.getJugador(id.toString().toInt())
                    withContext(Dispatchers.Main){
                        listaJugadoresTitularesLocal.add(jug!!)
                    }
                }

            }
            for(id in idSuplentesLocal.split("$").toTypedArray()){
                if(!id.toString().equals("")){
                    jug = EasyRefController.getJugador(id.toString().toInt())
                    withContext(Dispatchers.Main){
                        listaJugadoresSuplentesLocal.add(jug!!)
                    }
                }
            }
            for(id in idTitularesVisitante.split("$").toTypedArray()){
                if(!id.toString().equals("")){
                    jug = EasyRefController.getJugador(id.toString().toInt())
                    withContext(Dispatchers.Main){
                        listaJugadoresTitularesVisitante.add(jug!!)
                    }
                }

            }
            for(id in idSuplentesVisitante.split("$").toTypedArray()){
                if(!id.toString().equals("")){
                    jug = EasyRefController.getJugador(id.toString().toInt())
                    withContext(Dispatchers.Main){
                        listaJugadoresSuplentesVisitante.add(jug!!)
                    }
                }

            }
        }
        view.findViewById<TextView>(R.id.infoPartido).setText(stringInfo!!.replace("|","\n"))

        view.findViewById<Button>(R.id.nuevoPartido).setOnClickListener {
            var navHost = NavHostFragment.findNavController(this@InfoPartidoLayout)
            navHost.navigate(R.id.action_infoPartidoLayout_to_seleccionModo)
        }


        view.findViewById<Button>(R.id.generaActa).setOnClickListener{
            checkPermission(it)
        }
        return view
    }

    private fun generarPDF() {
        try {
            val calendar = Calendar.getInstance()
            val tituloPDF = "ACTA_"+calendar.get(Calendar.DAY_OF_MONTH).toString()+
                    calendar.get(Calendar.MONTH).toString()+
                    calendar.get(Calendar.YEAR).toString()+"_"+
                    calendar.get(Calendar.HOUR).toString()+
                    calendar.get(Calendar.MINUTE).toString()
            val carpeta = "/EasyRef/Actas"
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + carpeta

            val dir = File(path)
            if (!dir.exists()) {
                dir.mkdirs()
            }

            val file = File(dir, tituloPDF+".pdf")
            val fileOutputStream = FileOutputStream(file)

            val documento = Document()
            PdfWriter.getInstance(documento, fileOutputStream)

            documento.open()

            val titulo = Paragraph(
                "ACTA ARBITRAL EASYREF\n\n",
                FontFactory.getFont("arial", 22f, Font.BOLD, BaseColor.BLACK)
            )

            documento.add(titulo)

            val tablaTitulares = PdfPTable(2)
            tablaTitulares.addCell("EQUIPO LOCAL");tablaTitulares.addCell("EQUIPO VISITANTE")
            tablaTitulares.addCell(datosViewModel.getEquipoLocal.value!!.nombreEquipo)
            tablaTitulares.addCell(datosViewModel.getEquipoVisitante.value!!.nombreEquipo)
            for(i in 0..listaJugadoresTitularesLocal.size-1){
                jugL = listaJugadoresTitularesLocal[i]
                jugV = listaJugadoresTitularesVisitante[i]
                tablaTitulares.addCell(jugL.dorsal.toString()+". "+jugL!!.apellidosJugador+", "+jugL!!.nombreJugador);
                tablaTitulares.addCell(jugV.dorsal.toString()+". "+jugV!!.apellidosJugador+", "+jugV!!.nombreJugador)
            }

            documento.add(tablaTitulares)
            val divisionSuplentes = Paragraph(
                "Jugadores Suplentes\n\n",
                FontFactory.getFont("arial", 12f, Font.NORMAL, BaseColor.BLACK)
            )

            documento.add(divisionSuplentes)
            val tablaSuplentes = PdfPTable(2)
            var tamSuplentes = listaJugadoresSuplentesVisitante.size
            if(listaJugadoresSuplentesLocal.size > listaJugadoresSuplentesVisitante.size){
                tamSuplentes = listaJugadoresSuplentesLocal.size
            }
            for(i in 0..tamSuplentes-1){
                if(i<=listaJugadoresSuplentesLocal.size-1){
                    jugL = listaJugadoresSuplentesLocal[i]
                    tablaSuplentes.addCell(jugL.dorsal.toString()+". "+jugL!!.apellidosJugador+", "+jugL!!.nombreJugador);
                }else{
                    tablaSuplentes.addCell("")
                }

                if(i<=listaJugadoresSuplentesVisitante.size-1){
                    jugV = listaJugadoresSuplentesVisitante[i]
                    tablaSuplentes.addCell(jugV.dorsal.toString()+". "+jugV!!.apellidosJugador+", "+jugV!!.nombreJugador)
                }else{
                    tablaSuplentes.addCell("")
                }
            }

            documento.add(tablaSuplentes)
            documento.close()


        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        } catch (e: DocumentException) {
            e.printStackTrace()
        }

    }

    private fun checkPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                generarPDF()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) -> {
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            else -> {
                requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

}