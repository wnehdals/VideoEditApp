package com.jdm.videoeditapp.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object ScreenSizeUtil {
    fun getMetrics(context: Context): DisplayMetrics {
        val metrics = DisplayMetrics()
        var windowManager: WindowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.defaultDisplay.getMetrics(metrics)
        return metrics
    }

}