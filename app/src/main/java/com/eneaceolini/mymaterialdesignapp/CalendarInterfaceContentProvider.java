package com.eneaceolini.mymaterialdesignapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Enea on 03/07/15.
 */
public class CalendarInterfaceContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.eneaceolini.mymaterialdesignapp."+
                                            "CalendarInterfaceContentProvider/calendar_provider");

    public final static String CALENDAR_TITLE = "calendar_title";
    public static final String CALENDAR_TIME = "calendar_time";
    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
