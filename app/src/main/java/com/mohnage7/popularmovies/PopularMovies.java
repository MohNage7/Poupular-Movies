package com.mohnage7.popularmovies;

import android.app.Application;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

/**
 * Created by mohnage7 on 3/2/2018.
 */

public class PopularMovies extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // build picasso client
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        Picasso.setSingletonInstance(built);
    }
}
