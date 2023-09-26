package com.juraj.testappaaa
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.ceil

class GridPagerSnapHelper(private val rows: Int, private val cols: Int) : SnapHelper() {

    private var recyclerView: RecyclerView? = null

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        val out = IntArray(2)
        val position = layoutManager.getPosition(targetView)

        val itemsPerPage = rows * cols
        val currentPage = position / itemsPerPage
        val currentStart = currentPage * itemsPerPage
        val currentEnd = currentStart + itemsPerPage

        if (position >= currentEnd) {
            // Scrolling forward to the next page
            out[0] = (currentEnd - position) * targetView.width
        } else if (position < currentStart) {
            // Scrolling backward to the previous page
            out[0] = (currentStart - position) * targetView.width
        } else {
            // On the current page
            out[0] = 0
        }

        out[1] = 0 // Vertical scroll is not supported in this example
        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager is GridHorizontalLayoutManager) {
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val itemsPerPage = rows * cols
            val currentPage = ceil(firstVisibleItemPosition.toDouble() / itemsPerPage).toInt()
            return layoutManager.findViewByPosition(currentPage * itemsPerPage)
        }
        return null
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager,
        velocityX: Int,
        velocityY: Int
    ): Int {
        if (layoutManager !is GridHorizontalLayoutManager) {
            return RecyclerView.NO_POSITION
        }

        val currentView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
        val currentPosition = layoutManager.getPosition(currentView)
        val itemsPerPage = rows * cols

        return if (velocityX > 0) {
            // Scrolling forward
            val nextPage = (currentPosition / itemsPerPage + 1) * itemsPerPage
            min(nextPage, layoutManager.itemCount - 1)
        } else if (velocityX < 0) {
            // Scrolling backward
            val prevPage = (currentPosition / itemsPerPage - 1) * itemsPerPage
            max(prevPage, 0)
        } else {
            // No velocity, just snap to the nearest page
            val currentPage = currentPosition / itemsPerPage
            val currentStart = currentPage * itemsPerPage
            val currentEnd = currentStart + itemsPerPage

            if (currentPosition < currentStart + itemsPerPage / 2) {
                currentStart
            } else {
                currentEnd
            }
        }
    }
}
