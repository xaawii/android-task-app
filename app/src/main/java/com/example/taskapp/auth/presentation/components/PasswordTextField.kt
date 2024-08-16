package com.example.taskapp.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordTextField(
    value: String,
    label: String,
    isValid: Boolean = false,
    errorMessage: String = "",
    onValueChange: (String) -> Unit
) {

    var passwordVisibility by remember { mutableStateOf(false) }

    Column {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisibility) {
                    Icons.Rounded.Visibility
                } else {
                    Icons.Rounded.VisibilityOff
                }

                Row {

                    if (isValid && value.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Rounded.Check,
                            contentDescription = "valid",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(imageVector = image, contentDescription = "show/hide password")
                    }
                }


            },
            visualTransformation = if (passwordVisibility) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }

        )

        if (!isValid && value.isNotEmpty() && errorMessage.isNotEmpty()) {
            Text(text = errorMessage, style = MaterialTheme.typography.bodyMedium, color = Color.Red)
        }
    }
}