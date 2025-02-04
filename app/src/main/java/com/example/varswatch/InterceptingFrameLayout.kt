package com.example.varswatch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

class InterceptingFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        var initialTouchX = 0f
        var initialTouchY = 0f

        when (ev.action) {
            MotionEvent.ACTION_SCROLL -> {
                // Record the initial touch position
                initialTouchX = ev.x
                initialTouchY = ev.y
                Log.i("framelayout","scroll")
            }
            MotionEvent.ACTION_MOVE -> {
                // Check if the finger lifted at the same position it touched
                if (initialTouchX == ev.x && initialTouchY == ev.y) {
                    // Consume the event if the touch and lift positions are the same
                    Log.i("framelayout","move")
                    super.dispatchTouchEvent(ev)
                }
            }
        }
        return false
    }

}
