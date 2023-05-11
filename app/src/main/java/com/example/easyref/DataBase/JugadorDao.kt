package com.example.easyref.DataBase

import androidx.room.*
import com.example.easyref.Modelo.JugadorEntity

@Dao
interface JugadorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(jugador: JugadorEntity)

    @Delete
    suspend fun delete(jugador: JugadorEntity)

    @Update
    suspend fun update(jugador: JugadorEntity)

    @Query("SELECT * FROM jugadores ORDER BY id_jugador")
    suspend fun getJugadores(): List<JugadorEntity>

    @Query("SELECT * FROM jugadores WHERE id_equipo=(:equipoId) ORDER BY dorsal ASC")
    suspend fun getJugadores(equipoId: Int): List<JugadorEntity>

    @Transaction
    @Query("SELECT * FROM jugadores WHERE id_jugador=(:jugadorId) LIMIT 1")
    suspend fun getJugador(jugadorId:Int?): JugadorEntity

    @Transaction
    @Query("SELECT * FROM jugadores WHERE dorsal=(:dorsal) LIMIT 1")
    suspend fun getJugadorByDorsal(dorsal:Int?): JugadorEntity

    @Query("SELECT dorsal FROM jugadores WHERE id_equipo=(:equipoId) ORDER BY dorsal ASC")
    suspend fun getDorsales(equipoId: Int): List<Int>

    @Query("SELECT * FROM jugadores WHERE id_equipo=(:equipoId) AND es_titular=1 AND expulsado=0")
    suspend fun getTitulares(equipoId: Int): List<JugadorEntity>

    @Query("SELECT * FROM jugadores WHERE id_equipo=(:equipoId) AND es_titular=0 AND expulsado=0")
    suspend fun getSuplentes(equipoId: Int): List<JugadorEntity>

    @Query("SELECT id_jugador FROM jugadores WHERE (id_equipo=(:equipoId) AND es_titular=1 AND expulsado=0) ORDER BY dorsal ASC")
    suspend fun getIdTitulares(equipoId: Int): List<Int>

    @Query("SELECT id_jugador FROM jugadores WHERE (id_equipo=(:equipoId) AND es_titular=0 AND expulsado=0) ORDER BY dorsal ASC")
    suspend fun getIdSuplentes(equipoId: Int): List<Int>

    @Query("SELECT id_jugador FROM jugadores WHERE (id_equipo=(:equipoId) AND expulsado=0) ORDER BY dorsal ASC")
    suspend fun getIdJugadores(equipoId: Int): List<Int>


}