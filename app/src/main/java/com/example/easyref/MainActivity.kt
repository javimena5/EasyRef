package com.example.easyref

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.easyref.ViewModel.EasyRefController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EasyRefController.iniciarDB(application)
    }
}