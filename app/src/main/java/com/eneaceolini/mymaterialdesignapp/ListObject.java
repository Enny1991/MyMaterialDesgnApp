package com.eneaceolini.mymaterialdesignapp;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Enea on 14/05/15.
 */
public class ListObject {
    private int id;
    private String text;
    private Bitmap iconBitmap;
    private Drawable iconDrawable;
    //...

    public ListObject(int id, String text, Bitmap icon){
        this.id = id;
        this.text = text;
        this.iconBitmap = icon;
    }

    public ListObject(int id, String text, Drawable icon){
        this.id = id;
        this.text = text;
        this.iconDrawable = icon;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setText(String text){
        this.text = text;
    }
    public void setIconBitmap(Bitmap icon){
        this.iconBitmap = icon;
    }
    public void setIconDrawable(Drawable icon){
        this.iconDrawable = icon;
    }

    public int getId(){
        return id;
    }
    public String getText(){
        return text;
    }
    public Bitmap getIconBitmap(){
        return iconBitmap;
    }
    public Drawable getIconDrawable(){
        return iconDrawable;
    }
}
