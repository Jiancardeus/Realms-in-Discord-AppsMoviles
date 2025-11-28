package com.example.realmsindiscord

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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.ui.deck.DeckBuilderScreen
import com.example.realmsindiscord.ui.home.HomeScreen
import com.example.realmsindiscord.ui.library.CardLibraryScreen
import com.example.realmsindiscord.ui.login.LoginScreen
import com.example.realmsindiscord.ui.play.PlayScreen
import com.example.realmsindiscord.ui.profile.ProfileManagementScreen
import com.example.realmsindiscord.ui.register.RegisterScreen
import com.example.realmsindiscord.ui.theme.RealmsInDiscordTheme
import com.example.realmsindiscord.viewmodel.login.LoginViewModel
import com.example.realmsindiscord.viewmodel.profile.ProfileManagementViewModel
import com.example.realmsindiscord.viewmodel.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

object Screen {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val LIBRARY = "library"
    const val DECKS = "decks"
    const val PLAY = "play"
    const val PROFILE_MANAGEMENT = "profile_management"
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealmsInDiscordTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val currentDestination by navController.currentBackStackEntryAsState()
                    val currentRoute = currentDestination?.destination?.route

                    when (currentRoute) {
                        Screen.HOME -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        else -> requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
                                onNavigateToPlay = { navController.navigate(Screen.PLAY) },
                                onNavigateToProfileManagement = { user ->
                                    // Navegar a la pantalla de gestión de perfil
                                    navController.navigate("${Screen.PROFILE_MANAGEMENT}/${user.id}")
                                }
                            )
                        }

                        composable(Screen.LIBRARY) {
                            CardLibraryScreen(navController = navController)
                        }

                        composable(Screen.DECKS) {
                            DeckBuilderScreen()
                        }

                        composable(Screen.PLAY) {
                            PlayScreen()
                        }

                        // Gestión de Perfil
                        composable(
                            route = "${Screen.PROFILE_MANAGEMENT}/{userId}",
                            arguments = listOf(
                                navArgument("userId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            val userId = backStackEntry.arguments?.getInt("userId") ?: 0


                            val tempUser = User(
                                id = userId,
                                username = "Usuario Temporal",
                                email = "temp@example.com",
                                passwordHash = "",
                                level = 1,
                                experience = 0,
                                wins = 0,
                                losses = 0,
                                draws = 0
                            )

                            ProfileManagementScreen(
                                user = tempUser,
                                onBack = { navController.popBackStack() },
                                onLogout = {
                                    navController.navigate(Screen.LOGIN) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

