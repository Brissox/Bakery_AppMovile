package com.example.prueba

import Data.model.Productos
import com.example.prueba.ui.carrito.CartItem
import com.example.prueba.ui.carrito.CartViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class CartViewModelTest : StringSpec({

    "agregarProducto debe añadir producto a la lista" {
        // Mock del Application para el CartViewModel
        val application = mockk<android.app.Application>(relaxed = true)
        val vm = CartViewModel(application)

        // Creamos producto de prueba
        val producto1 = Productos(
            id_producto = 1,
            nombre = "Producto 1",
            descripcion = "Desc 1",
            precio = 100,
            stock = 10,
            categoria = "tortas",
            sku = "12312312",
            enlaceimg = "imagen1.jpg",
            estado = "A"

        )

        // Agregamos producto al carrito
        vm.agregarProducto(producto1)

        // Verificamos que la lista tenga 1 elemento y sea el correcto
        vm.cartItems.size shouldBe 1
        vm.cartItems[0].productos shouldBe producto1
        vm.cartItems[0].cantidad shouldBe 1
    }

    "increaseQuantity debe aumentar cantidad del producto existente" {
        val application = mockk<android.app.Application>(relaxed = true)
        val vm = CartViewModel(application)

        val producto = Productos(
            id_producto = 1,
            nombre = "Producto 1",
            descripcion = "Desc 1",
            precio = 100,
            stock = 10,
            categoria = "tortas",
            sku = "12312312",
            enlaceimg = "imagen1.jpg",
            estado = "A"
        )

        vm.agregarProducto(producto)
        vm.increaseQuantity(producto)

        vm.cartItems[0].cantidad shouldBe 2
    }

    "clearCart debe vaciar la lista" {
        val application = mockk<android.app.Application>(relaxed = true)
        val vm = CartViewModel(application)

        val producto = Productos(
            id_producto = 1,
            nombre = "Producto 1",
            descripcion = "Desc 1",
            precio = 100,
            stock = 10,
            categoria = "tortas",
            sku = "12312312",
            enlaceimg = "imagen1.jpg",
            estado = "A"



        vm.agregarProducto(producto)
        vm.clearCart()

        vm.cartItems.size shouldBe 0
    }
})
