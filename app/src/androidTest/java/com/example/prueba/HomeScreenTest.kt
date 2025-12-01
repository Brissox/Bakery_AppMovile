package com.example.prueba

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import ui.HomeScreen

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun textos_principales_deben_aparecer_en_pantalla() {
        composeRule.setContent {
            HomeScreen(
                onLoginClick = {},
                onRegisterClick = {},
                onRecoverClick = {}
            )
        }

        composeRule.onNodeWithText("Mi App Kotlin").assertIsDisplayed()
        composeRule.onNodeWithText("¡Bienvenido!").assertIsDisplayed()
        composeRule.onNodeWithText("Login").assertIsDisplayed()
        composeRule.onNodeWithText("Registrarse").assertIsDisplayed()
        composeRule.onNodeWithText("Recuperar contraseña").assertIsDisplayed()
    }

    @Test
    fun al_hacer_click_en_login_se_dispara_onLoginClick() {
        var loginClicked = false

        composeRule.setContent {
            HomeScreen(
                onLoginClick = { loginClicked = true },
                onRegisterClick = {},
                onRecoverClick = {}
            )
        }

        composeRule.onNodeWithText("Login").performClick()

        Assert.assertTrue(loginClicked)
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

        composeRule.onNodeWithText("Registrarse").performClick()
        composeRule.onNodeWithText("Recuperar contraseña").performClick()

        Assert.assertTrue(registerClicked)
        Assert.assertTrue(recoverClicked)
    }
}
