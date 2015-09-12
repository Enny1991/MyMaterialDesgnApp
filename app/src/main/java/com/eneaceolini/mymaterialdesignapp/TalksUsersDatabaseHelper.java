package com.eneaceolini.mymaterialdesignapp;

import com.eneaceolini.mymaterialdesignapp.TalksUsersDatabase.Talks;
import com.eneaceolini.mymaterialdesignapp.TalksUsersDatabase.Users;
import com.eneaceolini.mymaterialdesignapp.TalksUsersDatabase.TalkUser;
import com.eneaceolini.mymaterialdesignapp.TalksUsersDatabase.TagType;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;

// This class handles the creation and versioning of the application database

class TalksUsersDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "talks_users.db";
    private static final int DATABASE_VERSION = 1;

    TalksUsersDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the Talks table
        db.execSQL("CREATE TABLE " + Talks.TALKS_TABLE_NAME+ " ("
                + Talks._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Talks.TALK_ABSTRACT + " TEXT ,"
                + Talks.TALK_DATE + " DATE ,"
                + Talks.TALK_SERIES + " TEXT ,"
                + Talks.TALK_SPEAKER_FROM + " TEXT ,"
                + Talks.TALK_SPEAKER_NAME + " TEXT ,"
                + Talks.TALK_TITLE + " TEXT ,"
                + Talks.TALK_UID + " INTEGER ,"
                + Talks.TALK_URL + " TEXT "
                + ");");

        // Create Table Users
        db.execSQL("CREATE TABLE " + Users.USERS_TABLE_NAME+ " ("
                + Users._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Users.USER_EMAIL + " TEXT ,"
                + Users.USER_PASSWORD + " TEXT ,"
                + Users.USER_LANGUAGE + " TEXT ,"
                + Users.USER_LOCATION + " TEXT "
                + ");");

        db.execSQL("CREATE TABLE " + TagType.TAGTYPE_TABLE_NAME+ " ("
                + TagType._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + TagType.TAGTYPE_TAGTYPE + " TEXT ,"
                + TagType.TAGTYPE_USER_ID + " INTEGER "
                + ");");

        db.execSQL("CREATE TABLE " + TalkUser.TALKUSER_TABLE_NAME+ " ("
                + TalkUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + TalkUser.TALKUSER_TALK_ID + " INTEGER ,"
                + TalkUser.TALKUSER_USER_ID + " INTEGER "
                + ");");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Housekeeping here.
        // Implement how "move" your application data during an upgrade of
        // schema versions
        // There is no ALTER TABLE command in SQLite, so this generally involves
        // CREATING a new table, moving data if possible, or deleting the old
        // data and starting fresh
        // Your call.
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
