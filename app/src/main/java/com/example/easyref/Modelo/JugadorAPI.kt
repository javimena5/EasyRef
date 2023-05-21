package com.example.easyref.Modelo

class JugadorAPI(
    val idJugador : Int,
    val nombreJugador : String,
    val apellidosJugador : String,
    val dorsal : Int,
    val fotoJugador : String,
    val equipo : EquipoEntity,
    var esTitular : Int,
    var expulsado : Int
)