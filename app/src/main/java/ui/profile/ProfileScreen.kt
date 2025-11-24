package com.example.prueba.ui.profile

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(vm: ProfileViewModel) {
    val ui by vm.ui.collectAsState()
    val context = LocalContext.current

    var hasCamera by remember { mutableStateOf(false) }
    var hasRead by remember { mutableStateOf(false) }

    val cameraPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCamera = granted }

    val readPerm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES else Manifest.permission.READ_EXTERNAL_STORAGE

    val readPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasRead = granted }

    // Cámara: tomar foto y subir
    var pendingUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { ok ->
        if (ok && pendingUri != null) {
            vm.setLastSavedPhoto(pendingUri)
            vm.subirImagenDesdeUri(context, pendingUri!!)
            Toast.makeText(context, "Foto tomada y subida", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
        }
        pendingUri = null
    }

    // Galería: elegir imagen y subir
    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) vm.subirImagenDesdeUri(context, uri)
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Perfil") }) }) { inner ->
        val scroll = rememberScrollState()

        Column(
            Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(scroll)          // permite desplazar la pantalla
                .imePadding()                    // evita que el teclado tape contenido
                .navigationBarsPadding()         // respeta las barras del sistema
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Usuario: ${ui.usuario ?: "No disponible"}")
            Text("Correo: ${ui.email ?: "No disponible"}")
            Text("UID: ${ui.uid ?: "No disponible"}")
            Text("RUT: ${ui.run ?: "No disponible"}")
            Text("TELEFONO: ${ui.telefono ?: "No disponible"}")



            // Imagen desde backend (ByteArray)
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                val bytes = ui.imageBytes
                if (bytes != null && bytes.isNotEmpty()) {
                    val painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(bytes)     // Coil acepta ByteArray directo
                            .size(512)       // objetivo razonable para evitar consumo excesivo
                            .crossfade(true)
                            .build()
                    )
                    Image(
                        painter = painter,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.size(180.dp) // tamaño fijo que no empuja el layout
                    )
                } else {
                    Text("Sin imagen")
                }
            }

            // Nombre (editable)
            OutlinedTextField(
                value = ui.editingNombre,
                onValueChange = vm::onNombreEdit,
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = VisualTransformation.None
            )
            Button(
                onClick = vm::guardarNombre,
                enabled = !ui.loading && ui.run != "",
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar nombre") }

            // Botones para imagen
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = {
                    if (!hasCamera) cameraPermLauncher.launch(Manifest.permission.CAMERA)
                    if (!hasRead) readPermLauncher.launch(readPerm)

                    val dest = vm.createDestinationUriForCurrentUser(context)
                    if (dest == null) {
                        Toast.makeText(context, "No hay UID para crear destino", Toast.LENGTH_SHORT).show()
                        return@OutlinedButton
                    }
                    pendingUri = dest
                    takePictureLauncher.launch(dest)
                }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Outlined.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Tomar foto (cámara)")
                }

                OutlinedButton(onClick = {
                    if (!hasRead) readPermLauncher.launch(readPerm)
                    pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }, modifier = Modifier.weight(1f)) {
                    Text("Elegir de galería")
                }
            }

            if (ui.loading) {
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }

            ui.msg?.let {
                Text(it, color = MaterialTheme.colorScheme.primary)
                LaunchedEffect(it) { vm.clearMsg() }
            }
            ui.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(Modifier.height(24.dp)) // colchón para que se vea el final al hacer scroll
        }
    }
} /*
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
                    ProfileItem("Nombre Completo", "${perfil.nombre} ${perfil.apellidoPaterno} ${perfil.apellidoMaterno}")
                    ProfileItem("RUN", "${perfil.run}-${perfil.dv}")
                    ProfileItem("Email", perfil.correo)
                    ProfileItem("Fecha Nacimiento", perfil.fechaNacimiento)
                    ProfileItem("Ubicación", "${perfil.ciudad}, ${perfil.pais}")
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
}*/