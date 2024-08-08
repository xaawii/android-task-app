package com.example.taskapp.core.data.exceptions

class InvalidTokenException(message: String = "Token expired") : Exception(message) {
}