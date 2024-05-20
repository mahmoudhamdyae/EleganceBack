package com.mahmoudhamdyae.services.tasks.data

import com.mahmoudhamdyae.database.DatabaseFactory.dbQuery
import com.mahmoudhamdyae.services.tasks.domain.Task
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

class TasksDAOImpl : TasksDAO {
    override suspend fun insertTask(task: Task): Boolean {
        val queryResult = dbQuery {
            val insertStatement = TaskModel.insert { tasks ->
                tasks[taskId] = task.taskId
                tasks[userId] = task.userId
                tasks[title] = task.title
                tasks[description] = task.description
                tasks[category] = task.category
                tasks[state] = task.state
                tasks[startDate] = toLocalDateTime(task.startDate)
                tasks[endDate] = toLocalDateTime(task.endDate)
            }
            insertStatement.resultedValues?.singleOrNull()?.let {row ->
                resultRowToTask(row)
            }
        }
        return queryResult != null
    }

    override suspend fun getTasksByUserId(userId: String): List<Task> {
        return dbQuery {
            TaskModel.select {
                TaskModel.userId eq userId
            }.mapNotNull { resultRow ->
                resultRowToTask(resultRow)
            }
        }
    }

    override suspend fun getTaskByTaskId(taskId: String): Task? {
        return dbQuery {
            TaskModel.select {
                TaskModel.taskId eq taskId
            }.map(::resultRowToTask).singleOrNull()
        }
    }

    override suspend fun updateTaskDescriptionByTaskId(taskId: String, description: String): Boolean {
        return dbQuery {
            TaskModel.update( {TaskModel.taskId eq taskId}) {tasks ->
                tasks[TaskModel.description] = description
            } > 0
        }
    }

    override suspend fun updateTaskStateByTaskId(taskId: String, state: Boolean): Boolean {
        return dbQuery {
            TaskModel.update( {TaskModel.taskId eq taskId}) {tasks ->
                tasks[TaskModel.state] = state
            } > 0
        }
    }

    override suspend fun updateTaskEndDateByTaskId(taskId: String, endDate: String): Boolean {
        return dbQuery {
            TaskModel.update( {TaskModel.taskId eq taskId}) {tasks ->
                tasks[TaskModel.endDate] = toLocalDateTime(endDate)
            } > 0
        }
    }

    override suspend fun deleteTask(taskId: String): Boolean {
        return dbQuery {
            TaskModel.deleteWhere {
                TaskModel.taskId eq taskId
            } > 0
        }
    }
}

val tasksDAO: TasksDAO = TasksDAOImpl()