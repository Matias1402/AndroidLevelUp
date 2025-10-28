package com.example.levelupkotlin.ui.users

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAdd // 👇 CAMBIO: Importar el nuevo ícono
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.levelupkotlin.data.local.User
import com.example.levelupkotlin.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(
    viewModel: UserViewModel,
    modifier: Modifier = Modifier,
    onNavigateToRegistration: () -> Unit // 👇 CAMBIO: Recibir la nueva función
) {
    val users by viewModel.users.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Comunidad Level-Up") },
                // 👇 CAMBIO: Añadir el botón de acción en la barra
                actions = {
                    IconButton(onClick = onNavigateToRegistration) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Agregar nuevo usuario"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(users, key = { it.id }) { user ->
                UserItem(
                    user = user,
                    onDelete = { viewModel.deleteUser(user) },
                    onUpdatePhoto = { uri ->
                        viewModel.updateUserPhoto(user, uri.toString())
                    }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun UserItem(
    modifier: Modifier = Modifier,
    user: User,
    onDelete: () -> Unit,
    onUpdatePhoto: (Uri) -> Unit
) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onUpdatePhoto(it) }
        }
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = user.photoUri),
            contentDescription = "Foto de perfil de ${user.name}",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .clickable { galleryLauncher.launch("image/*") },
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(user.name, style = MaterialTheme.typography.titleMedium)
            Text("${user.age} años", style = MaterialTheme.typography.bodyMedium)
        }

        IconButton(onClick = { galleryLauncher.launch("image/*") }) {
            Icon(Icons.Default.AddAPhoto, contentDescription = "Cambiar foto")
        }

        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar usuario")
        }
    }
}
