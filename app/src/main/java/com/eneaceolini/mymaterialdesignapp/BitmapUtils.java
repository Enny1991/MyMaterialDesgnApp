package com.eneaceolini.mymaterialdesignapp;

/**
 * Created by Enea on 12/05/15.
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

public class BitmapUtils {

    int[] mPhotos = {
            R.drawable.enea

    };

    String[] mDescriptions = {
            "This picture was taken while sunbathing in a natural hot spring, which was " +
                    "unfortunately filled with acid, which is a lasting memory from that trip, whenever I " +
                    "I look at my own skin.",
            "I took this shot with a pinhole camera mounted on a tripod constructed out of " +
                    "soda straws. I felt that that combination best captured the beauty of the landscape " +
                    "in juxtaposition with the detritus of mankind.",
            "I don't remember where or when I took this picture. All I know is that I was really " +
                    "drunk at the time, and I woke up without my left sock.",
            "Right before I took this picture, there was a busload of school children right " +
                    "in my way. I knew the perfect shot was coming, so I quickly yelled 'Free candy!!!' " +
                    "and they scattered.",
    };

    static HashMap<Integer, Bitmap> sBitmapResourceMap = new HashMap<Integer, Bitmap>();

    /**
     * Load pictures and descriptions. A real app wouldn't do it this way, but that's
     * not the point of this animation demo. Loading asynchronously is a better way to go
     * for what can be time-consuming operations.
     */
    public ArrayList<PictureData> loadPhotos(Resources resources) {
        ArrayList<PictureData> pictures = new ArrayList<PictureData>();
        for (int i = 0; i < 30; ++i) {
            int resourceId = mPhotos[(int) (Math.random() * mPhotos.length)];
            Bitmap bitmap = getBitmap(resources, resourceId);
            Bitmap thumbnail = getThumbnail(bitmap, 200);
            String description = mDescriptions[(int) (Math.random() * mDescriptions.length)];
            pictures.add(new PictureData(resourceId, description, thumbnail));
        }
        return pictures;
    }

    /**
     * Utility method to get bitmap from cache or, if not there, load it
     * from its resource.
     */
    static Bitmap getBitmap(Resources resources, int resourceId) {
        Bitmap bitmap = sBitmapResourceMap.get(resourceId);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(resources, resourceId);
            sBitmapResourceMap.put(resourceId, bitmap);
        }
        return bitmap;
    }

    /**
     * Create and return a thumbnail image given the original source bitmap and a max
     * dimension (width or height).
     */
    private Bitmap getThumbnail(Bitmap original, int maxDimension) {
        int width = original.getWidth();
        int height = original.getHeight();
        int scaledWidth, scaledHeight;

        if (width >= height) {
            float scaleFactor = (float) maxDimension / width;
            scaledWidth = 200;
            scaledHeight = (int) (scaleFactor * height);
        } else {
            float scaleFactor = (float) maxDimension / height;
            scaledWidth = (int) (scaleFactor * width);
            scaledHeight = 200;
        }

        Bitmap thumbnail = Bitmap.createScaledBitmap(original, scaledWidth, scaledHeight, true);

        return thumbnail;
    }


}
