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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.eneaceolini.mymaterialdesignapp.PetTrackerDatabase.Pets;
import com.eneaceolini.mymaterialdesignapp.PetTrackerDatabase.PetType;

import java.util.Locale;

/**
 * Created by Enea on 02/07/15.
 */
public class ListPetsFragment extends Fragment {


    private SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    private String asColumnsToReturn[] = {
            Pets.PETS_TABLE_NAME + "." + Pets.PET_NAME,
            Pets.PETS_TABLE_NAME + "." + Pets._ID,
            PetType.PETTYPE_TABLE_NAME + "." + PetType.PET_TYPE_NAME };
    private ListAdapter adapter = null;
    ListView mList;
    PetTrackerDatabaseHelper mDatabase;
    SQLiteDatabase db;
    Button fill,getStuff;
    EditText mName,mType;
    Cursor mCursor;

    public ListPetsFragment() {}

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_pet_tracker,container,false);

        mList = (ListView)v.findViewById(R.id.listView);
        mDatabase = new PetTrackerDatabaseHelper(getActivity().getApplicationContext());
        db = mDatabase.getWritableDatabase();
        fill = (Button)v.findViewById(R.id.fill);
        mName = (EditText)v.findViewById(R.id.editTextName);
        mType = (EditText)v.findViewById(R.id.editTextType);
        getStuff = (Button)v.findViewById(R.id.get);
        getStuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryBuilder = new SQLiteQueryBuilder();
                queryBuilder.setTables(Pets.PETS_TABLE_NAME + "," +
                PetType.PETTYPE_TABLE_NAME);
                queryBuilder.appendWhere(Pets.PETS_TABLE_NAME + "." + Pets.PET_TYPE_ID
                        + "=" + PetType.PETTYPE_TABLE_NAME + "." + PetType._ID);

                mCursor = queryBuilder.query(db,asColumnsToReturn,null,null,null,null,
                Pets.DEFAULT_SORT_ORDER);

                adapter = new SimpleCursorAdapter(v.getContext(), R.layout.list_item,
                        mCursor, new String[] { Pets.PET_NAME, PetType.PET_TYPE_NAME },
                        new int[] { R.id.firstLine, R.id.secondLine }, 1);

                mList.setAdapter(adapter);

            }
        });

        fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save new records
                db.beginTransaction();
                try {

                    // check if species type exists already
                    long rowId = 0;
                    String strPetType = mType.getText().toString()
                            .toLowerCase(Locale.getDefault());

                    // SQL Query
                    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
                    queryBuilder.setTables(PetType.PETTYPE_TABLE_NAME);
                    queryBuilder.appendWhere(PetType.PET_TYPE_NAME + "='"
                            + strPetType + "'");

                    // run the query since it's all ready to go
                    Cursor c = queryBuilder.query(db, null, null, null, null,
                            null, null);

                    if (c.getCount() == 0) {
                        // add the new type to our list
                        ContentValues typeRecordToAdd = new ContentValues();
                        typeRecordToAdd.put(PetType.PET_TYPE_NAME, strPetType);
                        rowId = db.insert(PetType.PETTYPE_TABLE_NAME,
                                PetType.PET_TYPE_NAME, typeRecordToAdd);

                        // Update autocomplete with new record

                    } else {
                        c.moveToFirst();
                        rowId = c.getLong(c.getColumnIndex(PetType._ID));
                    }

                    c.close();

                    // Always insert new pet records, even if the names clash
                    ContentValues petRecordToAdd = new ContentValues();
                    petRecordToAdd.put(Pets.PET_NAME, mName.getText().toString()
                            .toLowerCase(Locale.getDefault()));
                    petRecordToAdd.put(Pets.PET_TYPE_ID, rowId);
                    db.insert(Pets.PETS_TABLE_NAME, Pets.PET_NAME,
                            petRecordToAdd);

                    db.setTransactionSuccessful();
                    Log.d("DB", "Successful Transaction");
                } finally {
                    db.endTransaction();
                }


            }
        });



        return v;



    }

}
