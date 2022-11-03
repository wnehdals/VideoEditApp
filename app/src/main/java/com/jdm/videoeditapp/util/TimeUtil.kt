package com.jdm.videoeditapp.util

import com.google.android.exoplayer2.C
import java.util.*

object TimeUtil {
    fun intToHourFormat(timeMs: Int): String {
        var timeMs = timeMs
        val formatBuilder = StringBuilder()
        val formatter = Formatter(formatBuilder, Locale.getDefault())
        timeMs = Math.abs(timeMs)
        val totalSeconds = (timeMs + 500) / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        formatBuilder.setLength(0)
        return formatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString()
    }
}