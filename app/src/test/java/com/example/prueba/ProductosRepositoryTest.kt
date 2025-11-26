package com.example.prueba

import Data.Remote.ApiBackendService
import Data.Remote.dto.ProductoResp
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import retrofit2.Response

class ProductoRepositoryTest : StringSpec({

    "listar productos debe devolver lista simulada correctamente" {

        // Mock del servicio API
        val mockApi = mockk<ApiBackendService>()

        // Lista simulada de productos
        val productosMock = listOf(
            ProductoResp(
                NOMBRE = "Producto A",
                DESCRIPCION = "Descripción A",
                CATEGORIA = "Categoria 1",
                PRECIO = 100,
                SKU = "SKU001",
                ESTADO = "Activo",
                STOCK = 10,
                ENLACEIMG = "https://imagen.com/a.jpg"
            ),
            ProductoResp(
                NOMBRE = "Producto B",
                DESCRIPCION = "Descripción B",
                CATEGORIA = "Categoria 2",
                PRECIO = 200,
                SKU = "SKU002",
                ESTADO = "Activo",
                STOCK = 5,
                ENLACEIMG = "https://imagen.com/b.jpg"
            )
        )

        // Simula la llamada suspend a la API
        coEvery { mockApi.listarProductos() } returns Response.success(productosMock)

        // Repositorio simple que usa mockApi
        class ProductoRepository(private val api: ApiBackendService) {
            suspend fun listarProductos(): List<ProductoResp> {
                val resp = api.listarProductos()
                return if (resp.isSuccessful) resp.body() ?: emptyList() else emptyList()
            }
        }

        val repo = ProductoRepository(mockApi)

        // Ejecutamos la función
        val lista = kotlinx.coroutines.runBlocking { repo.listarProductos() }

        // Validamos resultados
        lista.size shouldBe 2
        lista[0].NOMBRE shouldBe "Producto A"
        lista[1].PRECIO shouldBe 200
        lista[0].STOCK shouldBe 10
    }
})
