package com.eneaceolini.mymaterialdesignapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

/**
 * Created by Enea on 24/06/15.
 */
public class RecyclingBitmapDrawable extends BitmapDrawable {

    static final String TAG = "CountingBitmapDrawable";

    private int mCacheRefCount = 0;
    private int mDisplayRefCount = 0;

    private boolean mHasBeenDisplayed;

    public RecyclingBitmapDrawable(Resources res, Bitmap bitmap){
        super(res, bitmap);
    }

    public void setIsDisplayed(boolean isDisplayed){
        synchronized(this){
            if (isDisplayed) {
                mDisplayRefCount++;
                mHasBeenDisplayed = true;
            } else {
                mDisplayRefCount--;
            }
        }
        checkState();
    }

    public void setIsCached(boolean isCached){
        synchronized(this){
            if (isCached) {
                mCacheRefCount++;
            } else {
                mCacheRefCount--;
            }
        }
        checkState();
    }

    private synchronized void checkState() {


        if (mCacheRefCount <= 0 && mDisplayRefCount <= 0 && mHasBeenDisplayed
                && hasValidBitmap()) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "No longer being used or cached so recycling. "
                        + toString());
            }

            getBitmap().recycle();
        }
        //END_INCLUDE(check_state)
    }

    public synchronized boolean hasValidBitmap() {
        Bitmap bitmap = getBitmap();
        return bitmap != null && !bitmap.isRecycled();
    }
}
