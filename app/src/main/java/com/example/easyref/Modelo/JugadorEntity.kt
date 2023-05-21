package com.example.easyref.Modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "jugadores",
foreignKeys = arrayOf(
    ForeignKey(entity = EquipoEntity::class,
        parentColumns = arrayOf("id_equipo"),
        childColumns = arrayOf("id_equipo"),
        onDelete = ForeignKey.CASCADE
    )
)
)
class JugadorEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_jugador")
    val idJugador : Int,
    @ColumnInfo(name = "nombre_jugador")
    val nombreJugador : String,
    @ColumnInfo(name = "apellidos_jugador")
    val apellidosJugador : String,
    @ColumnInfo(name = "dorsal")
    val dorsal : Int,
    @ColumnInfo(name = "foto_jugador")
    val fotoJugador : String,
    @ColumnInfo(name = "id_equipo")
    val numIdEquipo : Int,
    @ColumnInfo(name = "es_titular")
    var esTitular : Int,
    @ColumnInfo(name = "expulsado")
    var expulsado : Int
){
    constructor(
        j: JugadorAPI) : this(j.idJugador,j.nombreJugador,j.apellidosJugador,j.dorsal,j.fotoJugador,j.equipo.idEquipo,j.esTitular,j.expulsado)
}

