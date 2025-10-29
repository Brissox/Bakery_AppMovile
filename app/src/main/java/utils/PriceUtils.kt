package utils


// Convierte string de precio ("45.000") a Int
fun String.toIntPrice(): Int {
    return this.replace(Regex("[^\\d]"), "").toIntOrNull() ?: 0
}

// Formatea un Int como string con puntos de miles
fun formatPrice(price: Int): String {
    return price.toString().replace(Regex("(\\d)(?=(\\d{3})+(?!\\d))"), "$1.")
}
