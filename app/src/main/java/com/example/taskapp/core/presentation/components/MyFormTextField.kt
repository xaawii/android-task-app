package com.example.taskapp.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MyFormTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    maxLines: Int = 1,
    singleLine: Boolean = true,
    isValid: Boolean = false,
    errorMessage: String = "",
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
        TextField(
            modifier = modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, textAlign = TextAlign.Start) },
            maxLines = maxLines,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = {
                if (isValid && value.isNotBlank()) Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "valid",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        )

        if (!isValid && value.isNotEmpty() && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red
            )
        }
    }
}