package com.example.easyref.Modelo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PasarDatosViewModel : ViewModel() {

    private var arbitroActual = MutableLiveData<ArbitroEntity>()
    private var tipoPartido = MutableLiveData<String>()
    private var equipoLocal = MutableLiveData<EquipoEntity>()
    private var equipoVisitante = MutableLiveData<EquipoEntity>()
    private var equipoSeleccionado = MutableLiveData<EquipoEntity>()
    private var equipoCambiar = MutableLiveData<String>()
    private var numeroJugadores = MutableLiveData<Int>()
    private var duracionParte = MutableLiveData<Int>()
    private var infoPartido = MutableLiveData<String>()
    private var infoSucesos = MutableLiveData<String>()


    private var listaIdTitularesLocales = MutableLiveData<List<JugadorEntity>>()
    private var listaIdTitularesVisitantes = MutableLiveData<List<JugadorEntity>>()
    private var jugadoresLocal = MutableLiveData<MutableList<JugadorEntity>>()
    private var jugadoresVisitante = MutableLiveData<MutableList<JugadorEntity>>()

    var getTipoPartido = tipoPartido
    fun setTipoPartido(tipo: String){
        if(tipo.equals("5")){
            duracionParte.value = 20
            numeroJugadores.value = 5
        }
        else if (tipo.equals("7")) {
            duracionParte.value  = 25
            numeroJugadores.value = 7
        }else if (tipo.equals("11")){
            duracionParte.value  = 45
            numeroJugadores.value = 11
        }
        tipoPartido.value = tipo
    }

    var getDuracionParte = duracionParte
    var getNumeroJugadores = numeroJugadores

    var getArbitro = arbitroActual
    fun setArbitro(arbitro: ArbitroEntity){
        arbitroActual.value = arbitro
    }

    var getEquipoVisitante = equipoVisitante
    fun setEquipoVisitante(visitante: EquipoEntity){
        equipoVisitante.value = visitante
    }

    var getEquipoLocal = equipoLocal
    fun setEquipoLocal(local: EquipoEntity){
        equipoLocal.value = local
    }

    var getEquipoSeleccionado = equipoSeleccionado

    var getEquipoCambiar = equipoCambiar
    fun setEquipoCambiar(cambiar: String){
        equipoCambiar.value = cambiar
        if(cambiar.equals("LOCAL")){
            equipoSeleccionado.value = equipoLocal.value
        } else if(cambiar.equals("VISITANTE")){
            equipoSeleccionado.value = equipoVisitante.value
        }

    }

    var getInfoPartido = infoPartido
    fun setInfoPartido(info: String){
        infoPartido.value = info
    }
    var getInfoSucesos = infoSucesos
    fun setInfoSucesos(info: String){
        infoSucesos.value = info
    }

    var getJugadoresLocal = jugadoresLocal
    fun setJugadoresLocal(jugadores: MutableList<JugadorEntity>){
        jugadoresLocal.value = jugadores
    }

    var getJugadoresVisitante = jugadoresVisitante
    fun setJugadoresVisitante(jugadores: MutableList<JugadorEntity>){
        jugadoresVisitante.value = jugadores
    }
}