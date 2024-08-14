package com.example.taskapp.task.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuOption(val name: String, val icon: ImageVector, val onClick: () -> Unit)