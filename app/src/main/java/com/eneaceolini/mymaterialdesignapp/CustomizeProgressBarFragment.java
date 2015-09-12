package com.eneaceolini.mymaterialdesignapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.eneaceolini.mymaterialdesignapp.TalksUsersDatabase.Talks;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CustomizeProgressBarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText series,speaker,from,title,abs,uid;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button notify;
    ContentResolver cr;
    NotificationCompat.Builder notifyBuilder;
    Intent toLaunch;
    Notification mNotification;
    NotificationManager notifier;
    private static final int NOTIFY_1 = 0x1001;




    public CustomizeProgressBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        notifyBuilder = new
                NotificationCompat.Builder(getActivity().getApplicationContext());

        notifyBuilder.setSmallIcon(R.drawable.ic_thumb);
        notifyBuilder.setTicker("Hello!");
        notifyBuilder.setWhen(System.currentTimeMillis()+1000);
        toLaunch = new Intent(getActivity(),MainActivity.class);
        notifyBuilder.setContentIntent(PendingIntent.getActivity(getActivity(), 0, toLaunch, 0));
        notifyBuilder.setContentTitle("Hi There!");
        notifyBuilder.setContentText("This is even more text");
        notifyBuilder.setAutoCancel(true);
        notifyBuilder.setVibrate(new long[]{0, 200,200,600,600});
        mNotification = notifyBuilder.build();
        notifier = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_customize_progressbar, container, false);
        notify = (Button)v.findViewById(R.id.notify);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notifier.notify(NOTIFY_1, mNotification);


            }
        });

        return v;
    }






}
