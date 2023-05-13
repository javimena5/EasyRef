package com.example.easyref.ViewModel

import com.example.easyref.Modelo.ArbitroEntity
import com.example.easyref.Modelo.EquipoEntity
import com.example.easyref.Modelo.JugadorEntity


object InicializarData {
    fun listaArbitros():MutableList<ArbitroEntity>
    {
        var lista= mutableListOf<ArbitroEntity>()
        lista.add(ArbitroEntity(0,"Jesus","Gil Manzano","https://upload.wikimedia.org/wikipedia/commons/e/ea/Gil_Manzano.jpg"))
        lista.add(ArbitroEntity(0,"Antonio","Mateu Lahoz","https://upload.wikimedia.org/wikipedia/commons/1/1d/20191002_Fu%C3%9Fball%2C_M%C3%A4nner%2C_UEFA_Champions_League%2C_RB_Leipzig_-_Olympique_Lyonnais_by_Stepro_StP_0043-2_%28cropped%29.jpg"))

        return lista
    }

    fun listaEquipos():MutableList<EquipoEntity>
    {
        var lista= mutableListOf<EquipoEntity>()
        lista.add(EquipoEntity(0,"Real Madrid C.F.","https://as01.epimg.net/img/comunes/fotos/fichas/equipos/large/1.png"))
        lista.add(EquipoEntity(0,"Barcelona F.C.","https://as01.epimg.net/img/comunes/fotos/fichas/equipos/large/3.png"))
        lista.add(EquipoEntity(0,"Atlético de Madrid","https://as01.epimg.net/img/comunes/fotos/fichas/equipos/large/42.png"))

        return lista
    }

    fun listaJugadores():MutableList<JugadorEntity>
    {
        var lista= mutableListOf<JugadorEntity>()
        lista.add(JugadorEntity(0,"Karim","Benzema",9,"",1,0,0))
        lista.add(JugadorEntity(0,"Vinicius","Junior",20,"",1,0,0))
        lista.add(JugadorEntity(0,"Rodrygo","Goes",21,"",1,0,0))
        lista.add(JugadorEntity(0,"Luka","Modric",10,"",1,0,0))
        lista.add(JugadorEntity(0,"Fede","Valverde",15,"",1,0,0))
        lista.add(JugadorEntity(0,"Ferland","Mendy",23,"",1,0,0))
        lista.add(JugadorEntity(0,"Thibaut","Courtois",1,"",1,0,0))


        lista.add(JugadorEntity(0,"Ter","Stegen",1,"",2,0,0))
        lista.add(JugadorEntity(0,"Robert","Lewandowski",9,"",2,0,0))
        lista.add(JugadorEntity(0,"Andreas","Christensen",15,"",2,0,0))
        lista.add(JugadorEntity(0,"Jordi","Alba",18,"",2,0,0))
        lista.add(JugadorEntity(0,"Jules","Kounde",23,"",2,0,0))
        lista.add(JugadorEntity(0,"Sergio","Busquets",5,"",2,0,0))
        lista.add(JugadorEntity(0,"Ansu","Fati",10,"",2,0,0))


        lista.add(JugadorEntity(0,"Álvaro","Morata",8,"",3,0,0))
        lista.add(JugadorEntity(0,"Antoine","Griezmann",19,"",3,0,0))
        return lista
    }
}