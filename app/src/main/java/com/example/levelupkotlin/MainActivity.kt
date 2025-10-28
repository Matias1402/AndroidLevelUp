package com.example.levelupkotlin

import com.example.levelupkotlin.ui.users.UsersScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.levelupkotlin.ui.theme.LevelUpKotlinTheme
import androidx.room.Room
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.levelupkotlin.data.local.AppDatabase
import com.example.levelupkotlin.data.repository.UserRepository
import com.example.levelupkotlin.viewmodel.UserViewModel
import com.example.levelupkotlin.viewmodel.UserViewModelFactory
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my_database"
        ).build()
        val repo = UserRepository(db.userDao())
        val factory = UserViewModelFactory(repo)
        setContent {
            val viewModel: UserViewModel = viewModel(factory = factory)
            LevelUpKotlinTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    UsersScreen(viewModel,
                        Modifier.fillMaxSize().padding(innerPadding))
                }
            }
        }
    }
}