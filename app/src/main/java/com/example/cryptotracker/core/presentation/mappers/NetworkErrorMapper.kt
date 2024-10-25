package com.example.cryptotracker.core.presentation.mappers

import com.example.cryptotracker.R
import com.example.cryptotracker.core.domain.util.NetworkError
import com.example.cryptotracker.core.presentation.util.UiText

fun NetworkError.toUiText(): UiText {
    return when(this) {
        NetworkError.REQUEST_TIMEOUT -> UiText.StringResource(resId = R.string.error_request_timeout)
        NetworkError.TOO_MANY_REQUESTS -> UiText.StringResource(resId = R.string.error_too_many_requests)
        NetworkError.NO_INTERNET -> UiText.StringResource(resId = R.string.error_no_internet)
        NetworkError.SERVER_ERROR -> UiText.StringResource(resId = R.string.error_unknown)
        NetworkError.SERIALIZATION -> UiText.StringResource(resId = R.string.error_serialization)
        NetworkError.UNKNOWN -> UiText.StringResource(resId = R.string.error_unknown)
    }
}