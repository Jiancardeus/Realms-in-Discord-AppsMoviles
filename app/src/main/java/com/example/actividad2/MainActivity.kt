package com.example.actividad2

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.actividad2.ui.home.HomeScreen
import com.example.actividad2.ui.library.CardLibraryScreen
import com.example.actividad2.ui.login.LoginScreen
import com.example.actividad2.data.model.LoginViewModel
import com.example.actividad2.ui.register.RegisterScreen
import com.example.actividad2.data.model.RegisterViewModel
import com.example.actividad2.ui.theme.Actividad2Theme
import dagger.hilt.android.AndroidEntryPoint

// Rutas de navegación
object Screen {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val LIBRARY = "library"
    const val DECKS = "decks"
    const val PLAY = "play"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Actividad2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Observar el destino actual para cambiar la orientación
                    val currentDestination by navController.currentBackStackEntryAsState()
                    val currentRoute = currentDestination?.destination?.route

                    // Forzar landscape solo en Home, portrait en las demás pantallas
                    when (currentRoute) {
                        Screen.HOME -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        else -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    }

                    NavHost(
                        navController = navController,
                        startDestination = Screen.LOGIN
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
                                onRegistrationSuccess = { username ->
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
                                onNavigateToLibrary = { navController.navigate(Screen.LIBRARY) },
                                onNavigateToDecks = { navController.navigate(Screen.DECKS) },
                                onNavigateToPlay = { navController.navigate(Screen.PLAY) }
                            )
                        }

                        // --- 4. Pantalla de BIBLIOTECA ---
                        composable(Screen.LIBRARY) {
                            CardLibraryScreen(navController = navController)
                        }

                        // --- 5. Pantalla de MAZOS (Nueva) ---
                        composable(Screen.DECKS) {
                            // Placeholder - Aquí irá tu pantalla de Mazos
                            androidx.compose.material3.Text(
                                "Pantalla de Mazos - En desarrollo",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        // --- 6. Pantalla de JUGAR (Nueva) ---
                        composable(Screen.PLAY) {
                            // Placeholder - Aquí irá tu pantalla de Jugar
                            androidx.compose.material3.Text(
                                "Pantalla de Jugar - En desarrollo",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}