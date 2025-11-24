package com.example.prueba.ui.carrito

import Data.model.Productos
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel

data class CartItem(
    val productos: Productos,
    var cantidad: Int = 1
)

class CartViewModel : ViewModel() {

    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: SnapshotStateList<CartItem> = _cartItems

    fun agregarProducto(producto: Productos) {
        val itemExistente = _cartItems.find { it.productos.id_producto == producto.id_producto }
        if (itemExistente != null) {
            itemExistente.cantidad++
        } else {
            _cartItems.add(CartItem(producto))
        }
    }

    fun eliminarProducto(productos: Productos) {
        _cartItems.removeAll { it.productos.id_producto == productos.id_producto }
    }

    fun increaseQuantity(producto: Productos) {
        val index = _cartItems.indexOfFirst { it.productos.id_producto == producto.id_producto }
        if (index != -1) {
            val item = _cartItems[index]
            _cartItems[index] = item.copy(cantidad = item.cantidad + 1)
        }
    }

    fun decreaseQuantity(producto: Productos) {
        val index = _cartItems.indexOfFirst { it.productos.id_producto == producto.id_producto }
        if (index != -1) {
            val item = _cartItems[index]
            if (item.cantidad > 1) {
                _cartItems[index] = item.copy(cantidad = item.cantidad - 1)
            } else {
                _cartItems.removeAt(index)
            }
        }
    }

    fun clearCart() {
        _cartItems.clear()
    }

    fun calcularTotal(): Long {
        // El precio ya viene como Long desde el backend, así que solo multiplicamos
        return _cartItems.sumOf { it.productos.precio * it.cantidad }
    }
}