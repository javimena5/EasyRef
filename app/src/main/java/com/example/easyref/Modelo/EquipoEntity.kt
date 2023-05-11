package com.example.easyref.Modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipos")
class EquipoEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_equipo")
    val idEquipo : Int,
    @ColumnInfo(name = "nombre_equipo")
    val nombreEquipo : String,
    @ColumnInfo(name = "escudo_equipo")
    val escudoEquipo : String
)