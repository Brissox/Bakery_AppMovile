package com.example.prueba.ui.recordatorio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.prueba.model.Recordatorio
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioScreen(vm: RecordatorioViewModel) {
    val state by vm.ui.collectAsState()
    val focus = LocalFocusManager.current
    
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(state.error) { }
    
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            vm.onFechaChange(millis)
                        }
                        showDatePicker = false
                    }
                ) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Recordatorios", style = MaterialTheme.typography.headlineSmall)
        Text("Usuario: ${state.uid}")

        OutlinedTextField(
            value = state.mensaje,
            onValueChange = vm::onMensajeChange,
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false, minLines = 2
        )
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar Fecha")
                Spacer(Modifier.width(8.dp))
                Text(text = state.fechaCreacion.ifEmpty { "Hoy" })
            }
            Text("Fecha del recordatorio", style = MaterialTheme.typography.bodySmall)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { vm.guardar(); focus.clearFocus() },
                enabled = !state.loading
            ) { Text(if (state.editingId == null) "Guardar" else "Actualizar") }
            OutlinedButton(onClick = { vm.onNuevo(); focus.clearFocus() }, enabled = !state.loading) {
                Text("Nuevo")
            }
        }

        if (state.error != null) {
            Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
        }

        Divider()

        if (state.items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay recordatorios")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(state.items, key = { it.id }) { item ->
                    ReminderItem(
                        item = item,
                        onEdit = vm::onEditar,
                        onDelete = vm::eliminar
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderItem(
    item: Recordatorio,
    onEdit: (Recordatorio) -> Unit,
    onDelete: (Recordatorio) -> Unit
) {
    // Verificamos si la fecha es futura
    val esFuturo = isFutureDate(item.createdAt)

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        // Usamos Box para poder posicionar el icono en la esquina superior derecha
        Box(modifier = Modifier.padding(12.dp)) {
            
            // Ícono de campana en la esquina (solo si es futuro)
            if (esFuturo) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Recordatorio futuro",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }

            Column {
                // Dejamos espacio a la derecha del texto para que no choque con el icono si es muy largo
                val paddingEnd = if (esFuturo) 24.dp else 0.dp
                
                Text(
                    text = item.message,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = paddingEnd)
                )
                Spacer(Modifier.height(4.dp))
                Text("Fecha: ${item.createdAt}", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { onEdit(item) }) {
                        Icon(Icons.Outlined.Edit, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Editar")
                    }
                    OutlinedButton(onClick = { onDelete(item) }, colors = ButtonDefaults.outlinedButtonColors()) {
                        Icon(Icons.Outlined.Delete, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

// Función auxiliar para comparar fechas
private fun isFutureDate(dateString: String): Boolean {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = sdf.parse(dateString) ?: return false
        
        // Obtenemos la fecha de hoy sin horas (00:00:00) para comparar solo días
        val hoy = Date()
        val hoyString = sdf.format(hoy)
        val hoyDate = sdf.parse(hoyString) ?: Date()

        date.after(hoyDate)
    } catch (e: Exception) {
        false
    }
}