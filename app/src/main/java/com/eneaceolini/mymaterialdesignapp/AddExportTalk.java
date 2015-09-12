package com.eneaceolini.mymaterialdesignapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.provider.CalendarContract.Events;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.eneaceolini.mymaterialdesignapp.TalksUsersDatabase.Talks;



public class AddExportTalk extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText series,speaker,from,title,abs,uid;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button add,export;
    ContentResolver cr;

    SQLiteDatabase db;
    TalksUsersDatabaseHelper dbHelper;




    public AddExportTalk() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new TalksUsersDatabaseHelper(getActivity().getApplicationContext());
        db = dbHelper.getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_add_export_talk, container, false);
        cr = getActivity().getContentResolver();
        series = (EditText)v.findViewById(R.id.series);
        speaker = (EditText)v.findViewById(R.id.speaker);
        from = (EditText)v.findViewById(R.id.from);
        uid = (EditText)v.findViewById(R.id.uid);
        abs = (EditText)v.findViewById(R.id.abs);
        title = (EditText)v.findViewById(R.id.title);
        add = (Button)v.findViewById(R.id.add);
        export = (Button)v.findViewById(R.id.export);
        datePicker = (DatePicker)v.findViewById(R.id.datePicker);
        timePicker = (TimePicker)v.findViewById(R.id.timePicker);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(Talks.TALK_ABSTRACT,abs.getText()
                        .toString().toLowerCase(Locale.getDefault()));
                cv.put(Talks.TALK_SERIES,series.getText()
                        .toString().toLowerCase(Locale.getDefault()));
                cv.put(Talks.TALK_SPEAKER_NAME,speaker.getText()
                        .toString().toLowerCase(Locale.getDefault()));
                cv.put(Talks.TALK_SPEAKER_FROM,from.getText()
                        .toString().toLowerCase(Locale.getDefault()));
                cv.put(Talks.TALK_TITLE,title.getText()
                        .toString().toLowerCase(Locale.getDefault()));
                cv.put(Talks.TALK_UID, Integer.parseInt(uid.getText()
                        .toString()));
                cv.put(Talks.TALK_URL,"noURL");

                Date d = getDateFromDatePicket(datePicker,timePicker);

                cv.put(Talks.TALK_DATE,""+d.getTime());

                db.beginTransaction();
                try{
                    long id = db.insertOrThrow(Talks.TALKS_TABLE_NAME,null,cv);

                    db.setTransactionSuccessful();
                    Log.d("SQLite","Successfully insert in "+id);
                }catch(Exception e){
                    Log.w("SQLite", e.toString());
                }finally{
                    db.endTransaction();
                }

            }
        });

        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Cursor c = db.query(Talks.TALKS_TABLE_NAME,null,Talks.TALK_TITLE + "=?",
                        new String[]{title.getText().toString().toLowerCase(Locale.getDefault())},
                        null,null,null);
                String title = "";
                Date d = null;
                c.moveToFirst();

                    title = c.getString(6);
                    Log.d("Time",c.getString(2));
                    //d = new Date(Long.parseLong(c.getString(2)));

                //d = new Date(Long.parseLong(c.getString(2)));

                //Log.d("Result",title + " "+d);


                //Add to calendar

                long calID = insertCalendar();
                long startMillis = Long.parseLong(c.getString(2));
                long endMillis = Long.parseLong(c.getString(2)) + 3600000;



                ContentValues values = new ContentValues();
                values.put(Events.DTSTART, startMillis);
                values.put(Events.DTEND, endMillis);
                values.put(Events.TITLE, title);
                values.put(Events.DESCRIPTION, "Group workout");
                values.put(Events.CALENDAR_ID, calID);
                values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
                Uri uri = cr.insert(Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
                long eventID = Long.parseLong(uri.getLastPathSegment());
                Toast.makeText(getActivity().getApplicationContext(),"Event successfully inserted",Toast.LENGTH_SHORT).show();
                Log.d("Calendar Insertion",""+eventID);
                //
                */
                deleteCalendar();

            }
        });

        // Inflate the layout for this fragment
        return v;
    }


    public void deleteCalendar() {


// The new display name for the calendar
        Uri calUri = CalendarContract.Calendars.CONTENT_URI;

        int rows = cr.delete(calUri,CalendarContract.Calendars.CALENDAR_DISPLAY_NAME+"=?",new String[]{"talks"});
        Log.d("deleted","row "+rows);
    }

    public long insertCalendar() {

        Uri calUri = CalendarContract.Calendars.CONTENT_URI;
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, "DUMMY");
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, "talks");
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "talks");
        cv.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.RED);
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, true);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        //cv.put(CalendarContract.Calendars.CAL_SYNC1, eventType.getId());

        calUri = calUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "DUMMY")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();

        Uri result = cr.insert(calUri, cv);
        return Long.parseLong(result.getLastPathSegment());
    }

    public static java.util.Date getDateFromDatePicket(DatePicker datePicker,TimePicker timePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();
        int hour = timePicker.getCurrentHour();
        int minutes = timePicker.getCurrentMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minutes);


        return calendar.getTime();
    }






}
