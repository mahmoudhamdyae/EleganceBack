package com.mahmoudhamdyae.services.users.domain

import com.mahmoudhamdyae.services.users.data.userDAO
import java.util.UUID

class UsersRepositoryImpl : UserRepository {
    override suspend fun insertUser(email: String, password: String): User? {
        val newUser = User(
            userId = UUID.randomUUID().toString(),
            email = email,
            password = toHashPassword(password = password)
        )
        return if (userDAO.insertUser(newUser)) {
            newUser
        } else null
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDAO.getUserByEmail(email = email)
    }

    override suspend fun isUserEmailExist(email: String): Boolean {
        return userDAO.getUserByEmail(email = email) != null
    }

    override suspend fun isUserPasswordValid(password: String, hashedPassword: String): Boolean {
        return toVerifyHashedPassword(password = password, hashedPassword = hashedPassword)
    }

    override suspend fun isUserIdExist(userId: String): Boolean {
        return userDAO.getUserByUserID(userId = userId) != null
    }
}

val userRepo : UserRepository = UsersRepositoryImpl()