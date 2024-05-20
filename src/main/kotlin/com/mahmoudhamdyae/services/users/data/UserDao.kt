package com.mahmoudhamdyae.services.users.data

import com.mahmoudhamdyae.services.users.domain.User

interface UserDao {
    suspend fun insertUser(user: User): Boolean
    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserByUserID(userId: String): User?
}