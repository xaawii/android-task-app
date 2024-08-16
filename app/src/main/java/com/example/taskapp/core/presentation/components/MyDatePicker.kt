package com.example.taskapp.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    selectedDate: String,
    onConfirm: (DatePickerState) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()


    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            value = selectedDate,
            onValueChange = { },
            label = { Text("Pick a due date") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Rounded.CalendarMonth,
                        contentDescription = "Select date"
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
                .clickable { showDatePicker = !showDatePicker }
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDismiss = { showDatePicker = !showDatePicker },
                onConfirm = {
                    onConfirm(datePickerState)
                    showDatePicker = false
                }) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )
            }

        }
    }
}