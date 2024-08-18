package com.example.taskapp.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.taskapp.R

@Composable
fun ErrorComponent(
    text: String, reload: () -> Unit, hasBackButton: Boolean, onBackPressed: () -> Unit
) {

    Scaffold(topBar = { if (hasBackButton) TopAppBarBack(onBackPressed = onBackPressed) }) { contentPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconInsideCircle(icon = Icons.Rounded.Error)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = text, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = reload) {
                Text(
                    text = stringResource(R.string.reload),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}