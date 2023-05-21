package com.example.easyref.DataBase

import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.JugadorAPI
import com.example.easyref.Modelo.JugadorEntity
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ProveedorBD {
    @GET("data/jugadores")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getJugadoresAPI(): Response<List<JugadorEntity>>

    @GET("data/jugadores/{idEquipo}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getJugadoresByIdEquipoAPI(@Path("idEquipo") id: Int): Response<List<JugadorEntity>>

    @GET("data/equipos")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getEquiposAPI(): Response<List<EquipoEntity>>

    @GET("data/equipos/{idEquipo}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getEquiposByIdAPI(@Path("idEquipo") id: Int): Response<EquipoEntity>
}