package com.example.prueba.ui

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.prueba.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrarseScreen(
    onBack: () -> Unit,
    onRegistered: () -> Unit,
    isPreview: Boolean = false
) {
    val context = LocalContext.current
    val auth = if (!isPreview) remember { FirebaseAuth.getInstance() } else null

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    fun validar(): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMsg = "Email inválido"; return false
        }
        if (password.length < 6) {
            errorMsg = "La clave debe tener al menos 6 caracteres"; return false
        }
        if (password != confirm) {
            errorMsg = "Las claves no coinciden"; return false
        }
        errorMsg = null; return true
    }

    fun signUp() {
        if (!validar()) return

        if (isPreview) {
            onRegistered()
            return
        }

        loading = true
        auth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener { task ->
                loading = false
                if (task.isSuccessful) {
                    Toast.makeText(context, "Cuenta creada. Inicia sesión.", Toast.LENGTH_SHORT).show()
                    onRegistered()
                } else {
                    errorMsg = task.exception?.localizedMessage ?: "No se pudo crear la cuenta"
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrarse") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.85f)
                    .offset(y = (-100).dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo), //carga el drawable logo
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .fillMaxWidth() //todo el ancho
                        .height(150.dp), //y 150dp de alto
                    contentScale = ContentScale.Fit //ajusta la imagen sin recortarla para que quepa
                )
                Text(
                    text = "Introduzca su cuenta",
                    style = MaterialTheme.typography.headlineSmall
                )
                OutlinedTextField(
                    value = email, onValueChange = { email = it },
                    label = { Text("Correo electrónico") }, singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text("Contraseña") }, singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirm, onValueChange = { confirm = it },
                    label = { Text("Confirmar contraseña") }, singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMsg != null) {
                    Text(errorMsg!!, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = { signUp() },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (loading) "Creando cuenta..." else "Crear cuenta")
                }
            }

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).size(48.dp)
                )
            }
        }
    }
}
@Preview
@Composable
fun RegistrarseScreenPreview() {
    RegistrarseScreen(
        onBack = {},
        onRegistered = {},
        isPreview = true
    )
}