package com.example.actividad2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.actividad2.ui.login.LoginScreen
import com.example.actividad2.ui.login.LoginViewModel
import com.example.actividad2.ui.register.RegisterScreen
import com.example.actividad2.ui.register.RegisterViewModel
import com.example.actividad2.ui.home.HomeScreen
import com.example.actividad2.ui.library.CardLibraryScreen
import com.example.actividad2.ui.theme.Actividad2Theme
import dagger.hilt.android.AndroidEntryPoint

// Rutas de navegación
object Screen {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val LIBRARY = "library"

}

@AndroidEntryPoint // Hilt inyecta el ViewModel en esta Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Actividad2Theme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Screen.LOGIN // Siempre empezamos en el login por defecto
                ) {
                    // --- 1. Pantalla de LOGIN ---
                    composable(Screen.LOGIN) {
                        val viewModel: LoginViewModel = hiltViewModel()
                        LoginScreen(
                            viewModel = viewModel,
                            onLoginSuccess = {
                                // Navegación al Home después del éxito
                                navController.navigate(Screen.HOME) {
                                    // Evita que el usuario regrese a Login con el botón Atrás
                                    popUpTo(Screen.LOGIN) { inclusive = true }
                                }
                            },
                            onNavigateToRegister = { navController.navigate(Screen.REGISTER) }
                        )
                    }
                    // --- 2. Pantalla de REGISTRO ---
                    composable(Screen.REGISTER) {
                        val viewModel: RegisterViewModel = hiltViewModel()
                        RegisterScreen(
                            viewModel = viewModel,
                            onRegistrationSuccess = { username -> // Debemos recibir el username para pasarlo al Home
                                // Navegación al Home después del éxito
                                navController.navigate(Screen.HOME) {
                                    popUpTo(Screen.LOGIN) { inclusive = true }
                                }
                            },
                            // El parámetro 'onNavigateToLogin' es el requerido
                            onNavigateToLogin = { navController.popBackStack() }
                        )
                    }

                    // --- 3. Pantalla de HOME (Contenedor Principal) ---
                    composable(Screen.HOME) {
                        HomeScreen(
                            onLogout = {
                                // Al hacer Logout, vuelve al Login
                                navController.navigate(Screen.LOGIN) {
                                    popUpTo(Screen.HOME) { inclusive = true }
                                }
                            },
                            onNavigateToLibrary = { navController.navigate(Screen.LIBRARY) }
                        )
                    }

                    // --- 4. Pantalla de BIBLIOTECA ---
                    composable(Screen.LIBRARY) {
                        CardLibraryScreen(navController = navController)
                    }
                }
            }
        }
    }
}