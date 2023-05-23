package com.example.easyref.ViewModel

import com.example.easyref.DataBase.ProveedorBD
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitController {

    fun crearRetrofit(): ProveedorBD{
        val urlAndroidStudio="http://10.0.2.2:8080/ApiRestEasyRef_Casa/"
        val urlMovil="http://localhost:8080/ApiRestEasyRef_Casa/"
        val urlAzure="http://apiref.eastus.cloudapp.azure.com:8080/ApiRestEasyRef/"
        val retrofitBuilder = Retrofit.Builder()
            //.baseUrl(urlAzure)
            .baseUrl(urlAndroidStudio)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofitBuilder.create(ProveedorBD::class.java)
    }
}