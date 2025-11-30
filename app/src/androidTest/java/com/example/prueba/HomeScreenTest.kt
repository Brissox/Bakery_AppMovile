package com.example.prueba

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.prueba.ui.home.HomeScreen
import org.junit.Assert

@RunWith(AndroidJUnit4::class) //Este test se ejecuta como test de Android
class HomeScreenTest {

    @get:Rule //le dice a JUnit que esta propiedad es una regla de test.
    val composeRule = createAndroidComposeRule<ComponentActivity>() //Crea una ComponentActivity de prueba


    @Test //marca el metodo como un test
    fun textos_principales_deben_aparecer_en_pantalla() {
        // Arrange: renderizamos la HomeScreen con callbacks vacíos
        composeRule.setContent {
            HomeScreen (
                onLoginClick = {},
                onRegisterClick = {},
                onRecoverClick = {}
            )
        }

        // Assert: validamos que los textos clave se muestren
        composeRule.onNodeWithText("Pasteleria Mil Sabores").assertExists()
        composeRule.onNodeWithText("¡Bienvenido!").assertExists()
        composeRule.onNodeWithText("Login").assertExists()
        composeRule.onNodeWithText("Registrarse").assertExists()
        composeRule.onNodeWithText("Recuperar contraseña").assertExists()
    }

    @Test
    fun al_hacer_click_en_login_se_dispara_onLoginClick() {
        var loginClicked = false //Para saber si hubo click

        composeRule.setContent {
            HomeScreen(
                onLoginClick = { loginClicked = true }, //Cambia la variable como si hicieran click
                onRegisterClick = {},
                onRecoverClick = {}
            )
        }

        // Act: hacemos click en el botón "Login"
        composeRule.onNodeWithText("Login").performClick()

        // Assert: verificamos que el callback se ejecutó
        composeRule.runOnIdle {
            Assert.assertTrue(loginClicked)
        }
    }

    @Test
    fun al_hacer_click_en_registrarse_y_recuperar_se_disparan_callbacks() {
        var registerClicked = false
        var recoverClicked = false

        composeRule.setContent {
            HomeScreen(
                onLoginClick = {},
                onRegisterClick = { registerClicked = true },
                onRecoverClick = { recoverClicked = true }
            )
        }

        // Act
        composeRule.onNodeWithText("Registrarse").performClick()
        composeRule.onNodeWithText("Recuperar contraseña").performClick()

        // Assert
        Assert.assertTrue(registerClicked)
        Assert.assertTrue(recoverClicked)
    }
}