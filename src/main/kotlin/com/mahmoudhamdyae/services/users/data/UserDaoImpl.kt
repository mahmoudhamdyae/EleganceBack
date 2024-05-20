package com.mahmoudhamdyae.services.users.data

import com.mahmoudhamdyae.database.DatabaseFactory.dbQuery
import com.mahmoudhamdyae.services.users.domain.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UsersDAOImpl : UserDao {
    override suspend fun insertUser(user: User): Boolean {
        val queryResult = dbQuery {
            val insertStatement = UserModel.insert { users ->
                users[userId] = user.userId
                users[email] = user.email
                users[password] = user.password
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
        }
        return queryResult != null
    }

    override suspend fun getUserByEmail(email: String): User? {
        return dbQuery {
            UserModel.select {
                UserModel.email eq email
            }.map(::resultRowToUser).singleOrNull()
        }
    }

    override suspend fun getUserByUserID(userId: String): User? {
        return dbQuery {
            UserModel.select {
                UserModel.userId eq userId
            }.map(::resultRowToUser).singleOrNull()
        }
    }
}

val userDAO: UserDao = UsersDAOImpl()