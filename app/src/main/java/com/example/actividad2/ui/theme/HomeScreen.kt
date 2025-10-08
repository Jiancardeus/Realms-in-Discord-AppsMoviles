package com.example.actividad2.ui.theme


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.actividad2.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.foundation.Image


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeScreen()   {
    Scaffold (
        topBar = {
            TopAppBar(title = {Text("Mi TCG en Kotlin")})
        }
    ){ InnerPadding ->
        Column (
            modifier = Modifier
                .padding(InnerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "Bienvenido!")
            Button(onClick = {}) {
                Text("Presioname")
            }
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo app",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}