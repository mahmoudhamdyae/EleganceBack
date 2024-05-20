package com.mahmoudhamdyae.services.tasks.data

import com.mahmoudhamdyae.services.tasks.domain.Task
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object TaskModel: Table() {
    val taskId = varchar("taskId", 64)
    val userId = varchar("userId", 64)
    val title = varchar("title", 128)
    val description = varchar("description", 512)
    val category = varchar("category", 64)
    val state = bool("state")
    val startDate = datetime("start_date")
    val endDate = datetime("end_date")

    override val primaryKey = PrimaryKey(taskId)
}

// this is a mapper function that builds a task data class
fun resultRowToTask(row: ResultRow) = Task(
    taskId = row[TaskModel.taskId],
    userId = row[TaskModel.userId],
    title = row[TaskModel.title],
    description = row[TaskModel.description],
    category = row[TaskModel.category],
    state = row[TaskModel.state],
    startDate = row[TaskModel.startDate].toString(),
    endDate = row[TaskModel.endDate].toString(),
)

// This is a helper function that parse local date time alike string
// and return LocalDateTime object
fun toLocalDateTime(localDateTime: String): LocalDateTime {
    return LocalDateTime.parse(localDateTime)
}