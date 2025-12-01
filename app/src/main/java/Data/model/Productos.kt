package Data.model


data class ProductoResponse(
    val _embedded: EmbeddedData
)
data class EmbeddedData(
    val productoList: List<Productos>
)

data class Productos(
    val id_producto: Long,
    val nombre: String,
    val descripcion: String?,
    val categoria: String?,
    val precio: Long,
    val sku: String,
    val estado: String,
    val stock: Int,
    val enlaceimg: String
)
