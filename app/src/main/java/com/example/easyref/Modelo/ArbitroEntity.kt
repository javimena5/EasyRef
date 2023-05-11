package com.example.easyref.Modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "arbitros")
class ArbitroEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_arbitro")
    val idArbitro : Int,
    @ColumnInfo(name = "nombre_arbitro")
    val nombreArbitro : String,
    @ColumnInfo(name = "apellidos_arbitro")
    val apellidosArbitro : String,
    @ColumnInfo(name = "foto_arbitro")
    val fotoArbitro: String
)