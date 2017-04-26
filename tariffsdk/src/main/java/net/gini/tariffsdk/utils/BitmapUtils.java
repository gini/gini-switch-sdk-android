package net.gini.tariffsdk.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

class BitmapUtils {

    static Bitmap decodeSampledBitmapFromUri(Context context, Uri uri, int reqWidth,
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

    private static int calculateInSampleSize(
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
}
