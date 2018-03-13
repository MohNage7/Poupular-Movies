package com.mohnage7.popularmovies.movies.view;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.mohnage7.popularmovies.db.MovieContract;

import java.lang.ref.WeakReference;

/**
 * Created by mohnage7 on 3/13/2018.
 */

public class MoviesAsyncTaskLoader extends AsyncTaskLoader<Cursor> {
    // Initialize a Cursor, this will hold all the task data
    Cursor mTaskData = null;
    private WeakReference<MoviesActivity> activityReference;

    public MoviesAsyncTaskLoader(@NonNull MoviesActivity context) {
        super(context);
        activityReference = new WeakReference<>(context);
    }

    // onStartLoading() is called when a loader first starts loading data
    @Override
    protected void onStartLoading() {
        // show loading dialog
        activityReference.get().loadingIndicatorView.smoothToShow();

        if (mTaskData != null) {
            // Delivers any previously loaded data immediately
            deliverResult(mTaskData);
        } else {
            // Force a new load
            forceLoad();
        }
    }

    @Nullable
    @Override
    public Cursor loadInBackground() {
        // Will implement to load data
        // COMPLETED (5) Query and load all task data in the background; sort by priority
        // [Hint] use a try/catch block to catch any errors in loading data

        try {
            return activityReference.get().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

        } catch (Exception e) {
            Log.e("Exception", "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }
    }

    // deliverResult sends the result of the load, a Cursor, to the registered listener
    public void deliverResult(Cursor data) {
        mTaskData = data;
        super.deliverResult(data);
    }
}
