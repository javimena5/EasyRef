package com.example.easyref.Modelo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "partidos",
    foreignKeys = arrayOf(
        ForeignKey(entity = EquipoEntity::class,
            parentColumns = arrayOf("id_equipo"),
            childColumns = arrayOf("id_local"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(entity = EquipoEntity::class,
            parentColumns = arrayOf("id_equipo"),
            childColumns = arrayOf("id_visitante"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(entity = ArbitroEntity::class,
            parentColumns = arrayOf("id_arbitro"),
            childColumns = arrayOf("id_arbitro"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
class PartidoEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_partido")
    val idPartido : Int,
    @ColumnInfo(name = "id_local")
    val idLocal : Int,
    @ColumnInfo(name = "id_visitante")
    val idVisitante : Int,
    @ColumnInfo(name = "id_arbitro")
    val idArbitro : Int,
    @ColumnInfo(name = "goles_local")
    val golesLocal : Int,
    @ColumnInfo(name = "goles_visitante")
    val golesVisitante : Int,
)