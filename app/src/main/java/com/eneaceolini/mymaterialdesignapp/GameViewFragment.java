package com.eneaceolini.mymaterialdesignapp;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.eneaceolini.mymaterialdesignapp.PetTrackerDatabase.Pets;
import com.eneaceolini.mymaterialdesignapp.PetTrackerDatabase.PetType;

import java.util.Locale;

/**
 * Created by Enea on 02/07/15.
 */
public class GameViewFragment extends Fragment {



    public GameViewFragment() {}

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_simple_frame,container,false);
        GameView image = new GameView(getActivity().getApplicationContext(),R.drawable.enea);
        FrameLayout frame = (FrameLayout)v.findViewById(R.id.mainFrame);
        frame.addView(image);


        return v;



    }

}
