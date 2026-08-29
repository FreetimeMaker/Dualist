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
        "v1.1.7" to listOf(
            stringResource(R.string.cl_v117_detail1)
        ),
        "v1.1.6" to listOf(
            stringResource(R.string.cl_v116_detail1)
        ),
        "v1.1.5" to listOf(
            stringResource(R.string.cl_v115_detail1)
        ),
        "v1.1.4" to listOf(
            stringResource(R.string.cl_v114_detail1)
        ),
        "v1.1.3" to listOf(
            stringResource(R.string.cl_v113_detail1)
        ),
        "v1.1.2" to listOf(
            stringResource(R.string.cl_v112_detail1)
        ),
        "v1.1.1" to listOf(
            stringResource(R.string.cl_v111_detail1)
        ),
        "v1.1.0" to listOf(
            stringResource(R.string.cl_v110_detail1),
            stringResource(R.string.cl_v110_detail2),
            stringResource(R.string.cl_v110_detail3),
            stringResource(R.string.cl_v110_detail4),
            stringResource(R.string.cl_v110_detail5),
            stringResource(R.string.cl_v110_detail6)
        ),
        "v1.0.0" to listOf(
            stringResource(R.string.cl_v100_detail1),
            stringResource(R.string.cl_v100_detail2),
            stringResource(R.string.cl_v100_detail3),
            stringResource(R.string.cl_v100_detail4)
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
