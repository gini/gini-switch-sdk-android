package net.gini.tariffsdk;


import static android.support.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.support.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.support.media.ExifInterface.ORIENTATION_ROTATE_90;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class DocumentServiceImpl implements DocumentService {

    private final Context mContext;
    private final Set<DocumentListener> mDocumentListeners;
    private final List<Image> mImageList;

    DocumentServiceImpl(final Context context) {
        mContext = context.getApplicationContext();
        mImageList = new ArrayList<>();
        mDocumentListeners = new CopyOnWriteArraySet<>();
    }

    @Override
    public void addDocumentListener(@NonNull final DocumentListener listener) {
        mDocumentListeners.add(listener);
    }

    @Override
    public void deleteImage(@NonNull final Uri uri) {

        new File(uri.getPath()).delete();

        final Image image = new Image(uri, State.DELETED);
        mImageList.remove(image);
        notifyListeners(image);
    }


    @Override
    public List<Image> getImageList() {
        return mImageList;
    }

    @Override
    public void keepImage(@NonNull final Uri uri) {
        //TODO - start processing
        final Image image = new Image(uri, State.PROCESSING);
        new Thread(new Runnable() {
            public void run() {
                try {
                    //Artificial delay for mocking, later we process correct
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                image.setProcessingState(State.SUCCESSFULLY_PROCESSED);
                imageProcessed(image);
            }
        }).start();

        mImageList.add(image);
    }

    @Override
    public void removeDocumentListener(@NonNull final DocumentListener listener) {
        mDocumentListeners.remove(listener);
    }

    @Override
    public Image saveImage(@NonNull final byte[] data) {
        final File directory = mContext.getDir("tariffsdk", Context.MODE_PRIVATE);
        final String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(
                new Date());
        final File file = new File(directory, fileName + ".jpeg");
        Uri uri;
        try {
            final FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.close();

            final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            final String orientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            final String newRotation = getNewRotation(orientation);
            exif.setAttribute(ExifInterface.TAG_ORIENTATION, newRotation);
            exif.saveAttributes();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            uri = Uri.EMPTY;
        }
        return new Image(uri, State.WAITING_FOR_PROCESSING);
    }

    private String getNewRotation(String orientation) {
        switch (orientation) {
            case "3":
                return Integer.toString(ORIENTATION_ROTATE_180);
            case "0":
            case "6":
                return Integer.toString(ORIENTATION_ROTATE_90);
            case "8":
                return Integer.toString(ORIENTATION_ROTATE_270);
        }
        return orientation;
    }

    private void imageProcessed(final Image image) {
        final int position = mImageList.indexOf(image);
        if (position >= 0) {
            mImageList.remove(position);
            mImageList.add(position, image);
        } else {
            mImageList.add(image);
        }
        notifyListeners(image);
    }

    private void notifyListeners(final Image image) {
        for (DocumentListener documentListener : mDocumentListeners) {
            documentListener.onImageStatChanged(image);
        }
    }
}
