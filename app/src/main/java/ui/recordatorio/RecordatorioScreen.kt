package com.example.prueba.ui.recordatorio

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.prueba.model.Recordatorio
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatorioScreen(vm: RecordatorioViewModel) {
    val state by vm.ui.collectAsState()
    val focus = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(state.error) { }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Recordatorios", style = MaterialTheme.typography.headlineSmall)
        Text("Usuario: ${state.uid}")

        // Formulario de mensaje
        OutlinedTextField(
            value = state.mensaje,
            onValueChange = vm::onMensajeChange,
            label = { Text("Mensaje") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            minLines = 2
        )


        // Botones Fecha y Hora
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Fecha
            Button(onClick = {
                val calendar = Calendar.getInstance()
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val cal = Calendar.getInstance().apply {
                            set(year, month, dayOfMonth, 0, 0, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        vm.onAlarmDateChange(cal.timeInMillis)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }) {
                Text("Seleccionar fecha")
            }

            // Hora
            Button(onClick = {
                val calendar = Calendar.getInstance()
                TimePickerDialog(
                    context,
                    { _, hourOfDay, minute ->
                        val currentAlarm = state.alarmTimeTemp ?: System.currentTimeMillis()
                        val cal = Calendar.getInstance().apply {
                            timeInMillis = currentAlarm
                            set(Calendar.HOUR_OF_DAY, hourOfDay)
                            set(Calendar.MINUTE, minute)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        vm.onAlarmTimeChange(cal.timeInMillis)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            }) {
                Text("Seleccionar hora")
            }
        }


        // Botones Guardar/Nuevo
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    vm.guardar()
                    focus.clearFocus()
                },
                enabled = !state.loading
            ) {
                Text(if (state.editingId == null) "Guardar" else "Actualizar")
            }

        }


        if (state.error != null) {
            Text(state.error ?: "", color = MaterialTheme.colorScheme.error)
        }

        Divider()

        // Listado de recordatorios
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
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {

            // Mensaje principal
            Text(item.message, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))

            // Fecha de creación
            Text("Creado: ${item.createdAt}", style = MaterialTheme.typography.bodySmall)

            // Mostrar siempre la fecha y hora seleccionadas
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val fechaHoraTexto = if (item.alarmTime != null) {
                sdf.format(Date(item.alarmTime))
            } else {
                "Sin fecha/hora seleccionada"
            }

            Spacer(Modifier.height(4.dp))
            Text("Alarma: $fechaHoraTexto", style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(8.dp))

            // Botones de acción
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onEdit(item) }) {
                    Icon(Icons.Outlined.Edit, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Editar")
                }
                OutlinedButton(
                    onClick = { onDelete(item) },
                    colors = ButtonDefaults.outlinedButtonColors()
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Eliminar")
                }
            }
        }
    }
}


