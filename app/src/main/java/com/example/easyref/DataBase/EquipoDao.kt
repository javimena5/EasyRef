package com.example.easyref.DataBase

import androidx.room.*
import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.Modelo.EquipoEntity

@Dao
interface EquipoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(equipo: EquipoEntity)

    @Delete
    suspend fun delete(equipo: EquipoEntity)

    @Update
    suspend fun update(equipo: EquipoEntity)

    @Query("SELECT * FROM equipos ORDER BY id_equipo")
    suspend fun getEquipos(): List<EquipoEntity>

    @Transaction
    @Query("SELECT * FROM equipos WHERE id_equipo=(:equipoId) LIMIT 1")
    suspend fun getEquipo(equipoId:Int): EquipoEntity
}