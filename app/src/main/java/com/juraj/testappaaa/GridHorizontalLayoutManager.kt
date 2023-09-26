package com.juraj.testappaaa

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.State
import androidx.recyclerview.widget.LinearSmoothScroller
import java.lang.Integer.min
import java.lang.Math.max

class GridHorizontalLayoutManager(
    context: Context,
    private val numRows: Int,
    private val numColumns: Int,
    private val reverseLayout: Boolean = false
) : LinearLayoutManager(context, HORIZONTAL, reverseLayout) {

    private var totalWidth = 0
    private var totalHeight = 0

    var totalScroll = 0

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: Recycler, state: State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        fill(recycler)
    }

    private fun fill(recycler: Recycler) {
        detachAndScrapAttachedViews(recycler)

        val width = width
        val height = height
        val itemWidth = width / numColumns
        val itemHeight = height / numRows

        totalWidth = itemWidth * numColumns
        totalHeight = itemHeight * numRows

        for (i in 0 until itemCount/(numColumns*numRows)) {
            for (row in 0 until numRows) {
                for (col in 0 until numColumns) {
                    val position = getPositionFor(i, row, col)
                    Log.d("TESTXXX", "position: ${position}")
                    if (position >= itemCount) {
                        break
                    }

                    val view = recycler.getViewForPosition(position)
                    addView(view)
                    measureChildWithMargins(view, 0, 0)
                    val left = (width * i) + (col * itemWidth) - totalScroll
                    val top = row * itemHeight
                    val right = left + itemWidth
                    val bottom = top + itemHeight
                    layoutDecorated(view, left, top, right, bottom)
                }
            }
        }
    }

    private fun getPositionFor(index: Int, row: Int, col: Int): Int {
        return if (reverseLayout) {
            (index * numColumns * numRows) + (numRows - row - 1) * numColumns + (numColumns - col - 1)
        } else {
            (index * numColumns * numRows) + (row * numColumns + col)
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: State): Int {
        if (childCount == 0 || dx == 0) {
            return 0
        }

        totalScroll += dx
        if (totalScroll < 0) {
            totalScroll = 0
        }

        val scrolled = if (dx < 0) {
            scrollLeft(dx, recycler)
        } else {
            scrollRight(dx, recycler)
        }

        return scrolled
    }

    private fun scrollLeft(dx: Int, recycler: Recycler): Int {
        var scrolled = 0
        val leftView = getChildAt(0)
        val leftPosition = getPosition(leftView!!)
        Log.d("TESTXXX", "leftPos: ${leftPosition}")
        if (totalScroll > 0) {
            val leftOffset = max(-dx, -leftView.left)
            scrolled -= leftOffset
            offsetChildrenHorizontal(leftOffset)
            fill(recycler)
        }
        return scrolled
    }

    private fun getTotalWidth(): Int {
        return (width/numColumns) * (itemCount/(numColumns*numRows))
    }

    private fun scrollRight(dx: Int, recycler: Recycler): Int {
        var scrolled = 0
        val rightView = getChildAt(childCount - 1)
        val rightPosition = getPosition(rightView!!)
        if (rightPosition < itemCount - 1) {
            val rightOffset = min(dx, getTotalWidth() - rightView.right)
            scrolled += rightOffset
            offsetChildrenHorizontal(rightOffset)
            fill(recycler)
        }
        return scrolled
    }

    override fun supportsPredictiveItemAnimations(): Boolean {
        return true
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: State, position: Int) {
        val smoothScroller = object : LinearSmoothScroller(recyclerView.context) {
            override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int {
                val out = IntArray(2)
                out[0] = view.left - paddingRight // Adjust for any padding
                return out[0]
            }
        }
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }
}
