package com.example.jeffhsu.gridimagesearch.Listeners;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.jeffhsu.gridimagesearch.activities.SearchActivity;

/**
 * Created by jeffhsu on 2/1/15.
 */
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;

    // The current offset index of data you have loaded
    private int currentPage = 0;

    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;

    // True if we are still waiting for the last set of data to load
    private boolean loading = true;

    // Sets the starting page index
    private int startingPageIndex = 0;

    public EndlessScrollListener() {
    }

    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int startingPageIndex) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startingPageIndex;
        this.currentPage = startingPageIndex;
    }

    // This happens many times a second during a scroll, so be wary of the code you put here
    // We are given a few useful parameters to help us work out if we need to load some more
    // data, but first we check if we are waiting for the previous Load to finish
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.i("DEBUG", "We are scrolling now");
        Log.i("DEBUG", "currentpage is "+currentPage);
        Log.i("DEBUG", "totalItemCount is "+totalItemCount);
        Log.i("DEBUG", "previousTotalItemCount is "+previousTotalItemCount);
        Log.i("DEBUG", "visibleThreshold is "+visibleThreshold);
        Log.i("DEBUG", "visibleItemCount is "+visibleItemCount);
        Log.i("DEBUG", "firstVisibleItem is "+firstVisibleItem);
        Log.i("DEBUG", "loading is "+loading);
        // If the total item count is zero and the previous isn't, assume the list is invalidated
        // and should be reset back to initial state
        if (totalItemCount < previousTotalItemCount) {
            Log.i("DEBUG", "I am resetting!!!");
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = 0;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        // If it's still loading, we check to see if the dataset count has changed,
        // if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            Log.i("DEBUG","Setting loading to false");
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // If it isn't currently Loading, we check to see if we have breached the
        // visibleThreshold and need to reload more data.  If we do need to reload
        // some more data, we execute onLoadMore to fetch the data

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            onLoadMore(currentPage + 1, totalItemCount);
            loading = true;
        }

    }

    public abstract void onLoadMore(int page, int totalItemCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }


}
