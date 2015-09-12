package com.eneaceolini.mymaterialdesignapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Enea on 15/07/15.
 */
public class GameView extends View {


    private static final String DEBUG_TAG = "SimpleGesture->GameView";

    private GestureDetector gestures;
    private ScaleGestureDetector multiGestures;
    private Matrix translate;
    private Matrix scale;
    private Bitmap droid;
    private int deg = 0;

    private Matrix animateStart;
    private Interpolator animateInterpolator;
    private long startTime;
    private long endTime;
    private float totalAnimDx;
    private float totalAnimDy;

    public GameView(Context context, int iGraphicResourceId) {
        super(context);
        translate = new Matrix();
        scale = new Matrix();
        GestureListener listener = new GestureListener(this);
        ScaleGestureListener scaleListener = new ScaleGestureListener(this);
        gestures = new GestureDetector(context, listener, null, true);
        multiGestures = new ScaleGestureDetector(context, scaleListener);
        droid = BitmapFactory.decodeResource(getResources(), iGraphicResourceId);
    }

    public void onAnimateMove(float dx, float dy, long duration) {
        animateStart = new Matrix(translate);
        animateInterpolator = new OvershootInterpolator();
        startTime = System.currentTimeMillis();
        endTime = startTime + duration;
        totalAnimDx = dx;
        totalAnimDy = dy;
        post(new Runnable() {
            @Override
            public void run() {
                onAnimateStep();
            }
        });
    }

    private void onAnimateStep() {
        long curTime = System.currentTimeMillis();
        float percentTime = (float) (curTime - startTime)
                / (float) (endTime - startTime);
        float percentDistance = animateInterpolator
                .getInterpolation(percentTime);
        float curDx = percentDistance * totalAnimDx;
        float curDy = percentDistance * totalAnimDy;
        translate.set(animateStart);
        onMove(curDx, curDy);

        if (percentTime < 1.0f) {
            post(new Runnable() {
                @Override
                public void run() {
                    onAnimateStep();
                }
            });
        }
    }

    public void onScale(float factor) {
        scale.preScale(factor, factor);
        invalidate();
    }

    public void onMove(float dx, float dy) {
        translate.postTranslate(dx, dy);
        invalidate();
    }

    public void onResetLocation() {
        translate.reset();
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.v(DEBUG_TAG, "onDraw");
        Matrix transform = new Matrix(scale);
        float width = droid.getWidth() / 2;
        float height = droid.getHeight() / 2;
        transform.postTranslate(-width, -height);
        transform.postConcat(scale);
        transform.postTranslate(width, height);
        transform.postConcat(translate);
        //transform.postRotate(deg++,0,0);
        canvas.drawBitmap(droid, transform, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d("# Pointers",""+event.getPointerCount());
        if(event.getPointerCount()>1){
            int mActivePointerId = event.getPointerId(0);
            int pointerIndex = event.findPointerIndex(mActivePointerId);
            // Get the pointer's current position
            float x1 = event.getX(pointerIndex);
            float y1 = event.getY(pointerIndex);
            int mActivePointerId2 = event.getPointerId(1);
            int pointerIndex2 = event.findPointerIndex(mActivePointerId2);
            // Get the pointer's current position
            float x2 = event.getX(pointerIndex2);
            float y2 = event.getY(pointerIndex2);
            Log.d("one",""+x1+" "+y1);
            Log.d("two",""+x2+" "+y2);
        }
        boolean retVal = false;
        retVal = gestures.onTouchEvent(event);
        retVal = multiGestures.onTouchEvent(event);

        return retVal;
    }

    private class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        GameView view;

        public ScaleGestureListener(GameView view) {
            this.view = view;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.v(DEBUG_TAG, "onScale");
            float scale = detector.getScaleFactor();
            view.onScale(scale);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }

    private class GestureListener extends
            GestureDetector.SimpleOnGestureListener  {

        GameView view;

        public GestureListener(GameView view) {
            this.view = view;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.v(DEBUG_TAG, "onDown");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               final float velocityX, final float velocityY) {
            Log.v(DEBUG_TAG, "onFling");
            final float distanceTimeFactor = 0.4f;
            final float totalDx = (distanceTimeFactor * velocityX / 2);
            final float totalDy = (distanceTimeFactor * velocityY / 2);

            view.onAnimateMove(totalDx, totalDy,
                    (long) (1000 * distanceTimeFactor));
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.v(DEBUG_TAG, "onDoubleTap");
            view.onResetLocation();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            //Log.v(DEBUG_TAG, "onScroll");

            view.onMove(-distanceX, -distanceY);

            return true;
        }
    }
}
