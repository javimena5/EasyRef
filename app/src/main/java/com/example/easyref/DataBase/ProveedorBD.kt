package com.example.easyref.DataBase

import com.example.easyref.Modelo.ArbitroEntity
import retrofit2.Response
import retrofit2.http.*

interface ProveedorBD {
    @GET("arbitros")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun arbitros(): Response<List<ArbitroEntity>>

    @GET("arbitros/{id}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun getArbitro(@Path("id") id: Int): Response<List<ArbitroEntity>>

    @POST("arbitros")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun insertarArbitro(@Body arbitro: ArbitroEntity): Response<RespuestaJson>

    @DELETE("arbitros/{id}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun deleteArbitro(@Path("id")id: Int) : Response<RespuestaJson>
}