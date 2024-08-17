package com.example.taskapp.task.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldForPicker(
    label: String,
    value: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            value = value,
            onValueChange = {},
            label = { Text(label, textAlign = TextAlign.Start) },
            maxLines = 1,
            singleLine = true,
            readOnly = true,
            leadingIcon = {
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "valid",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

            }
        )

        // Superposición invisible para capturar clics
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable { onClick() }
        )


    }
}