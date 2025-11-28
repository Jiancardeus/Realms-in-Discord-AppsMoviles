package com.example.realmsindiscord.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.realmsindiscord.R
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.ui.components.ProfileButton
import com.example.realmsindiscord.ui.theme.TealAccent
import com.example.realmsindiscord.viewmodel.profile.ProfileViewModel

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onNavigateToDecks: () -> Unit,
    onNavigateToPlay: () -> Unit,
    onNavigateToProfileManagement: (User) -> Unit
) {

    val profileViewModel: ProfileViewModel = hiltViewModel()
    val context = LocalContext.current
    val activity = context as? android.app.Activity

    LaunchedEffect(Unit) {
        activity?.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // FONDO DE PANTALLA
        Image(
            painter = painterResource(id = R.drawable.home_background),
            contentDescription = "Home Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // LAYOUT PRINCIPAL CON NAVBAR LATERAL
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // --- NAVBAR LATERAL IZQUIERDA ---
            Column(
                modifier = Modifier
                    .width(200.dp)
                    .fillMaxHeight()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // CONTENIDO SUPERIOR DE LA NAVBAR
                Column {
                    // BOTÓN DE PERFIL CORREGIDO
                    ProfileButton(
                        viewModel = profileViewModel,
                        onEditProfile = { user ->
                            onNavigateToProfileManagement(user)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // BOTONES DE NAVEGACIÓN
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        NavBarButton(text = "JUGAR", onClick = onNavigateToPlay)
                        NavBarButton(text = "MAZOS", onClick = onNavigateToDecks)
                        NavBarButton(text = "BIBLIOTECA", onClick = onNavigateToLibrary)
                    }
                }

                // BOTÓN DE CERRAR SESIÓN INFERIOR
                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))
                ) {
                    Text("Cerrar Sesión", color = Color.White)
                }
            }

            // --- CONTENIDO PRINCIPAL DERECHO ---
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // SECCIÓN SUPERIOR (Eventos/Noticias)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column {
                                Text(
                                    "PRÓXIMO TORNEO REALMS IN DISCORD",
                                    color = TealAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    "COMIENZA EN 16 DÍAS",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { /* Navegar a torneos */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = TealAccent)
                                ) {
                                    Text("JUGAR PATH OF CHAMPIONS", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // SECCIÓN MEDIA
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "¡BIENVENIDO A REALMS IN DISCORD!",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // SECCIÓN INFERIOR (Tienda/Estadísticas)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { /* Navegar a tienda */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("TIENDA", color = TealAccent, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { /* Navegar a estadísticas */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("VICTORIAS", color = TealAccent, fontSize = 12.sp)
                                    Text("50", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavBarButton(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        color = TealAccent,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
    )
}