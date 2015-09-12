package com.eneaceolini.mymaterialdesignapp;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;

import java.sql.Date;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolBar;
    private static final String DEBUG_TAG = "MainActivity";
    private static final String TAG = "MainActivity";
    private static final String CREATE_AUTHOR_TABLE = "CREATE TABLE tbl_authors (id INTEGER PRIMARY KEY AUTOINCREMENT , firstname TEXT, lastname TEXT);";
    private static final String CREATE_BOOK_TABLE = "CREATE TABLE tbl_books (id INTEGER PRIMARY KEY AUTOINCREMENT , title TEXT, dateadded DATE,  authorid INTEGER NOT NULL CONSTRAINT authorid REFERENCES tbl_authors(id) ON DELETE CASCADE);";
    private static final String DROP_AUTHOR_TABLE = "DROP TABLE tbl_authors;";
    private static final String DROP_BOOK_TABLE = "DROP TABLE tbl_books;";

    private static final String CREATE_TRIGGER_ADD = "CREATE TRIGGER fk_insert_book BEFORE INSERT ON tbl_books FOR EACH ROW BEGIN  SELECT RAISE(ROLLBACK, 'insert on table \"tbl_books\" violates foreign key constraint \"fk_authorid\"') WHERE  (SELECT id FROM tbl_authors WHERE id = NEW.authorid) IS NULL; END;";
    private static final String CREATE_TRIGGER_UPDATE = "CREATE TRIGGER fk_update_book BEFORE UPDATE ON tbl_books FOR EACH ROW BEGIN SELECT RAISE(ROLLBACK, 'update on table \"tbl_books\" violates foreign key constraint \"fk_authorid\"') WHERE  (SELECT id FROM tbl_authors WHERE id = NEW.authorid) IS NULL; END;";
    private static final String CREATE_TRIGGER_DELETE = "CREATE TRIGGER fk_delete_author BEFORE DELETE ON tbl_authors FOR EACH ROW BEGIN SELECT RAISE(ROLLBACK, 'delete on table \"tbl_authors\" violates foreign key constraint \"fk_authorid\"') WHERE (SELECT authorid FROM tbl_books WHERE authorid = OLD.id) IS NOT NULL; END;";

    private static final String TABLE_BOOK = "tbl_books";
    private static final String TABLE_AUTHOR = "tbl_authors";

    final private String PACKAGE = "com.eneaceolini.mymaterialdesignapp";
    HashMap<ImageView, PictureData> mPicturesData = new HashMap<>();

    NotificationCompat.Builder notifyBuilder;
    Intent toLaunch;
    Notification mNotification;

    static float sAnimatorScale = 1;
    SQLiteDatabase mDataBase;

    private ImageFetcher mImageFetcher;


    String[] TITLES = {"Home","Events","Mail","Shop","Travel"};
    int[] ICONS = {R.drawable.ic_thumb,
            R.drawable.ic_thumb,
            R.drawable.ic_thumb,
            R.drawable.ic_thumb,
            R.drawable.ic_thumb,
            R.drawable.ic_thumb};

    String SETT[] = {"Settings"};
    String NAME = "Enea Ceolini";
    String EMAIL = "enea.ceolini@gmail.com";
    int PROFILE = R.drawable.enea;

    RecyclerView mRecyclerView;             // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;          // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager; // Declaring Layout Manager as a linear
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;

    private ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.container, new ImageGridFragment(), TAG);
            ft.commit();
        }

        toolBar = (Toolbar)findViewById(R.id.tool_bar);
        setSupportActionBar(toolBar);




        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);
        // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE,SETT,this);
        // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);
        // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);
        // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolBar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        //handle touch
        final GestureDetector mGestureDetector =
                new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());



                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();

                    switch(recyclerView.getChildAdapterPosition(child)) {
                        case 1:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //startActivity(new Intent(MainActivity.this, MySettings.class));
                                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.container, new ImageGridFragment(), TAG);
                                    ft.commit();
                                }
                            }, 350);

                            break;
                        case 2:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //startActivity(new Intent(MainActivity.this, MySettings.class));
                                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.container, new ListPetsFragment(), TAG);
                                    ft.commit();
                                }
                            }, 350);
                            break;
                        case 3:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //startActivity(new Intent(MainActivity.this, MySettings.class));
                                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.container, new CustomizeProgressBarFragment(), TAG);
                                    ft.commit();
                                }
                            }, 350);

                            break;
                        case 4:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //startActivity(new Intent(MainActivity.this, MySettings.class));
                                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.container, new AddExportTalk(), TAG);
                                    ft.commit();
                                }
                            }, 350);

                            break;
                        case 5:
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //startActivity(new Intent(MainActivity.this, MySettings.class));
                                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.container, new GameViewFragment(), TAG);
                                    ft.commit();
                                }
                            }, 350);

                            break;
                    }

                    if(recyclerView.getChildAdapterPosition(child)==2){


                    }
                    if(recyclerView.getChildAdapterPosition(child)==3){

                    }
                    return true;

                }


                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });


        //Loading photos

            //mGridLayout.addView(imageView);

        PetTrackerDatabaseHelper mDatabase = new PetTrackerDatabaseHelper(this.getApplicationContext());
        SQLiteDatabase db = mDatabase.getWritableDatabase();


        }



    private View.OnClickListener thumbnailClickListener = new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        // Interesting data to pass across are the thumbnail size/location, the
        // resourceId of the source bitmap, the picture description, and the
        // orientation (to avoid returning back to an obsolete configuration if
        // the device rotates again in the meantime)
        int[] screenLocation = new int[2];
        v.getLocationOnScreen(screenLocation);
        PictureData info = mPicturesData.get(v);
        Intent subActivity = new Intent(MainActivity.this,
                MoreInfoActivity.class);
        int orientation = getResources().getConfiguration().orientation;
        subActivity.
                putExtra(PACKAGE + ".orientation", orientation).
                putExtra(PACKAGE + ".resourceId", info.resourceId).
                putExtra(PACKAGE + ".left", screenLocation[0]).
                putExtra(PACKAGE + ".top", screenLocation[1]).
                putExtra(PACKAGE + ".width", v.getWidth()).
                putExtra(PACKAGE + ".height", v.getHeight()).
                putExtra(PACKAGE + ".description", info.description);
        startActivity(subActivity);
        Log.d("values","W="+v.getWidth()+" H="+v.getHeight());
        // Override transitions: we don't want the normal window animation in addition
        // to our custom one
        overridePendingTransition(0, 0);
    }
};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.action_search:
                //mActionMode = startActionMode(mActionModeCallback);

                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //CAB
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                //case R.id.menu_share:
                    //shareCurrentItem();
                    //mode.finish(); // Action picked, so close the CAB
                    //return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };


    private class ImageAdapter extends BaseAdapter{

        private final Context mContext;
        private int mItemHeight = 0;
        private int mNumColumns = 0;
        private int mToolBarHeight = 0;
        private GridView.LayoutParams mImageViewLayoutParams;

        public ImageAdapter(Context context) {
            super();
            mContext = context;
            mImageViewLayoutParams = new GridView.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

            TypedValue tv = new TypedValue();
            if (context.getTheme().resolveAttribute(
                    android.R.attr.actionBarSize, tv, true)) {
                mToolBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, context.getResources().getDisplayMetrics());
            }
        }


        @Override
        public int getCount() {
            if(getNumColumns() == 0){
                return 0;
            }
            return Images.imageThumbUrls.length + mNumColumns;
        }

        @Override
        public Object getItem(int position) {
            return position < mNumColumns ?
                    null : Images.imageThumbUrls[position - mNumColumns];
        }

        @Override
        public int getViewTypeCount(){
            return 2;
        }

        @Override
        public int getItemViewType(int position){
            return position < mNumColumns ? 1 : 0;
        }

        @Override
        public boolean hasStableIds(){
            return true;
        }

        @Override
        public long getItemId(int position) {
            return position < mNumColumns ? 0 : position - mNumColumns;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(position < mNumColumns){
                if (convertView == null){
                    convertView = new View(mContext);
                }
                convertView.setLayoutParams(new AbsListView.LayoutParams(
                        LayoutParams.MATCH_PARENT,mToolBarHeight));
                return convertView;
            }

            ImageView imageView;
            if (convertView == null){
                imageView = new RecyclingImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(mImageViewLayoutParams);
            } else {
                imageView = (ImageView) convertView;
            }

            if (imageView.getLayoutParams().height != mItemHeight){
                imageView.setLayoutParams(mImageViewLayoutParams);
            }

            mImageFetcher.loadImage(Images.imageThumbUrls[position - mNumColumns], imageView);
            return imageView;
        }


        public void setItemHeight(int height){
            if(mItemHeight == height){
                return;
            }
            mItemHeight = height;
            mImageViewLayoutParams = new GridView.LayoutParams(
                    LayoutParams.MATCH_PARENT,mItemHeight);
            mImageFetcher.setImageSize(height);
            notifyDataSetChanged();

        }

        public int getNumColumns(){
            return mNumColumns;
        }


        public void setmNumColumns(int numColumns){
            this.mNumColumns = numColumns;
        }
    }



}
