package com.example.pruebaunidad_2.ui.theme.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pruebaunidad_2.data.ProductoRepository
import com.example.pruebaunidad_2.data.modelo.Producto
import com.example.pruebaunidad_2.ui.state.ProductoUIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID

class ProductoViewModel(
    private val productoRepository: ProductoRepository = ProductoRepository()
) : ViewModel() {

    companion object {
        const val FILE_NAME = "productos.data"
    }

    private var job: Job? = null
    private val _uiState = MutableStateFlow(ProductoUIState())
    val uiState:StateFlow<ProductoUIState> = _uiState.asStateFlow()

    init {
        obtenerProductos()
    }

    fun obtenerProductosGuardadosEnDisco(fileInputStream: FileInputStream) {
        productoRepository.getProductoEnDisco(fileInputStream)
    }

    fun guardarProductosEnDisco(fileOutputStream: FileOutputStream) {
        productoRepository.guardarProductoEnDisco(fileOutputStream)
    }

    private fun obtenerProductos() {
        job?.cancel()
        job = viewModelScope.launch {
            val productoStream = productoRepository.getProductoStream()
            productoStream.collect { ProductoActualizado ->
                Log.v("ProductosViewModel", "obtenerProductos() update{}")
                _uiState.update { currentState ->
                    currentState.copy(
                        productos = ProductoActualizado
                    )
                }
            }
        }
    }
/*
    fun agregarProducto(Producto:String) {
        job = viewModelScope.launch {
            val t = Producto(UUID.randomUUID().toString(), Producto)
            productoRepository.insertar(t)
            _uiState.update {
                it.copy(mensaje = "Tarea agregada: ${t.descripcion}")
            }
            obtenerProductos()
        }
    }
*/
 fun actualizarEstadoProducto(producto: Producto, isChecked: Boolean) {
    viewModelScope.launch {
        val productoActualizado = producto.copy(seleccionado = isChecked)
        productoRepository.actualizarProducto(productoActualizado)
        obtenerProductos()
    }
}
    fun agregarProducto(descripcion: String) {
        job = viewModelScope.launch {
            try {
                val nuevoProducto = Producto(
                    id = UUID.randomUUID().toString(),
                    descripcion = descripcion,
                    seleccionado = false // Establecer el checkbox a false por defecto
                )

                productoRepository.insertar(nuevoProducto)
                _uiState.update {
                    it.copy(mensaje = "Producto agregado: ${nuevoProducto.descripcion}")
                }
                obtenerProductos()
            } catch (e: Exception) {
                // Manejo de excepciones aquí
                Log.e("ProductoViewModel", "Error al crear FileOutputStream: ${e.message}")
                e.printStackTrace()
                // Aquí podrías realizar acciones de recuperación o notificar al usuario sobre el error.
            }
        }
    }

    fun eliminarProducto(Producto:Producto) {
        job = viewModelScope.launch {
            productoRepository.eliminar(Producto)
            _uiState.update {
                it.copy(mensaje = "Producto eliminado: ${Producto.descripcion}")
            }
            obtenerProductos()
        }
    }
}