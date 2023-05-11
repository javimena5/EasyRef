package com.example.easyref.ViewModel

import android.app.Application
import androidx.room.Room
import com.example.easyref.DataBase.EasyRefDatabase
import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.JugadorEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object EasyRefController {
    private lateinit var db: EasyRefDatabase

    fun iniciarDB(aplicacion: Application){
        db= Room.databaseBuilder(
            aplicacion,
            EasyRefDatabase::class.java, "easyref"
        ).fallbackToDestructiveMigration()
        .build()

        CoroutineScope(Dispatchers.IO).launch {
            if(getArbitros().isEmpty()){
                InicializarData.listaArbitros().forEach {
                    CoroutineScope(Dispatchers.IO).launch {
                        insertArbitro(it)
                    }
                }
            }

            if(getEquipos().isEmpty()){
                InicializarData.listaEquipos().forEach {
                    CoroutineScope(Dispatchers.IO).launch {
                        insertEquipo(it)
                    }
                }
            }

            if(getJugadores().isEmpty()){
                InicializarData.listaJugadores().forEach {
                    CoroutineScope(Dispatchers.IO).launch {
                        insertJugador(it)
                    }
                }
            }
            var listaJugadores = db.jugadorDao().getJugadores()

            for(jug in listaJugadores){
                jug.esTitular=0
                jug.expulsado=0
                db.jugadorDao().update(jug)
            }

        }
    }
    suspend fun deleteArbitro(arbitro: ArbitroEntity)=db.arbitroDao().delete(arbitro)
    suspend fun insertArbitro(arbitro: ArbitroEntity)=db.arbitroDao().insert(arbitro)
    suspend fun updateArbitro(arbitro: ArbitroEntity)=db.arbitroDao().update(arbitro)
    suspend fun getArbitros()=db.arbitroDao().getArbitros()
    suspend fun getArbitro(arbitroId:Int)=db.arbitroDao().getArbitro(arbitroId)

    suspend fun deleteEquipo(equipo: EquipoEntity)=db.equipoDao().delete(equipo)
    suspend fun insertEquipo(equipo: EquipoEntity)=db.equipoDao().insert(equipo)
    suspend fun updateEquipo(equipo: EquipoEntity)=db.equipoDao().update(equipo)
    suspend fun getEquipos()=db.equipoDao().getEquipos()
    suspend fun getEquipo(equipoId:Int)=db.equipoDao().getEquipo(equipoId)

    suspend fun deleteJugador(jugador: JugadorEntity)=db.jugadorDao().delete(jugador)
    suspend fun insertJugador(jugador: JugadorEntity)=db.jugadorDao().insert(jugador)
    suspend fun updateJugador(jugador: JugadorEntity)=db.jugadorDao().update(jugador)
    suspend fun getJugadores()=db.jugadorDao().getJugadores()
    suspend fun getIdJugadores(equipoId: Int?)=db.jugadorDao().getIdJugadores(equipoId!!)
    suspend fun getJugadores(equipoId: Int?)=db.jugadorDao().getJugadores(equipoId!!)
    suspend fun getJugador(jugadorId:Int)=db.jugadorDao().getJugador(jugadorId)
    suspend fun getDorsales(equipoId: Int?)=db.jugadorDao().getDorsales(equipoId!!)
    suspend fun getJugadorByDorsal(dorsal: Int)=db.jugadorDao().getJugadorByDorsal(dorsal)
    suspend fun getTitulares(equipoId: Int?)=db.jugadorDao().getTitulares(equipoId!!)
    suspend fun getSuplentes(equipoId: Int?)=db.jugadorDao().getSuplentes(equipoId!!)
    suspend fun getIdTitulares(equipoId: Int?)=db.jugadorDao().getIdTitulares(equipoId!!)
    suspend fun getIdSuplentes(equipoId: Int?)=db.jugadorDao().getIdSuplentes(equipoId!!)



}

