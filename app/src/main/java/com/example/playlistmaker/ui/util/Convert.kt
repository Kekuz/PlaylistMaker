package com.example.playlistmaker.ui.util

import android.content.Context
import android.util.TypedValue

object Convert {
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}