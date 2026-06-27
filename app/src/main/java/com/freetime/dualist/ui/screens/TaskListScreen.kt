package com.freetime.dualist.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.freetime.dualist.ChangeLogActivity
import com.freetime.dualist.DonateActivity
import com.freetime.dualist.R
import com.freetime.dualist.data.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<Task>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onTaskClick: (Task) -> Unit,
    onToggleTask: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onAddTask: (String, String) -> Unit,
    onExportTasks: (Uri) -> Unit,
    onImportTasks: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskDescription by remember { mutableStateOf("") }

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let { onExportTasks(it) }
    }

    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onImportTasks(it) }
    }

    Scaffold(
        topBar = {
            if (isSearchActive) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    onSearch = { isSearchActive = false },
                    active = true,
                    onActiveChange = { isSearchActive = it },
                    placeholder = { Text("Search tasks...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = { 
                        IconButton(onClick = { isSearchActive = false; onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Close search")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {}
            } else {
                LargeTopAppBar(
                    title = { 
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                stringResource(R.string.app_name),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Changelog") },
                                onClick = { 
                                    showMenu = false
                                    context.startActivity(Intent(context, ChangeLogActivity::class.java))
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Donate") },
                                onClick = { 
                                    showMenu = false
                                    context.startActivity(Intent(context, DonateActivity::class.java))
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Export Tasks") },
                                onClick = { 
                                    showMenu = false
                                    exportLauncher.launch("dualist_backup.json")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Import Tasks") },
                                onClick = { 
                                    showMenu = false
                                    importLauncher.launch("application/json")
                                }
                            )
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                )
            }
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    Icons.Default.Add, 
                    contentDescription = stringResource(R.string.add_task),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    ) { padding ->
        Column(modifier = modifier.padding(padding).fillMaxSize()) {
            if (tasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_tasks), style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn {
                    items(tasks, key = { it.id }) { task ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    onDeleteTask(task)
                                    true
                                } else false
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                val color = when (dismissState.dismissDirection) {
                                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.errorContainer
                                    else -> Color.Transparent
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 24.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            },
                            enableDismissFromStartToEnd = false
                        ) {
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = task.title,
                                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.SemiBold
                                    )
                                },
                                supportingContent = {
                                    Column {
                                        if (task.description.isNotEmpty()) {
                                            Text(
                                                task.description,
                                                maxLines = 1,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                        SuggestionChip(
                                            onClick = { },
                                            label = { Text(task.category, style = MaterialTheme.typography.labelSmall) },
                                            modifier = Modifier.height(24.dp)
                                        )
                                    }
                                },
                                leadingContent = {
                                    IconButton(
                                        onClick = { onToggleTask(task) },
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (task.isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                                            contentDescription = stringResource(R.string.toggle_completion),
                                            tint = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                },
                                colors = ListItemDefaults.colors(
                                    containerColor = if (task.isCompleted) 
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) 
                                    else MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onTaskClick(task) }
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(stringResource(R.string.new_task), style = MaterialTheme.typography.headlineSmall) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        label = { Text(stringResource(R.string.task_title)) },
                        placeholder = { Text(stringResource(R.string.task_title_placeholder)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                    OutlinedTextField(
                        value = newTaskDescription,
                        onValueChange = { newTaskDescription = it },
                        label = { Text(stringResource(R.string.description_label)) },
                        placeholder = { Text("Optional details...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTaskTitle.isNotBlank()) {
                            onAddTask(newTaskTitle, newTaskDescription)
                            newTaskTitle = ""
                            newTaskDescription = ""
                            showAddDialog = false
                        }
                    },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(stringResource(R.string.add_task))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            shape = MaterialTheme.shapes.extraLarge
        )
    }
}
