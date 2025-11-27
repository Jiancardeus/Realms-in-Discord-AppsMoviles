package com.example.actividad2

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
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
import com.example.actividad2.ui.login.LoginViewModel
import com.example.actividad2.ui.register.RegisterScreen
import com.example.actividad2.ui.register.RegisterViewModel
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

                    val currentDestination by navController.currentBackStackEntryAsState()
                    val currentRoute = currentDestination?.destination?.route

                    // Corregir error de orientation
                    LaunchedEffect(currentRoute) {
                        val orientation = when (currentRoute) {
                            Screen.HOME -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                            else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        }
                        requestedOrientation = orientation
                    }

                    NavHost(
                        navController = navController,
                        startDestination = Screen.LOGIN
                    ) {
                        composable(Screen.LOGIN) {
                            val viewModel: LoginViewModel = hiltViewModel()
                            LoginScreen(
                                viewModel = viewModel,
                                onLoginSuccess = {
                                    navController.navigate(Screen.HOME) {
                                        popUpTo(Screen.LOGIN) { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate(Screen.REGISTER) }
                            )
                        }

                        composable(Screen.REGISTER) {
                            val viewModel: RegisterViewModel = hiltViewModel()
                            RegisterScreen(
                                viewModel = viewModel,
                                onRegistrationSuccess = { username ->
                                    navController.navigate(Screen.HOME) {
                                        popUpTo(Screen.LOGIN) { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = { navController.popBackStack() }
                            )
                        }

                        composable(Screen.HOME) {
                            HomeScreen(
                                onLogout = {
                                    navController.navigate(Screen.LOGIN) {
                                        popUpTo(Screen.HOME) { inclusive = true }
                                    }
                                },
                                onNavigateToLibrary = { navController.navigate(Screen.LIBRARY) },
                                onNavigateToDecks = { navController.navigate(Screen.DECKS) },
                                onNavigateToPlay = { navController.navigate(Screen.PLAY) }
                            )
                        }

                        composable(Screen.LIBRARY) {
                            CardLibraryScreen(navController = navController)
                        }

                        composable(Screen.DECKS) {
                            androidx.compose.material3.Text(
                                "Pantalla de Mazos - En desarrollo",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        composable(Screen.PLAY) {
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