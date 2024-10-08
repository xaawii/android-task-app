package com.example.taskapp.core.presentation.utils


import com.example.taskapp.R
import com.example.taskapp.core.domain.validator.DataError
import com.example.taskapp.core.domain.validator.Result
import com.example.taskapp.core.domain.validator.UserDataValidator
import com.example.taskapp.task.domain.enum.TaskStatus

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(
            R.string.the_request_timed_out
        )

        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.youve_hit_your_rate_limit
        )

        DataError.Network.NO_INTERNET -> UiText.StringResource(
            R.string.no_internet
        )

        DataError.Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(
            R.string.file_too_large
        )

        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.server_error
        )

        DataError.Network.SERIALIZATION -> UiText.StringResource(
            R.string.error_serialization
        )

        DataError.Network.UNKNOWN -> UiText.StringResource(
            R.string.unknown_error
        )

        DataError.Local.DISK_FULL -> UiText.StringResource(
            R.string.error_disk_full
        )

        DataError.Network.BAD_REQUEST -> UiText.StringResource(
            R.string.error_bad_request
        )

        DataError.Network.NOT_FOUND -> UiText.StringResource(
            R.string.error_not_found
        )

        DataError.Network.FORBIDDEN -> UiText.StringResource(
            R.string.error_forbidden
        )

        DataError.Network.UNAUTHORIZED -> UiText.StringResource(
            R.string.error_unauthorized
        )
    }
}

fun Result.Error<*, DataError>.asErrorUiText(): UiText {
    return error.asUiText()
}

fun TaskStatus.asUiText(): UiText {
    return when (this) {
        TaskStatus.PENDING -> UiText.StringResource(
            R.string.status_pending
        )

        TaskStatus.IN_PROGRESS -> UiText.StringResource(
            R.string.status_in_progress
        )

        TaskStatus.COMPLETED -> UiText.StringResource(
            R.string.status_completed
        )
    }
}

fun  UserDataValidator.PasswordError.asUiText(): UiText {
    return when (this) {
        UserDataValidator.PasswordError.TOO_SHORT -> UiText.StringResource(
            R.string.min_six_characters
        )
        UserDataValidator.PasswordError.NO_UPPERCASE -> UiText.StringResource(
            R.string.need_min_one_uppercase
        )
        UserDataValidator.PasswordError.NO_DIGIT -> UiText.StringResource(
            R.string.need_min_one_digit
        )
        UserDataValidator.PasswordError.NO_MATCHES -> UiText.StringResource(
            R.string.password_dont_match
        )
    }
}

fun  UserDataValidator.NameError.asUiText(): UiText {
    return when (this) {
        UserDataValidator.NameError.TOO_SHORT -> UiText.StringResource(
            R.string.min_three_characters
        )
    }
}

fun  UserDataValidator.EmailError.asUiText(): UiText {
    return when (this) {
        UserDataValidator.EmailError.INVALID_FORMAT -> UiText.StringResource(
            R.string.invalid_email_format
        )
    }
}