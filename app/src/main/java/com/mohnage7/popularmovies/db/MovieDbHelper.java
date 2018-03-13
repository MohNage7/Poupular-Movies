package com.mohnage7.popularmovies.db;/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Manages a local database for weather data.
 */
public class MovieDbHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our weather data.
         */
        final String SQL_CREATE_WEATHER_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                        MovieContract.MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COLUMN_TITLE + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_POSTER + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_COVER + " REAL NOT NULL, " +
                        MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL)";

        /*
         * execute SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
//        onCreate(sqLiteDatabase);
    }
}