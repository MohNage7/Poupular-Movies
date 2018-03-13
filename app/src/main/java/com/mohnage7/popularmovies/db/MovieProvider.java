package com.mohnage7.popularmovies.db;/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;

import static com.mohnage7.popularmovies.db.MovieContract.MovieEntry.TABLE_NAME;


public class MovieProvider extends ContentProvider {


    public static final int CODE_MOVIE = 100;
    public static final int CODE_ALL_MOVIES = 101;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MovieDbHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, CODE_ALL_MOVIES);

        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", CODE_MOVIE);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        // init db helper
        mOpenHelper = new MovieDbHelper(getContext());
        // return true to indicate the method is done
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        throw new RuntimeException("Student, you need to implement the bulkInsert mehtod!");
    }


    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIE: {
                // Get the id from the URI
                String id = uri.getPathSegments().get(1);

                // Selection is the _ID column = ?, and the Selection args = the row ID from the URI
                String mSelection = MovieContract.MovieEntry.COLUMN_ID + "=?";
                String[] mSelectionArgs = new String[]{id};

                // Construct a query as you would normally, passing in the selection/args
                cursor = mOpenHelper.getReadableDatabase().query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_ALL_MOVIES: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Sunshine.");
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // init db
        SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        // get matched code
        int match = sUriMatcher.match(uri);

        Uri returnedUri;
        switch (match) {
            case CODE_ALL_MOVIES:
                long id = sqLiteDatabase.insert(TABLE_NAME, null, values);
                if (id > 0)
                    returnedUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                else
                    throw new SQLiteException("Failed to add movie as favorite");
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri" + uri.toString());

        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnedUri;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;
        String normalizedUtcDateString = uri.getLastPathSegment();
        String[] selectionArguments = new String[]{normalizedUtcDateString};
        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIE:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_ID + " = ? ",
                        selectionArguments);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update in Sunshine");
    }


    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}