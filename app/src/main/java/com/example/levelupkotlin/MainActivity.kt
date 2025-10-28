package com.example.levelupkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.levelupkotlin.data.local.AppDatabase
import com.example.levelupkotlin.data.repository.UserRepository
import com.example.levelupkotlin.ui.theme.LevelUpKotlinTheme
import com.example.levelupkotlin.ui.users.RegistrationScreen
import com.example.levelupkotlin.ui.users.UsersScreen
import com.example.levelupkotlin.viewmodel.UserViewModel
import com.example.levelupkotlin.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my_database"
        )
            .fallbackToDestructiveMigration()
            .build()

        val repo = UserRepository(db.userDao())
        val factory = UserViewModelFactory(repo)

        setContent {
            val viewModel: UserViewModel = viewModel(factory = factory)
            LevelUpKotlinTheme(darkTheme = true) {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "registration") {
                        composable("registration") {
                            RegistrationScreen(
                                viewModel = viewModel,
                                onRegistrationSuccess = {
                                    navController.navigate("users") {
                                        popUpTo("registration") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("users") {
                            UsersScreen(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize(),
                                onNavigateToRegistration = {
                                    navController.navigate("registration")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

