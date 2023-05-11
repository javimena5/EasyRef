package com.example.easyref.DataBase


import androidx.room.*
import com.example.easyref.Modelo.ArbitroEntity

@Dao
interface ArbitroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(arbitro: ArbitroEntity)

    @Delete
    suspend fun delete(arbitro: ArbitroEntity)

    @Update
    suspend fun update(arbitro: ArbitroEntity)

    @Query("SELECT * FROM arbitros ORDER BY id_arbitro")
    suspend fun getArbitros(): List<ArbitroEntity>

    @Transaction
    @Query("SELECT * FROM arbitros WHERE id_arbitro=(:arbitroId) LIMIT 1")
    suspend fun getArbitro(arbitroId:Int): ArbitroEntity
}