package com.freetime.dualist.ui.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.freetime.dualist.data.AppDatabase
import com.freetime.dualist.data.Task
import com.freetime.dualist.data.TaskRepository
import com.freetime.dualist.reminder.ReminderReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    private val context = application.applicationContext

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        val taskDao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
    }

    val tasks: StateFlow<List<Task>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.allTasks
            } else {
                repository.searchTasks(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun addTask(title: String, description: String = "", category: String = "General") {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.insert(Task(title = title, description = description, category = category))
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            repository.update(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
    
    fun getTaskById(taskId: Int): Task? {
        return tasks.value.find { it.id == taskId }
    }

    fun exportTasks(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val json = Json.encodeToString(tasks.value)
                context.contentResolver.openOutputStream(uri)?.use { 
                    it.write(json.toByteArray())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun importTasks(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val content = context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
                if (content != null) {
                    val importedList = Json.decodeFromString<List<Task>>(content)
                    for (task in importedList) {
                        repository.insert(task.copy(id = 0))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun scheduleReminder(task: Task, timeInMillis: Long) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("TASK_ID", task.id)
            putExtra("TASK_TITLE", task.title)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, task.id, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }
}
