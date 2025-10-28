package ui.home.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.prueba.R

@Composable
fun AnimatedLogo(
    modifier: Modifier = Modifier,
    logoRes: Int = R.drawable.logo,
    contentScale: ContentScale = ContentScale.Fit
) {
    // Animación de entrada (escala + opacidad)
    var appeared by remember { mutableStateOf(false) }

    val enterScale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "enterScale"
    )

    val enterAlpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "enterAlpha"
    )

    // Animaciones infinitas (pulso + balanceo suave)
    val infinite = rememberInfiniteTransition(label = "logoInfinite")

    val pulse by infinite.animateFloat(
        initialValue = 0.97f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val tilt by infinite.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tilt"
    )

    // Efecto de presión (feedback táctil)
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val pressScale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = tween(100, easing = LinearEasing),
        label = "pressScale"
    )

    // Disparar la animación inicial
    LaunchedEffect(Unit) { appeared = true }

    Image(
        painter = painterResource(id = logoRes),
        contentDescription = "Logo App",
        contentScale = contentScale,
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .graphicsLayer {
                val finalScale = enterScale * pulse * pressScale
                scaleX = finalScale
                scaleY = finalScale
                rotationZ = tilt
                alpha = enterAlpha
            }
    )
}
