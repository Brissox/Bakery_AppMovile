package utils



fun String.toIntPrice(): Int {
    return this.replace(Regex("[^\\d]"), "").toIntOrNull() ?: 0
}


fun formatPrice(price: Int): String {
    return price.toString().replace(Regex("(\\d)(?=(\\d{3})+(?!\\d))"), "$1.")
}
