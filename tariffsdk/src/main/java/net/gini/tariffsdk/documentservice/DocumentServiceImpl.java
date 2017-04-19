package net.gini.tariffsdk.documentservice;


import static android.support.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.support.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.support.media.ExifInterface.ORIENTATION_ROTATE_90;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;
import android.support.v4.util.SimpleArrayMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DocumentServiceImpl implements DocumentService {

    private static DocumentService mInstance;

    private final Context mContext;

    private final SimpleArrayMap<Uri, Boolean> mImageList;

    private DocumentServiceImpl(final Context context) {
        mContext = context.getApplicationContext();
        mImageList = new SimpleArrayMap<>();
    }

    @Override
    public void deleteImage(final Uri imageUri) {
        new File(imageUri.getPath()).delete();
    }

    @Override
    public void keepImage(final Uri imageUri) {
        //TODO
        mImageList.put(imageUri, true);
    }

    @Override
    public Uri saveImage(@NonNull final byte[] data, final File directory) {
        final String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(
                new Date());
        final File file = new File(directory, fileName + ".jpeg");
        try {
            final FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.close();

            final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            final String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            final String newRotation = getNewRotation(orientation);
            exif.setAttribute(ExifInterface.TAG_ORIENTATION, newRotation);
            exif.saveAttributes();
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.EMPTY;
    }

    @Override
    public SimpleArrayMap<Uri, Boolean> getImageList() {
        return mImageList;
    }

    private String getNewRotation(String orientation) {
        switch (orientation) {
            case "3":
                return "" + ORIENTATION_ROTATE_180;
            case "0":
            case "6":
                return "" + ORIENTATION_ROTATE_90;
            case "8":
                return "" + ORIENTATION_ROTATE_270;
        }
        return orientation;
    }

    public static DocumentService getInstance(final Context context) {
        if (mInstance == null) {
            mInstance = new DocumentServiceImpl(context);
        }
        return mInstance;
    }
}
