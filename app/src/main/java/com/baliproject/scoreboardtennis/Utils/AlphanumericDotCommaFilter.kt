package com.baliproject.scoreboardtennis.Utils

import android.text.InputFilter
import android.text.Spanned

class AlphanumericDotCommaFilter : InputFilter {
    private val allowedPattern = Regex("^[a-zA-Z0-9.,\\s]*$")

    override fun filter(
        source: CharSequence, start: Int, end: Int,
        dest: Spanned, dstart: Int, dend: Int
    ): CharSequence? {
        val newText = dest.substring(0, dstart) + source.substring(start, end) + dest.substring(dend)
        return if (allowedPattern.matches(newText)) null else ""
    }
}
