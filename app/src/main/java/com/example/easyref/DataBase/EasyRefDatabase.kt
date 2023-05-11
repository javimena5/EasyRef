package com.example.easyref.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.JugadorEntity
import com.example.easyref.Modelo.PartidoEntity
@Database(
    entities = [ArbitroEntity::class, EquipoEntity::class, JugadorEntity::class, PartidoEntity::class],
    version = 4
)
abstract class EasyRefDatabase: RoomDatabase() {
    abstract fun arbitroDao():ArbitroDao
    abstract fun equipoDao():EquipoDao
    abstract fun jugadorDao():JugadorDao
    abstract fun partidoDao():PartidoDao
}