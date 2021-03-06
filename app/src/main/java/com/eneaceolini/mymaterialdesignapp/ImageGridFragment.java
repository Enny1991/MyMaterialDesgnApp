package com.eneaceolini.mymaterialdesignapp;



import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * The main fragment that powers the ImageGridActivity screen. Fairly straight forward GridView
 * implementation with the key addition being the ImageWorker class w/ImageCache to load children
 * asynchronously, keeping the UI nice and smooth and caching thumbnails for quick retrieval. The
 * cache is retained over configuration changes like orientation change so the images are populated
 * quickly if, for example, the user rotates the device.
 */
public class ImageGridFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String TAG = "ImageGridFragment";
    private static final String IMAGE_CACHE_DIR = "thumbs";
    final private String PACKAGE = "com.eneaceolini.mymaterialdesignapp";


    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private ImageAdapter mAdapter;
    private ImageFetcher mImageFetcher;

    /**
     * Empty constructor as per the Fragment documentation
     */
    public ImageGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        mAdapter = new ImageAdapter(getActivity());

        ImageCache.ImageCacheParams cacheParams =
                new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);

        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
        mImageFetcher.setLoadingImage(R.drawable.empty_photo);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.image_grid_fragment, container, false);
        final GridView mGridView = (GridView) v.findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                // Pause fetcher to ensure smoother scrolling when flinging
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Before Honeycomb pause image loading on scroll to help with performance
                    if (!Utils.hasHoneycomb()) {
                        mImageFetcher.setPauseWork(true);
                    }
                } else {
                    mImageFetcher.setPauseWork(false);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

        // This listener is used to get the final width of the GridView and then calculate the
        // number of columns and the width of each column. The width of each column is variable
        // as the GridView has stretchMode=columnWidth. The column width is used to set the height
        // of each view so we get nice square thumbnails.
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @TargetApi(VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        if (mAdapter.getNumColumns() == 0) {
                            final int numColumns = (int) Math.floor(
                                    mGridView.getWidth() / (mImageThumbSize + mImageThumbSpacing));
                            if (numColumns > 0) {
                                final int columnWidth =
                                        (mGridView.getWidth() / numColumns) - mImageThumbSpacing;
                                mAdapter.setNumColumns(numColumns);
                                mAdapter.setItemHeight(columnWidth);
                                if (BuildConfig.DEBUG) {
                                    Log.d(TAG, "onCreateView - numColumns set to " + numColumns);
                                }
                                if (Utils.hasJellyBean()) {
                                    mGridView.getViewTreeObserver()
                                            .removeOnGlobalLayoutListener(this);
                                } else {
                                    mGridView.getViewTreeObserver()
                                            .removeGlobalOnLayoutListener(this);
                                }
                            }
                        }
                    }
                });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

        // Interesting data to pass across are the thumbnail size/location, the
        // resourceId of the source bitmap, the picture description, and the
        // orientation (to avoid returning back to an obsolete configuration if
        // the device rotates again in the meantime)
        int[] screenLocation = new int[2];
        v.getLocationOnScreen(screenLocation);
        //PictureData info = mPicturesData.get(v);
        Intent subActivity = new Intent(getActivity(),
                MoreInfoActivity.class);
        int orientation = getResources().getConfiguration().orientation;
        subActivity.
                putExtra(PACKAGE + ".orientation", orientation).
                putExtra(PACKAGE + ".resourceId", Images.imageUrls[(int) id]).
                putExtra(PACKAGE + ".left", screenLocation[0]).
                putExtra(PACKAGE + ".top", screenLocation[1]).
                putExtra(PACKAGE + ".width", v.getWidth()).
                putExtra(PACKAGE + ".height", v.getHeight());
                //putExtra(PACKAGE + ".description", info.description);
        startActivity(subActivity);
        //Log.d("values","W="+v.getWidth()+" H="+v.getHeight());
        // Override transitions: we don't want the normal window animation in addition
        // to our custom one
        getActivity().overridePendingTransition(0, 0);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
            case R.id.clear_cache:
                mImageFetcher.clearCache();
                Toast.makeText(getActivity(), R.string.clear_cache_complete_toast,
                        Toast.LENGTH_SHORT).show();
                return true;
                */
        }

            return super.onOptionsItemSelected(item);
        }

        /**
         * The main adapter that backs the GridView. This is fairly standard except the number of
         * columns in the GridView is used to create a fake top row of empty views as we use a
         * transparent ActionBar and don't want the real top row of images to start off covered by it.
         */
        private class ImageAdapter extends BaseAdapter {

            private final Context mContext;
            private int mItemHeight = 0;
            private int mNumColumns = 0;
            private int mActionBarHeight = 0;
            private GridView.LayoutParams mImageViewLayoutParams;

            public ImageAdapter(Context context) {
                super();
                mContext = context;
                mImageViewLayoutParams = new GridView.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                // Calculate ActionBar height

            }

            @Override
            public int getCount() {
                // If columns have yet to be determined, return no items

                // Size + number of columns for top empty row
                return Images.imageThumbUrls.length;
            }

            @Override
            public Object getItem(int position) {
                return Images.imageThumbUrls[position ];
            }

            @Override
            public long getItemId(int position) {
                return position < mNumColumns ? 0 : position - mNumColumns;
            }

            @Override
            public int getViewTypeCount() {
                // Two types of views, the normal ImageView and the top row of empty views
                return 2;
            }

            @Override
            public int getItemViewType(int position) {
                return (position < mNumColumns) ? 1 : 0;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup container) {
                //BEGIN_INCLUDE(load_gridview_item)
                // First check if this is the top row
                if (position < mNumColumns) {
                    if (convertView == null) {
                        convertView = new View(mContext);
                    }
                    // Set empty view with height of ActionBar
                    convertView.setLayoutParams(new AbsListView.LayoutParams(
                            LayoutParams.MATCH_PARENT, mActionBarHeight));
                    return convertView;
                }

                // Now handle the main ImageView thumbnails
                ImageView imageView;
                if (convertView == null) { // if it's not recycled, instantiate and initialize
                    imageView = new RecyclingImageView(mContext);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setLayoutParams(mImageViewLayoutParams);
                } else { // Otherwise re-use the converted view
                    imageView = (ImageView) convertView;
                }

                // Check the height matches our calculated column width
                if (imageView.getLayoutParams().height != mItemHeight) {
                    imageView.setLayoutParams(mImageViewLayoutParams);
                }

                // Finally load the image asynchronously into the ImageView, this also takes care of
                // setting a placeholder image while the background thread runs
                mImageFetcher.loadImage(Images.imageThumbUrls[position - mNumColumns], imageView);
                return imageView;
                //END_INCLUDE(load_gridview_item)
            }

            /**
             * Sets the item height. Useful for when we know the column width so the height can be set
             * to match.
             *
             * @param height
             */
            public void setItemHeight(int height) {
                if (height == mItemHeight) {
                    return;
                }
                mItemHeight = height;
                mImageViewLayoutParams =
                        new GridView.LayoutParams(LayoutParams.MATCH_PARENT, mItemHeight);
                mImageFetcher.setImageSize(height);
                notifyDataSetChanged();
            }

            public void setNumColumns(int numColumns) {
                mNumColumns = numColumns;
            }

            public int getNumColumns() {
                return mNumColumns;
            }
        }

    private View.OnClickListener thumbnailClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    }


