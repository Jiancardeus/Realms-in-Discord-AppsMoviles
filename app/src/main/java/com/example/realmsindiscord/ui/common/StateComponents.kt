package com.example.realmsindiscord.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realmsindiscord.domain.models.Resource
import com.example.realmsindiscord.ui.theme.TealAccent

@Composable
fun <T> ResourceHandler(
    resource: Resource<T>,
    onSuccess: @Composable (T) -> Unit,
    onLoading: @Composable () -> Unit = { DefaultLoadingState() },
    onError: @Composable (String, () -> Unit) -> Unit = { message, onRetry ->
        DefaultErrorState(message = message, onRetry = onRetry)
    },
    onIdle: @Composable () -> Unit = { DefaultIdleState() }
) {
    when (resource) {
        is Resource.Success -> onSuccess(resource.data)
        is Resource.Loading -> onLoading()
        is Resource.Error -> onError(resource.message) { /* Retry será pasado desde fuera */ }
        is Resource.Idle -> onIdle()
    }
}

@Composable
fun DefaultLoadingState(
    message: String = "Cargando...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = TealAccent,
                strokeWidth = 4.dp
            )
            Text(
                text = message,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun DefaultErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("❌", fontSize = 48.sp)
            Text(
                text = "Oops! Algo salió mal",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = message,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = TealAccent)
            ) {
                Text("Reintentar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun DefaultIdleState(
    message: String = "Listo para cargar",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.LightGray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun EmptyState(
    message: String = "No hay elementos para mostrar",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📭", fontSize = 48.sp)
            Text(
                text = message,
                color = Color.LightGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}