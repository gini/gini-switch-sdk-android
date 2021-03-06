package net.gini.switchsdk.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

class BitmapMemoryCache {

    private static BitmapMemoryCache mInstance = null;
    private final LruCache<Uri, Bitmap> mMemoryCache;

    private BitmapMemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<Uri, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Uri key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    static BitmapMemoryCache getInstance() {
        if (mInstance == null) {
            mInstance = new BitmapMemoryCache();
        }
        return mInstance;
    }

    void loadBitmapAsync(Uri uri, int height, int width, Context context, BitmapListener callback) {
        final Bitmap bitmap = getBitmapFromMemCache(uri);
        if (bitmap != null) {
            callback.bitmapLoaded(bitmap);
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask(callback, height, width, context, false);
            task.execute(uri);
        }
    }

    void loadThumbnailAsync(Uri uri, int height, int width, Context context,
            BitmapListener callback) {

        final Bitmap bitmap = getBitmapFromMemCache(uri);
        if (bitmap != null) {
            Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, height, width);
            callback.bitmapLoaded(thumbnail);
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask(callback, height, width, context, true);
            task.execute(uri);
        }
    }

    private void addBitmapToMemoryCache(Uri key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmapFromUri(Context context, Uri uri, int reqWidth,
            int reqHeight) {

        Bitmap decodedBitmap = null;
        InputStream inputStream = null;
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            inputStream = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(inputStream, null, options);
            if (inputStream != null) {
                inputStream.close();
            }

            inputStream = context.getContentResolver().openInputStream(uri);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            decodedBitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ignored) {
            }
        }

        return decodedBitmap;
    }

    private Bitmap getBitmapFromMemCache(Uri key) {
        return mMemoryCache.get(key);
    }

    private class BitmapWorkerTask extends AsyncTask<Uri, Void, Bitmap> {
        final int mHeight;
        final int mWidth;
        private final WeakReference<Context> mContextReference;
        private final WeakReference<BitmapListener> mImageViewReference;
        private final boolean mIsThumbnail;

        BitmapWorkerTask(final BitmapListener listener, final int height, final int width,
                final Context context, final boolean isThumbnail) {

            mHeight = height;
            mWidth = width;
            mImageViewReference = new WeakReference<>(listener);
            mContextReference = new WeakReference<>(context);
            mIsThumbnail = isThumbnail;
        }

        @Override
        protected Bitmap doInBackground(final Uri... params) {
            final Bitmap bitmap = decodeSampledBitmapFromUri(mContextReference.get(), params[0],
                    mWidth, mHeight);
            addBitmapToMemoryCache(params[0], bitmap);
            return bitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap bitmap) {
            if (mIsThumbnail) {
                Bitmap thumbnail = ThumbnailUtils.extractThumbnail(bitmap, mHeight, mWidth);
                mImageViewReference.get().bitmapLoaded(thumbnail);
            } else {
                mImageViewReference.get().bitmapLoaded(bitmap);
            }
        }
    }

    interface BitmapListener {
        void bitmapLoaded(@Nullable final Bitmap bitmap);
    }
}
