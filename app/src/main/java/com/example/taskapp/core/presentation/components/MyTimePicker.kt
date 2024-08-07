package com.example.taskapp.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTimePicker(
    selectedTime: String,
    onConfirm: (TimePickerState) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedTime,
            onValueChange = { },
            label = { Text("Pick due time") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showTimePicker = !showTimePicker }) {
                    Icon(
                        imageVector = Icons.Rounded.AccessTime,
                        contentDescription = "Pick time"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )
        // Superposición invisible para capturar clics
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable { showTimePicker = !showTimePicker }
        )

        if (showTimePicker) {
            DialWithDialog(
                onConfirm = {
                    onConfirm(it)
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false })
        }


    }

}