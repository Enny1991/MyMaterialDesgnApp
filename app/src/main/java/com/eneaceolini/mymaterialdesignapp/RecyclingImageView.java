package com.eneaceolini.mymaterialdesignapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Enea on 24/06/15.
 */
public class RecyclingImageView extends ImageView {

    public RecyclingImageView(Context context){
        super(context);
    }

    public RecyclingImageView(Context context, AttributeSet attr){
        super(context, attr);
    }

    @Override
    protected void onDetachedFromWindow(){
        setImageDrawable(null);

        super.onDetachedFromWindow();
    }

    @Override
    public void setImageDrawable(Drawable drawable){
        final Drawable previousDrawable = getDrawable();

        super.setImageDrawable(drawable);

        notifyDrawable(drawable, true);

        notifyDrawable(previousDrawable, false);
    }

    private static void notifyDrawable(Drawable drawable, final boolean isDisplayed){
        if(drawable instanceof RecyclingBitmapDrawable){
            ((RecyclingBitmapDrawable) drawable).setIsDisplayed(isDisplayed);
        } else if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            for(int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++){
                notifyDrawable(layerDrawable.getDrawable(i),isDisplayed);
            }
        }

    }
}
