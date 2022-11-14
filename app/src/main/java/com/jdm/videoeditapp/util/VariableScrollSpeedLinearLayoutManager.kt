package com.jdm.videoeditapp.util

import android.content.Context
import android.graphics.PointF
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView


class VariableScrollSpeedLinearLayoutManager(context: Context, private val factor: Float) :
    LinearLayoutManager(context) {
    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State?,
        position: Int
    ) {
        val linearSmoothScroller: LinearSmoothScroller =
            object : LinearSmoothScroller(recyclerView.getContext()) {
                override fun computeScrollVectorForPosition(targetPosition: Int): PointF {
                    return computeScrollVectorForPosition(targetPosition)
                }

                protected override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                    return super.calculateSpeedPerPixel(displayMetrics) * factor
                }
            }
        linearSmoothScroller.setTargetPosition(position)
        startSmoothScroll(linearSmoothScroller)
    }
}