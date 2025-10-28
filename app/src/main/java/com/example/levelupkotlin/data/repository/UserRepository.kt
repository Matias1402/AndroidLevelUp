package com.example.levelupkotlin.data.repository

import com.example.levelupkotlin.data.local.User
import com.example.levelupkotlin.data.local.UserDao
import kotlinx.coroutines.flow.Flow
class UserRepository(private val dao: UserDao) {
    val users: Flow<List<User>> = dao.getAllUsers()
    suspend fun insert(user: User) = dao.insertUser(user)
    suspend fun update(user: User) = dao.updateUser(user)
    suspend fun delete(user: User) = dao.deleteUser(user)
}