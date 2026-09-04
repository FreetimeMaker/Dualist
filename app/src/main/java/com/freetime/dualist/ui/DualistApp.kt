package com.freetime.dualist.ui

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.freetime.dualist.ui.screens.TaskDetailScreen
import com.freetime.dualist.ui.screens.TaskListScreen
import com.freetime.dualist.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DualistApp(viewModel: TaskViewModel = viewModel()) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>()
    val scope = rememberCoroutineScope()
    val tasks by viewModel.tasks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            TaskListScreen(
                tasks = tasks,
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.onSearchQueryChange(it) },
                onTaskClick = { task ->
                    scope.launch {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, task.id)
                    }
                },
                onToggleTask = { viewModel.toggleTaskCompletion(it) },
                onDeleteTask = { viewModel.deleteTask(it) },
                onAddTask = { title, description, category -> 
                    viewModel.addTask(title, description, category) 
                },
                onExportTasks = { viewModel.exportTasks(it) },
                onImportTasks = { viewModel.importTasks(it) }
            )
        },
        detailPane = {
            val taskId = navigator.currentDestination?.contentKey
            val task = taskId?.let { viewModel.getTaskById(it) }
            
            TaskDetailScreen(
                task = task,
                onDeleteTask = {
                    viewModel.deleteTask(it)
                    scope.launch {
                        navigator.navigateBack()
                    }
                }
            )
        }
    )
}
