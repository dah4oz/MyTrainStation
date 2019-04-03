package com.example.mytrainstation.model

class Station {

    var id: Int = 0
    var name: String? = null
    var code: String? = null
    var latitude: String? = null
    var longitude: String? = null

    override fun toString(): String {
        return "Station(id=$id, name=$name, code=$code, latitude=$latitude, longitude=$longitude)"
    }


}