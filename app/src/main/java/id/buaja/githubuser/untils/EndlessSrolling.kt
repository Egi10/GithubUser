package id.buaja.githubuser.untils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessSrolling : RecyclerView.OnScrollListener() {
    private var mPreviousTotal: Int = 0
    private var mLoading: Boolean = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = recyclerView.layoutManager?.itemCount
        val firstVisibleItem = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

        if (mLoading) {
            totalItemCount?.let {
                if (it > mPreviousTotal) {
                    mLoading = false
                    mPreviousTotal = it
                }
            }
        }

        val visibleThresold = 5
        totalItemCount?.let {
            if (!mLoading && (it - visibleItemCount) <= (firstVisibleItem + visibleThresold)) {
                onLoadMore()
                mLoading = true
            }
        }
    }

    abstract fun onLoadMore()
}