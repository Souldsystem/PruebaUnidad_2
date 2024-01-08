package com.example.pruebaunidad_2.data.modelo

import java.io.Serializable

data class Producto(
    val id:String,
    val descripcion:String,
    var seleccionado: Boolean
) : Serializable {


}
