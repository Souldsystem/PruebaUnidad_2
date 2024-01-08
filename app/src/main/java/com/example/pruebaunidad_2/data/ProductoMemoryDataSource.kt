package com.example.pruebaunidad_2.data

import android.util.Log
import com.example.pruebaunidad_2.data.modelo.Producto

class ProductoMemoryDataSource {
    private val _productos = mutableListOf<Producto>()

    init {
        //inicializa productos
        _productos.addAll(ProductoDePrueba())
    }

    fun obtenerTodo():List<Producto> {
        return _productos
    }

    fun insertar(vararg Producto: Producto) {
        _productos.addAll( Producto.asList() )
    }

    fun eliminar(Producto: Producto) {
        _productos.remove(Producto)
        Log.v("DataSource", _productos.toString())
    }

    private fun ProductoDePrueba():List<Producto> = listOf(
        /*
        Tarea(UUID.randomUUID().toString(), "Lavar la ropa")
        , Tarea(UUID.randomUUID().toString(), "Cocinar")
        , Tarea(UUID.randomUUID().toString(), "Supermercado")
        , Tarea(System.currentTimeMillis(), "Veterinario Rocky")
        , Tarea(System.currentTimeMillis(), "Terminar Taller 1")
        , Tarea(System.currentTimeMillis(), "Matrícula")
        , Tarea(System.currentTimeMillis(), "Comprar pasajes avión")
        , Tarea(System.currentTimeMillis(), "Pagar cuentas")
        , Tarea(System.currentTimeMillis(), "Pagar Tarjetas de Crédito")
        , Tarea(System.currentTimeMillis(), "Reclamo comisiones")
        , Tarea(System.currentTimeMillis(), "Comprar regalos")
        , Tarea(System.currentTimeMillis(), "Veterinario Mila")
        , Tarea(System.currentTimeMillis(), "Informes de Cierre")
        , Tarea(System.currentTimeMillis(), "Preparar declaración de impuestos")
        , Tarea(System.currentTimeMillis(), "Pago a proveedores")
        , Tarea(System.currentTimeMillis(), "Renovación de arriendo")
        , Tarea(System.currentTimeMillis(), "Revisión Técnica")
         */
    )
    fun actualizar(productoActualizado: Producto) {
        val index = _productos.indexOfFirst { it.id == productoActualizado.id }
        if (index != -1) {
            _productos[index] = productoActualizado
        }
    }



}