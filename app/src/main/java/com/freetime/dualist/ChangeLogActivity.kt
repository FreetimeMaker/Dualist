package com.freetime.dualist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.freetime.dualist.R
import com.freetime.dualist.ui.theme.DualistTheme

class ChangeLogActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DualistTheme {
                ChangeLogScreen(onBack = { finish() })
            }
        }
    }
}

@Composable
fun ReleaseCard(
    version: String,
    details: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = version,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            details.forEach { line ->
                Text(
                    text = "• $line",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeLogScreen(onBack: () -> Unit) {
    val releases = listOf(
        "v1.1.6" to listOf(
            "Under the Hood Changes so the F-Droid Build should run successful now"
        ),
        "v1.1.5" to listOf(
            "Hopefully fixed the Error now"
        ),
        "v1.1.4" to listOf(
            "Fixed (hopefully) the F-Droid Reproductible Build Error"
        ),
        "v1.1.3" to listOf(
        "Fixed an F-Droid Reproductible Build Error (Hopefully)"
        ),
        "v1.1.2" to listOf(
            "Fixed an F-Droid Reproductible Build Error"
        ),
        "v1.1.1" to listOf(
            "Fixed the App Crash on App Startup"
        ),
        "v1.1.0" to listOf(
            "Added Search & Filter for tasks.",
            "Added Swipe-to-Delete gesture.",
            "Added Categories and Tags support.",
            "Integrated local JSON Backup & Restore.",
            "Improved Material 3 Adaptive Navigation.",
            "Added Task Reminders and local notifications."
        ),
        "v1.0.0" to listOf(
            "Initial Release.",
            "Offline-first Room database.",
            "Material 3 design.",
            "Adaptive 2-pane layout."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.whats_new_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back_nav_desc))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            releases.forEach { (version, details) ->
                ReleaseCard(version = version, details = details)
            }
        }
    }
}
