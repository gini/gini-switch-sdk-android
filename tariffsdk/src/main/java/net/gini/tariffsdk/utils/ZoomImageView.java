package net.gini.tariffsdk.utils;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class ZoomImageView extends android.support.v7.widget.AppCompatImageView {

    private static final int MAX_DIFF_FOR_CLICK = 3;
    private static final float MAX_SCALE = 4f;
    private static final float MIN_SCALE = 1f;
    private Matrix mMatrix;
    private Mode mMode = Mode.NONE;
    private float mRight, mBottom, mOriginalBitmapWidth, mOriginalBitmapHeight;
    private float mSaveScale = 1f;
    private ScaleGestureDetector mScaleDetector;
    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int bmHeight = getBitmapHeight();
        int bmWidth = getBitmapWidth();

        float width = getMeasuredWidth();
        float height = getMeasuredHeight();
        //Fit to screen.
        float scale = width > height ? (height / bmHeight) : (width / bmWidth);

        mMatrix.setScale(scale, scale);
        mSaveScale = 1f;

        mOriginalBitmapWidth = scale * bmWidth;
        mOriginalBitmapHeight = scale * bmHeight;

        // Center the image
        final float redundantYSpace = (height - mOriginalBitmapHeight);
        final float redundantXSpace = (width - mOriginalBitmapWidth);

        mMatrix.postTranslate(redundantXSpace / 2, redundantYSpace / 2);

        setImageMatrix(mMatrix);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);

        final float[] values = new float[9];
        mMatrix.getValues(values);
        float x = values[Matrix.MTRANS_X];
        float y = values[Matrix.MTRANS_Y];

        final PointF last = new PointF();
        final PointF curr = new PointF(event.getX(), event.getY());
        final PointF start = new PointF();

        switch (event.getAction()) {
            //when one finger is touching
            //set the mMode to DRAG
            case MotionEvent.ACTION_DOWN:
                last.set(event.getX(), event.getY());
                start.set(last);
                mMode = Mode.DRAG;
                break;
            //when two fingers are touching
            //set the mMode to ZOOM
            case MotionEvent.ACTION_POINTER_DOWN:
                last.set(event.getX(), event.getY());
                start.set(last);
                mMode = Mode.ZOOM;
                break;
            //when a finger moves
            //If mMode is applicable move image
            case MotionEvent.ACTION_MOVE:
                //if the mMode is ZOOM or
                //if the mMode is DRAG and already zoomed
                if (mMode == Mode.ZOOM || (mMode == Mode.DRAG && mSaveScale > MIN_SCALE)) {
                    float deltaX = curr.x - last.x;// x difference
                    float deltaY = curr.y - last.y;// y difference
                    float scaleWidth = Math.round(
                            mOriginalBitmapWidth * mSaveScale);// width after applying current scale
                    float scaleHeight = Math.round(
                            mOriginalBitmapHeight
                                    * mSaveScale);// height after applying current scale

                    boolean limitX = false;
                    boolean limitY = false;

                    //if scaleWidth is smaller than the views width
                    //in other words if the image width fits in the view
                    //limit left and mRight movement
                    if (scaleWidth < getWidth() && scaleHeight < getHeight()) {
                        // don't do anything
                    } else if (scaleWidth < getWidth()) {
                        deltaX = 0;
                        limitY = true;
                    }
                    //if scaleHeight is smaller than the views height
                    //in other words if the image height fits in the view
                    //limit up and down movement
                    else if (scaleHeight < getHeight()) {
                        deltaY = 0;
                        limitX = true;
                    }
                    //if the image doesn't fit in the width or height
                    //limit both up and down and left and mRight
                    else {
                        limitX = true;
                        limitY = true;
                    }

                    if (limitY) {
                        if (y + deltaY > 0) {
                            deltaY = -y;
                        } else if (y + deltaY < -mBottom) {
                            deltaY = -(y + mBottom);
                        }

                    }

                    if (limitX) {
                        if (x + deltaX > 0) {
                            deltaX = -x;
                        } else if (x + deltaX < -mRight) {
                            deltaX = -(x + mRight);
                        }

                    }
                    //move the image with the mMatrix
                    mMatrix.postTranslate(deltaX, deltaY);
                    //set the last touch location to the current
                    last.set(curr.x, curr.y);
                }
                break;
            //first finger is lifted
            case MotionEvent.ACTION_UP:
                mMode = Mode.NONE;
                int xDiff = (int) Math.abs(curr.x - start.x);
                int yDiff = (int) Math.abs(curr.y - start.y);
                if (xDiff < MAX_DIFF_FOR_CLICK && yDiff < MAX_DIFF_FOR_CLICK) {
                    performClick();
                }
                break;
            // second finger is lifted
            case MotionEvent.ACTION_POINTER_UP:
                mMode = Mode.NONE;
                break;
        }
        setImageMatrix(mMatrix);
        invalidate();
        return true;
    }

    private int getBitmapHeight() {
        final Drawable drawable = getDrawable();
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return 0;
    }

    private int getBitmapWidth() {
        final Drawable drawable = getDrawable();
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return 0;
    }

    private void init(Context context) {
        super.setClickable(true);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        mMatrix = new Matrix();
        setImageMatrix(mMatrix);
        setScaleType(ScaleType.MATRIX);
    }

    private enum Mode {
        NONE, DRAG, ZOOM
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scaleFactor = detector.getScaleFactor();
            float newScale = mSaveScale * scaleFactor;
            if (newScale < MAX_SCALE && newScale > MIN_SCALE) {
                mSaveScale = newScale;
                float width = getWidth();
                float height = getHeight();
                mRight = (mOriginalBitmapWidth * mSaveScale) - width;
                mBottom = (mOriginalBitmapHeight * mSaveScale) - height;

                float scaledBitmapWidth = mOriginalBitmapWidth * mSaveScale;
                float scaledBitmapHeight = mOriginalBitmapHeight * mSaveScale;

                if (scaledBitmapWidth <= width || scaledBitmapHeight <= height) {
                    mMatrix.postScale(scaleFactor, scaleFactor, width / 2, height / 2);
                } else {
                    mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(),
                            detector.getFocusY());
                }
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mMode = Mode.ZOOM;
            return true;
        }

    }
}
