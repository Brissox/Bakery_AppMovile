package com.example.prueba.ui.profile

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(vm: ProfileViewModel) {
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current

    // Permisos de cámara
    var hasCamera by remember { mutableStateOf(false) }
    val cameraPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCamera = granted }

    var pendingUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok && pendingUri != null) {
            vm.setLastSavedPhoto(pendingUri)
            Toast.makeText(context, "Foto guardada", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Mi Perfil") }) }) { inner ->
        if (ui.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                Modifier
                    .padding(inner)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Permite scroll si la info es larga
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // FOTO DE PERFIL
                // Prioridad: 1. Foto recién tomada, 2. Foto del backend, 3. Placeholder
                val fotoMostrar = ui.lastSavedPhoto ?: ui.perfilBackend?.imagen
                
                Card(
                    modifier = Modifier.size(150.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    AsyncImage(
                        model = fotoMostrar,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = rememberAsyncImagePainter(com.example.prueba.R.drawable.ic_launcher_foreground),
                        placeholder = rememberAsyncImagePainter(com.example.prueba.R.drawable.ic_launcher_foreground)
                    )
                }

                OutlinedButton(onClick = {
                    if (!hasCamera) cameraPermLauncher.launch(Manifest.permission.CAMERA)
                    
                    val dest = vm.createDestinationUriForCurrentUser(context)
                    if (dest != null) {
                        pendingUri = dest
                        takePictureLauncher.launch(dest)
                    }
                }) {
                    Icon(Icons.Outlined.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cambiar Foto")
                }

                Divider()

                // DATOS DEL PERFIL (Desde el backend)
                val perfil = ui.perfilBackend
                
                if (perfil != null) {
                    ProfileItem("Usuario", perfil.usuario)
                    ProfileItem("Nombre Completo", "${perfil.nombre} ${perfil.apellidoPaterno}")
                    ProfileItem("RUN", "${perfil.run}-${perfil.dv}")
                    ProfileItem("Email", perfil.mail)
                    ProfileItem("Fecha Nacimiento", perfil.fechaNacimiento)
                    ProfileItem("Ubicación", "${perfil.ciudad}, ${perfil.pais}")
                    ProfileItem("Dirección", perfil.direccion)
                    ProfileItem("Teléfono", perfil.telefono.toString())
                } else {
                    Text("No se pudieron cargar los datos del perfil.", color = MaterialTheme.colorScheme.error)
                }

                ui.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(value, style = MaterialTheme.typography.bodyLarge)
        Divider(modifier = Modifier.padding(top = 8.dp), color = MaterialTheme.colorScheme.surfaceVariant)
    }
}