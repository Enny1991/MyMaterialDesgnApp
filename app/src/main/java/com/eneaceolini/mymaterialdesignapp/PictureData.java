package com.eneaceolini.mymaterialdesignapp;

/**
 * Created by Enea on 12/05/15.
 */

import android.graphics.Bitmap;

public class PictureData {
    int resourceId;
    String description;
    Bitmap thumbnail;

    public PictureData(int resourceId, String description, Bitmap thumbnail) {
        this.resourceId = resourceId;
        this.description = description;
        this.thumbnail = thumbnail;
    }
}