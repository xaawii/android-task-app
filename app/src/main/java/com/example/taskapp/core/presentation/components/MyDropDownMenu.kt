package com.example.taskapp.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> MyDropDownMenu(
    items: List<T>,
    selectedItem: String,
    onSelected: (T) -> Unit,
    label: String,
    itemToString: (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedItem,
                onValueChange = { },
                label = { Text(text = label) },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            // Superposición invisible para capturar clics
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .clickable { expanded = !expanded }
            )
        }

        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        onSelected(item)
                        expanded = false
                    },
                    text = { Text(itemToString(item)) }
                )
            }
        }
    }
}