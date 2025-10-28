package com.example.levelupkotlin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupkotlin.data.local.User
import com.example.levelupkotlin.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val users = repository.users.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(),
        emptyList()
    )
    fun addUser(name: String, age: Int) {
        viewModelScope.launch {
            repository.insert(User(name = name, age = age))
        }
    }
    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.delete(user)
        }
    }
    fun updateUser(user: User, newName: String, newAge: Int) {
        viewModelScope.launch {
            repository.update(user.copy(name = newName, age = newAge))
        }
    }
}