package com.mahmoudhamdyae.services.users.domain

interface UserRepository {
    suspend fun insertUser(email: String, password: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun isUserEmailExist(email: String): Boolean
    suspend fun isUserPasswordValid(password: String, hashedPassword: String): Boolean
    suspend fun isUserIdExist(userId: String): Boolean
}