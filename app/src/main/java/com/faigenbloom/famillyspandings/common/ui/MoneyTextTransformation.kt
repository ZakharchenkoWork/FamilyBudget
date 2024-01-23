package com.faigenbloom.famillyspandings.common.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.util.Currency
import java.util.Locale

class MoneyTextTransformation(
    private val currencyCode: String = Currency.getInstance(Locale.getDefault()).currencyCode,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return if (text.isBlank()) {
            TransformedText(
                text = AnnotatedString("0.00 $currencyCode"),
                offsetMapping = MoneyOffset(text),
            )
        } else {
            return TransformedText(
                text = AnnotatedString(filter(text.text)),
                offsetMapping = MoneyOffset(text),
            )
        }
    }

    fun filter(text: String): String {
        return "$text $currencyCode"
    }

    private data class MoneyOffset(val text: AnnotatedString) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = offset
        override fun transformedToOriginal(offset: Int): Int = if (text.isNotBlank()) {
            if (offset >= text.length) text.length - 1
            else
                offset
        } else 0
    }
}
