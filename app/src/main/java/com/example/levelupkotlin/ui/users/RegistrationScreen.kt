package com.example.levelupkotlin.ui.users

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levelupkotlin.viewmodel.UserViewModel

@Composable
fun RegistrationScreen(
    viewModel: UserViewModel,
    onRegistrationSuccess: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()

    LaunchedEffect(formState.registrationSuccess) {
        if (formState.registrationSuccess) {
            onRegistrationSuccess()
            viewModel.resetRegistrationSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro en Level-Up Gamer", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(32.dp))

        TextField(
            value = formState.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Nombre de usuario") },
            isError = formState.nameError != null,
            supportingText = {
                formState.nameError?.let { Text(it) }
            },
            singleLine = true
        )
        Spacer(Modifier.height(16.dp))

        TextField(
            value = formState.age,
            onValueChange = { viewModel.onAgeChange(it) },
            label = { Text("Edad") },
            isError = formState.ageError != null,
            supportingText = {
                formState.ageError?.let { Text(it) }
            },
            singleLine = true
        )
        Spacer(Modifier.height(24.dp))

        Button(onClick = { viewModel.registerUser() }) {
            Text("Registrarse")
        }
    }
}
