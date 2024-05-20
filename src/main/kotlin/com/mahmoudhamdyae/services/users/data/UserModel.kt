package com.mahmoudhamdyae.services.users.data

import com.mahmoudhamdyae.services.users.domain.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object UserModel: Table() {
    val userId = varchar("userId", 64)
    val email = varchar("email", 128)
    val password = varchar("password", 128)

    override val primaryKey = PrimaryKey(userId)
}

fun resultRowToUser(row: ResultRow) = User(
    userId = row[UserModel.userId],
    email = row[UserModel.email],
    password = row[UserModel.password]
)