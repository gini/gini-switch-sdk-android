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
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class DocumentServiceImpl implements DocumentService {

    private static DocumentService mInstance;

    private final Context mContext;
    private final Set<DocumentListener> mDocumentListeners;
    private final SimpleArrayMap<Uri, Boolean> mImageList;

    private DocumentServiceImpl(final Context context) {
        mContext = context.getApplicationContext();
        mImageList = new SimpleArrayMap<>();
        mDocumentListeners = new HashSet<>();
    }

    @Override
    public void addDocumentListener(@NonNull final DocumentListener listener) {
        mDocumentListeners.add(listener);
    }

    @Override
    public void deleteImage(final Uri imageUri) {
        new File(imageUri.getPath()).delete();
        mImageList.remove(imageUri);
    }

    @Override
    public SimpleArrayMap<Uri, Boolean> getImageList() {
        return mImageList;
    }

    @Override
    public void keepImage(final Uri imageUri) {
        //TODO
        new Thread(new Runnable() {
            public void run() {
                try {
                    //Artificial delay for mocking, later we process correct
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                imageSuccessfullyProcessed(imageUri);
            }
        }).start();

        mImageList.put(imageUri, true);
    }

    @Override
    public void removeDocumentListener(@NonNull final DocumentListener listener) {
        mDocumentListeners.remove(listener);
    }

    @Override
    public Uri saveImage(@NonNull final byte[] data) {
        final File directory = mContext.getDir("tariffsdk", Context.MODE_PRIVATE);
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

    public static DocumentService getInstance(final Context context) {
        if (mInstance == null) {
            mInstance = new DocumentServiceImpl(context);
        }
        return mInstance;
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

    private void imageSuccessfullyProcessed(final Uri imageUri) {
        mImageList.put(imageUri, false);
        notifyListeners(imageUri);
    }

    private void notifyListeners(final Uri imageUri) {
        for (DocumentListener documentListener : mDocumentListeners) {
            documentListener.onDocumentProcessed(imageUri);
        }
    }
}
