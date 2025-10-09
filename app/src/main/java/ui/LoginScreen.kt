package com.example.prueba.ui

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prueba.R
import com.google.firebase.auth.FirebaseAuth




@OptIn(ExperimentalMaterial3Api::class) //@OptIn(ExperimentalMaterial3Api::class): avisas que usas componentes experimentales de Material3 (TopAppBar).
@Composable //@Composable: indica que esta función dibuja UI en Compose.
fun LoginScreen(
    onBack: () -> Unit, //onBack: callback que se ejecuta al presionar “Atrás”.
    onLoginSuccess: () -> Unit, // callback que se ejecuta si el login es exitoso.
    //Un callback es simplemente una función que pasas como parámetro a otra función, para que esa función la ejecute en algún momento.
    isPreview: Boolean = false
) {
    val context = LocalContext.current // accede al contexto de Android (necesario para mostrar un Toast).
    //crea o reutiliza una instancia de Firebase Auth para manejar login. remember { ... } asegura que la instancia no se recree en cada recomposición.
    val auth = remember {
        if (isPreview) null else FirebaseAuth.getInstance()
    }
    //Estados de la pantalla
    var email by rememberSaveable { mutableStateOf("") } //almacenan lo que escribe el usuario. rememberSaveable guarda estado incluso si la actividad rota (ej: rotar pantalla).
    var password by rememberSaveable { mutableStateOf("") } // almacenan lo que escribe el usuario. rememberSaveable guarda estado incluso si la actividad rota (ej: rotar pantalla).
    var loading by remember { mutableStateOf(false) } //indica si se está ejecutando un login.
    var errorMsg by remember { mutableStateOf<String?>(null) }  //mensaje de error (si hay).

    //Función de validación
    fun validar(): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMsg = "Email inválido"
            return false
        }
        if (password.length < 6) {
            errorMsg = "La clave debe tener al menos 6 caracteres"
            return false
        }
        errorMsg = null
        return true
    }

    //Función de login
    fun signIn() {
        if (!validar()) return
        loading = true
        auth?.signInWithEmailAndPassword(email, password) // Llama a Firebase: signInWithEmailAndPassword.
            ?.addOnCompleteListener { task ->
                loading = false
                if (task.isSuccessful) {
                    Toast.makeText(context, "Ingreso exitoso", Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                } else {
                    errorMsg = task.exception?.localizedMessage ?: "Error al iniciar sesión"
                }
            }
    }


    //ui
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Login") },
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
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMsg != null) {
                    Text(errorMsg!!, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = { signIn() },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (loading) "Ingresando..." else "Ingresar")
                }
            }

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}
//ui
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onBack = {},
        onLoginSuccess = {},
        isPreview = true // Importante para que el preview funcione
    )
}