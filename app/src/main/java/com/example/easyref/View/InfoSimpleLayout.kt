package com.example.easyref.View

import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.util.*


class InfoSimpleLayout : Fragment() {
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
        (activity as AppCompatActivity).window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        (activity as AppCompatActivity).supportActionBar?.hide()
        var listaJugadores = listOf<JugadorEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            listaJugadores = EasyRefController.getJugadores()
        }
        var view = inflater.inflate(R.layout.info_simple, container, false)

        view.findViewById<Button>(R.id.nuevoPartido).setOnClickListener {
            var navHost = NavHostFragment.findNavController(this@InfoSimpleLayout)
            navHost.navigate(R.id.action_infoSimpleLayout_to_infoRapidoLayout)
        }
        view.findViewById<Button>(R.id.repetir).setOnClickListener {
            var navHost = NavHostFragment.findNavController(this@InfoSimpleLayout)
            navHost.navigate(R.id.action_infoSimpleLayout_to_arbitrarRapidoLayout)
        }
        view.findViewById<Button>(R.id.inicio).setOnClickListener {
            var navHost = NavHostFragment.findNavController(this@InfoSimpleLayout)
            navHost.navigate(R.id.action_infoSimpleLayout_to_inicioFragment)
        }
        return view
    }

}