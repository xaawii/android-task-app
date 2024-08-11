package com.example.taskapp.core.domain.validator

sealed interface DataError : Error {
    enum class Network : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        BAD_REQUEST,
        NOT_FOUND,
        FORBIDDEN,
        UNAUTHORIZED,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL
    }
}