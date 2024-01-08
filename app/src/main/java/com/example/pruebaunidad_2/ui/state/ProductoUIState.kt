package com.example.pruebaunidad_2.ui.state

import com.example.pruebaunidad_2.data.modelo.Producto

data class ProductoUIState (
    val mensaje:String = "",
    val productos:List<Producto> = listOf<Producto>(),
    var seleccionado: Boolean = false
)