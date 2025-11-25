package com.example.prueba.ui.register

import Data.media.ImageCompressor
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.register.RegistrarseViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarseScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit,
    vm: RegistrarseViewModel = viewModel()
) {
    val ui by vm.ui.collectAsState()
    val ctx = LocalContext.current

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val file = runCatching {
                ImageCompressor.compressToTempFile(ctx, uri)
            }.getOrElse {
                Toast.makeText(ctx, "Error al procesar imagen: ${it.message}", Toast.LENGTH_SHORT).show()
                null
            }
            vm.onImagenFile(file)
        }
    }

    var pendingUri by remember { mutableStateOf<Uri?>(null) }
    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { ok ->
        val u = pendingUri
        pendingUri = null
        if (!ok || u == null) {
            Toast.makeText(ctx, "No se pudo tomar la foto", Toast.LENGTH_SHORT).show()
            return@rememberLauncherForActivityResult
        }
        val file = runCatching {
            ImageCompressor.compressToTempFile(ctx, u)
        }.getOrElse {
            Toast.makeText(ctx, "Error al procesar imagen: ${it.message}", Toast.LENGTH_SHORT).show()
            null
        }
        vm.onImagenFile(file)
        Toast.makeText(ctx, "Foto agregada", Toast.LENGTH_SHORT).show()
    }

    fun createCameraUri(context: Context): Uri? {
        val name = "reg_${System.currentTimeMillis()}.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 0)
            }
        }
        return context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
        )
    }

    LaunchedEffect(ui.ok) { if (ui.ok) onRegistered() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registro de Usuario") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Volver") } }
            )
        }
    ) { inner ->
        val scroll = rememberScrollState()

        Column(
            Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(scroll)
                .imePadding()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = ui.run, 
                    onValueChange = vm::onRun,
                    label = { Text("RUN (sin puntos)") },
                    modifier = Modifier.weight(0.7f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                OutlinedTextField(
                    value = ui.dv, 
                    onValueChange = vm::onDv,
                    label = { Text("DV") },
                    modifier = Modifier.weight(0.3f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = ui.usuario, 
                onValueChange = vm::onUsuario,
                label = { Text("Nombre de Usuario") }, 
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )


            OutlinedTextField(
                value = ui.nombre,
                onValueChange = vm::onNombre,
                label = { Text("Nombre ") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = ui.fechaNacimiento, 
                onValueChange = vm::onFechaNacimiento,
                label = { Text("Fecha Nacimiento (DD-MM-YYYY)") },
                placeholder = { Text("Ej: 10-12-1993") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = ui.email, 
                onValueChange = vm::onEmail,
                label = { Text("Email") }, 
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true
            )

            OutlinedTextField(
                value = ui.password, 
                onValueChange = vm::onPass,
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text("Foto de perfil (Opcional)")
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = {
                        val dest = createCameraUri(ctx)
                        if (dest != null) {
                            pendingUri = dest
                            takePictureLauncher.launch(dest)
                        } else {
                            Toast.makeText(ctx, "No se pudo iniciar cámara", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Cámara") }

                OutlinedButton(
                    onClick = {
                        pickImage.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (ui.imagenFile == null) "Galería" else "Cambiar")
                }
            }

            if (ui.imagenFile != null) {
                Text(
                    text = "Imagen: ${ui.imagenFile!!.name}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = vm::registrar,
                enabled = !ui.loading,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (ui.loading) "Registrando..." else "Crear Cuenta") }

            ui.msg?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}