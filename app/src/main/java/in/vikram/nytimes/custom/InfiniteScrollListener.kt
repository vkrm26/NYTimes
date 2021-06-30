package `in`.vikram.nytimes.custom

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfiniteScrollListener ( val func: () -> Unit, val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    var previousTotal = 0
    var loading = true
    var visibleThreshold = 10
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy > 0) {
            visibleItemCount = recyclerView!!.childCount
            totalItemCount = layoutManager.itemCount
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }

            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                func()
                loading = true
            }
        }
    }

    fun setLoadingFlag(loading : Boolean) {
        this.loading = loading
    }

    fun reset() {
        previousTotal = 0
        loading = true
        visibleThreshold = 2
        firstVisibleItem = 0
        visibleItemCount = 0
        totalItemCount = 0
    }
}