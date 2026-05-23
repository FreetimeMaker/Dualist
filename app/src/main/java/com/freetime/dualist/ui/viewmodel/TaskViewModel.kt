package com.freetime.dualist.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.freetime.dualist.data.AppDatabase
import com.freetime.dualist.data.Task
import com.freetime.dualist.data.TaskDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao: TaskDao = AppDatabase.getDatabase(application).taskDao()

    val tasks: StateFlow<List<Task>> = taskDao.getAllTasks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTask(title: String, description: String = "") {
        if (title.isBlank()) return
        viewModelScope.launch {
            taskDao.insertTask(Task(title = title, description = description))
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }
    
    fun getTaskById(taskId: Int): Task? {
        return tasks.value.find { it.id == taskId }
    }
}
