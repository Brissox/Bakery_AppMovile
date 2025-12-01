package com.example.prueba.ui.recover


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.recover.RecuperarViewModel
import com.example.prueba.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarPasswordScreen(
    onBack: () -> Unit,
    onSent: () -> Unit,
    vm: RecuperarViewModel = viewModel()
) {


    val state by vm.ui.collectAsState()

    LaunchedEffect(state.sent) {
        if (state.sent) onSent()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.message) {
        state.message?.let {
            snackbarHostState.showSnackbar(it)
            vm.messageConsumed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar contraseña") },
                navigationIcon = {
                    IconButton (onClick = onBack) {
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
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo App",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "Introduzca su correo",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = state.email,
                    onValueChange = vm::onEmailChange,
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                if (state.error != null) {
                    Text(state.error!!, color = MaterialTheme.colorScheme.error)
                }

                Button(
                    onClick = vm::sendReset,
                    enabled = !state.loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.loading) "Enviando..." else "Enviar correo de recuperación")
                }
            }

            if (state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center).size(48.dp)
                )
            }
        }
    }
}
