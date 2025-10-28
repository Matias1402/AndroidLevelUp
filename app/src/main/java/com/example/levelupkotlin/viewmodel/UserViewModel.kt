package com.example.levelupkotlin.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levelupkotlin.data.local.User
import com.example.levelupkotlin.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class RegistrationFormState(
    val name: String = "",
    val age: String = "",
    val nameError: String? = null,
    val ageError: String? = null,
    val registrationSuccess: Boolean = false
)

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val users = repository.users.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private val _formState = MutableStateFlow(RegistrationFormState())
    val formState = _formState.asStateFlow()

    fun onNameChange(name: String) {
        _formState.update { it.copy(name = name, nameError = null) }
    }

    fun onAgeChange(age: String) {
        _formState.update { it.copy(age = age, ageError = null) }
    }

    // Lógica de validación desacoplada de la UI
    fun registerUser() {
        val name = _formState.value.name
        val age = _formState.value.age.toIntOrNull()
        var hasError = false

        if (name.isBlank()) {
            _formState.update { it.copy(nameError = "El nombre es obligatorio") }
            hasError = true
        }

        // Validación de edad requerida por el caso "Level-Up Gamer"
        if (age == null || age < 18) {
            _formState.update { it.copy(ageError = "Debes ser mayor de 18 años") }
            hasError = true
        }

        if (hasError) return

        viewModelScope.launch {
            repository.insert(User(name = name, age = age!!))
            // Actualizar estado para notificar a la UI que el registro fue exitoso
            _formState.update { it.copy(registrationSuccess = true) }
        }
    }

    fun resetRegistrationSuccess() {
        _formState.update { it.copy(registrationSuccess = false, name = "", age = "") }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.delete(user)
        }
    }

    fun updateUserPhoto(user: User, photoUri: String) {
        viewModelScope.launch {
            repository.update(user.copy(photoUri = photoUri))
        }
    }
}