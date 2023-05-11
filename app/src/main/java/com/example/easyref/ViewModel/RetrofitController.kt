package com.example.easyref.ViewModel

import com.example.easyref.DataBase.ProveedorBD
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitController {

    lateinit var retrofit : ProveedorBD
    fun crearRetrofit(){
        val urlAndroidStudio="http://10.0.2.2:8080/easyref/data/"
        val urlMovil="http://localhost:8080/easyref/data/"
        val urlAzure="http://easyref.eastus.cloudapp.azure.com:8080/easyref/data/"
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(urlAndroidStudio)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit = retrofitBuilder.create(ProveedorBD::class.java)
    }
}