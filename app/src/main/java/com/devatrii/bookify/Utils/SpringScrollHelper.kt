package com.devatrii.bookify.Utils

import android.util.Log
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

// Created by Kaustubh Patange at 02-02-2021
// Modified by Dev Atrii at 23-07-2023

/**
 * Usage: ```SpringScrollHelper().attachToRecyclerView(recyclerView)```
 */

class SpringScrollHelper {
    /**
     * Adds a snappy spring effect to recyclerView.
     *
     * Passing null will remove it.
     */

    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        if (recyclerView != null) {
            this.recyclerView = recyclerView
            setup()
        } else {
            remove()
        }
    }

    fun setMaxVelocity(maxVelocity: Float) {
        MAX_VELOCITY = maxVelocity
    }

    private var recyclerView: RecyclerView? = null
    private var flingVx: Int = 0
    private var flingVy: Int = 0
    private var MAX_VELOCITY = 2f

    private fun setup() {
        recyclerView?.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        recyclerView?.onFlingListener = recyclerViewFlingListener
        recyclerView?.addOnScrollListener(recyclerViewScrollListener)
    }

    private fun remove() {
        recyclerView?.onFlingListener = null
        recyclerView?.removeOnScrollListener(recyclerViewScrollListener)
        recyclerView = null
    }

    private val recyclerViewFlingListener = object : RecyclerView.OnFlingListener() {
        override fun onFling(velocityX: Int, velocityY: Int): Boolean {
            flingVx = velocityX
            flingVy = velocityY
            return false
        }
    }

    private val recyclerViewScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                var velocity = 1f
                var currentOffset = 0
                var totalOffset = 0
                var property: DynamicAnimation.ViewProperty = DynamicAnimation.SCALE_Y
                val recycledViewHeight = recyclerView.measuredHeight
                val recycledViewWidth = recyclerView.measuredWidth
                if (flingVy != 0) {
                    // If padding is applied at top, bottom
                    currentOffset =
                        (recyclerView.computeVerticalScrollOffset() - recyclerView.paddingTop - recyclerView.paddingBottom).coerceAtLeast(
                            0
                        )
                    totalOffset =
                        recyclerView.computeVerticalScrollRange() - recycledViewHeight
                    velocity = (abs(flingVy) / recycledViewHeight).toFloat()
                    property = DynamicAnimation.SCALE_Y
                    if (flingVy > 0) {
                        recyclerView.pivotY = recycledViewHeight.toFloat()
                    } else {
                        recyclerView.pivotY = 0f
                    }
                } else if (flingVx != 0) {
                    // If padding is applied at start, end
                    currentOffset =
                        (recyclerView.computeHorizontalScrollOffset() - recyclerView.paddingStart - recyclerView.paddingEnd).coerceAtLeast(
                            0
                        )
                    totalOffset =
                        recyclerView.computeHorizontalScrollRange() - recycledViewWidth
                    velocity = (abs(flingVx) / recycledViewWidth).toFloat()
                    property = DynamicAnimation.SCALE_X
                    if (flingVx > 0) {
                        recyclerView.pivotX = recycledViewWidth.toFloat()
                    } else {
                        recyclerView.pivotX = 0f
                    }
                }

                if (currentOffset == totalOffset || currentOffset == 0) {
                    val anim = SpringAnimation(recyclerView, property, 1f)
                    // You can set other animation parameters like stiffness or damping ratio, I'm leaving it default.
                    anim.setStartVelocity(velocity.coerceAtMost(MAX_VELOCITY)).start()
                }
            }
        }
    }
}

