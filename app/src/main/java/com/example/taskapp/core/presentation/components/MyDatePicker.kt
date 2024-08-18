package com.example.taskapp.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.taskapp.R
import com.example.taskapp.task.presentation.components.TextFieldForPicker

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

        TextFieldForPicker(
            label = stringResource(R.string.due_date),
            value = selectedDate,
            icon = Icons.Rounded.CalendarMonth
        ) {
            showDatePicker = !showDatePicker
        }

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