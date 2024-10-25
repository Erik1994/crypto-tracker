package com.example.cryptotracker.core.presentation.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {

    data class DynamicString(val value: String) : UiText
    class StringResource(
        @StringRes val resId: Int,
        val args: Array<Any> = arrayOf()
    ) : UiText

    @Composable
    fun asString(): String {
        return when (this) {
            is StringResource -> stringResource(this.resId, *args)
            is DynamicString -> this.value
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is StringResource -> context.getString(this.resId, *args)
            is DynamicString -> this.value
        }
    }
}