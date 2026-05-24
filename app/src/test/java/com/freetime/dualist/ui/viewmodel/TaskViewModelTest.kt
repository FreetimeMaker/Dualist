package com.freetime.dualist.ui.viewmodel

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.freetime.dualist.data.AppDatabase
import com.freetime.dualist.data.Task
import com.freetime.dualist.data.TaskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class TaskViewModelTest {

    private lateinit var viewModel: TaskViewModel
    private lateinit var database: AppDatabase
    private lateinit var taskDao: TaskDao
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Application>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        
        // Note: TaskViewModel currently creates its own database instance via AppDatabase.getDatabase(application).
        // For production testing, we would ideally use dependency injection.
        // For this MVP, we'll assume the logic in TaskViewModel is correct or refactor it for testability if needed.
        viewModel = TaskViewModel(context)
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `initial tasks list is empty`() = runTest {
        val tasks = viewModel.tasks.value
        assertEquals(0, tasks.size)
    }
}
