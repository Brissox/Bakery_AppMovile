package com.example.prueba.ui.principal.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.ui.Alignment
import com.example.prueba.model.Producto

@Composable
fun UiProductosCard(
    producto: Producto,
    onAgregar: (Producto) -> Unit,
    esFavorito: Boolean,
    onFavoritoClick: () -> Unit
) {
    var agregado by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Box { // <-- Caja para superponer el botón de favorito sobre la imagen
                Image(
                    painter = painterResource(producto.imagenes),
                    contentDescription = producto.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = onFavoritoClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = if (esFavorito)
                            androidx.compose.material.icons.Icons.Filled.Favorite
                        else
                            androidx.compose.material.icons.Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (esFavorito) androidx.compose.ui.graphics.Color.Red
                        else androidx.compose.ui.graphics.Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = producto.titulo,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = producto.categoria,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Valor: ${producto.precio}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.weight(1f))

            // --- Animaciones del botón de agregar ---
            val interactionSource = remember { MutableInteractionSource() }
            val presionado by interactionSource.collectIsPressedAsState()

            val escala by animateFloatAsState(
                targetValue = if (presionado) 0.95f else 1f,
                animationSpec = tween(durationMillis = 100),
                label = "scaleAnim"
            )

            val colorFondo by animateColorAsState(
                targetValue = if (agregado)
                    MaterialTheme.colorScheme.secondary
                else
                    MaterialTheme.colorScheme.primary,
                animationSpec = tween(durationMillis = 300),
                label = "colorAnim"
            )

            Button(
                onClick = {
                    agregado = !agregado
                    onAgregar(producto)
                },
                interactionSource = interactionSource,
                colors = ButtonDefaults.buttonColors(containerColor = colorFondo),
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer { scaleX = escala; scaleY = escala }
                    .animateContentSize()
            ) {
                Text(
                    if (agregado) "Agregado" else "Agregar al carrito",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
