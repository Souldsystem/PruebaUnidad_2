package com.example.pruebaunidad_2.data

import com.example.pruebaunidad_2.data.modelo.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.FileInputStream
import java.io.FileOutputStream

class ProductoRepository(
    private val tareaMemoryDataSource: ProductoMemoryDataSource = ProductoMemoryDataSource(),
    private val tareaDiskDataSource: ProductoDiskDataSource = ProductoDiskDataSource()
) {
    private val _tareasStream = MutableStateFlow(listOf<Producto>())

    fun getProductoEnDisco(fileInputStream:FileInputStream) {
        val tareas = tareaDiskDataSource.obtener(fileInputStream)
        insertar(*tareas.toTypedArray())
    }

    fun guardarProductoEnDisco(fileOutputStream:FileOutputStream) {
        tareaDiskDataSource.guardar(fileOutputStream, tareaMemoryDataSource.obtenerTodo())
    }

    fun getProductoStream():StateFlow<List<Producto>> {
        _tareasStream.update {
            ArrayList(tareaMemoryDataSource.obtenerTodo())
        }
        return _tareasStream.asStateFlow()
    }

    fun insertar(vararg Producto:Producto) {
        tareaMemoryDataSource.insertar(*Producto) // spread operator (*)
        getProductoStream()
    }

    fun eliminar(Producto:Producto) {
        tareaMemoryDataSource.eliminar(Producto)
        getProductoStream()
    }

  suspend fun actualizarProducto(producto: Producto) {
        tareaMemoryDataSource.actualizar(producto)
        _tareasStream.value = ArrayList(tareaMemoryDataSource.obtenerTodo())
    }

}