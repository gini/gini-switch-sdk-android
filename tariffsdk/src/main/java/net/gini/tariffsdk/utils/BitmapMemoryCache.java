package net.gini.tariffsdk.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

class BitmapMemoryCache {

    private static BitmapMemoryCache mInstance = null;
    private LruCache<Uri, Bitmap> mMemoryCache;

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

    void setImage(Uri uri, ImageView imageView) {
        final Bitmap bitmap = getBitmapFromMemCache(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
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
        private final WeakReference<ImageView> mImageViewReference;

        BitmapWorkerTask(final ImageView imageView) {

            mImageViewReference = new WeakReference<>(imageView);
            mHeight = imageView.getHeight();
            mWidth = imageView.getWidth();
            mContextReference = new WeakReference<>(imageView.getContext());
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
            mImageViewReference.get().setImageBitmap(bitmap);
        }
    }
}
