package com.example.taskapp.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.taskapp.task.presentation.components.TextFieldForPicker

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

        TextFieldForPicker(
            label = "Due time",
            value = selectedTime,
            icon = Icons.Rounded.AccessTime
        ) {
            showTimePicker = !showTimePicker
        }


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