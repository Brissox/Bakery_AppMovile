package ui.feriados

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import model.Feriado

@Composable
fun FeriadoScreen(viewModel: FeriadoViewModel) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.loading -> CargandoBox()
        state.error != null -> ErrorBox(mensaje = state.error ?: "Error") {
            viewModel.cargarFeriados()
        }
        else -> ListaFeriados(state.items)
    }
}

@Composable
private fun CargandoBox() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorBox(mensaje: String, onReintentar: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Ocurrió un problema:\n$mensaje", color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onReintentar) { Text("Reintentar") }
        }
    }
}

@Composable
private fun ListaFeriados(items: List<Feriado>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp), // Aumenté un poco el padding general
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // TÍTULO DE LA LISTA
        item {
            Text(
                text = "Lista de feriados en Chile",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        // LISTA DE TARJETAS
        items(items) { f -> TarjetaFeriado(f) }
    }
}

@Composable
private fun TarjetaFeriado(f: Feriado) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = f.title, 
                        style = MaterialTheme.typography.titleMedium, 
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = f.date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                
                if (f.inalienable) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text("Irrenunciable", style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.height(28.dp)
                    )
                }
            }

            Text(
                text = f.type,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}