package net.gini.tariffsdk;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.media.ExifInterface;

import net.gini.tariffsdk.network.ExtractionOrder;
import net.gini.tariffsdk.network.ExtractionOrderPage;
import net.gini.tariffsdk.network.NetworkCallback;
import net.gini.tariffsdk.network.TariffApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class DocumentServiceImpl implements DocumentService {

    private final Context mContext;
    private final Set<DocumentListener> mDocumentListeners;
    private final List<Image> mImageList;
    private final TariffApi mTariffApi;
    private final HashSet<Image> mUploadedImages;
    private ExtractionOrder mExtractionOrder;

    DocumentServiceImpl(final Context context, final TariffApi tariffApi) {
        mContext = context.getApplicationContext();
        mTariffApi = tariffApi;
        mImageList = new ArrayList<>();
        mDocumentListeners = new CopyOnWriteArraySet<>();
        mUploadedImages = new HashSet<>();
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void addDocumentListener(@NonNull final DocumentListener listener) {
        mDocumentListeners.add(listener);
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void cleanup() {
        mDocumentListeners.clear();
        final List<Image> imagesToDelete = new ArrayList<>(mImageList);
        mImageList.clear();
        new Thread(new Runnable() {
            public void run() {
                for (Image image : imagesToDelete) {
                    Uri uri = image.getUri();
                    deleteFileFromStorage(uri);
                }
            }
        }).start();

    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void deleteImage(@NonNull final Uri uri) {

        deleteFileFromStorage(uri);

        final Image image = new Image(uri, ImageState.DELETED);
        mImageList.remove(image);

        notifyListeners(image);
        if (mUploadedImages.contains(image)) {
            //TODO when specified by the backend
//            mTariffApi.deletePage(image);
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public List<Image> getImageList() {
        return mImageList;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void keepImage(@NonNull final Uri uri, final int rotationCount) {
        final Image image = new Image(uri, ImageState.PROCESSING);
        mImageList.add(image);

        if (rotationCount > 0) {
            //rotate image

            //reupload image if uploaded
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void removeDocumentListener(@NonNull final DocumentListener listener) {
        mDocumentListeners.remove(listener);
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public Image saveImage(@NonNull final byte[] data, final int cameraOrientation) {
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
            final String newRotation = getNewRotation(cameraOrientation);
            exif.setAttribute(ExifInterface.TAG_ORIENTATION, newRotation);
            exif.saveAttributes();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            uri = Uri.EMPTY;
        }
        final Image image = new Image(uri, ImageState.WAITING_FOR_PROCESSING);
        uploadImage(image);
        return image;
    }

    private void deleteFileFromStorage(final @NonNull Uri uri) {
        final File file = getFileFromUri(uri);
        file.delete();
    }

    private byte[] getBytesFromFile(final File imageFile) throws IOException {
        byte[] data;
        RandomAccessFile f = new RandomAccessFile(imageFile, "r");
        try {
            long longLength = f.length();
            int length = (int) longLength;
            data = new byte[length];
            f.readFully(data);
        } finally {
            f.close();
        }
        return data;
    }

    @NonNull
    private File getFileFromUri(final @NonNull Uri uri) {
        return new File(uri.getPath());
    }

    private String getNewRotation(int degrees) {
        int exifOrientation;
        switch (degrees) {
            case 0:
                exifOrientation = 1; // 0CW
                break;
            case 90:
                exifOrientation = 6; // 270CW
                break;
            case 180:
                exifOrientation = 3; // 180CW
                break;
            case 270:
                exifOrientation = 8; // 90CW
                break;
            default:
                exifOrientation = 1; // 0CW
                break;
        }
        return Integer.toString(exifOrientation);
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

    private void pollStatePeriodically() {

    }

    private void uploadImage(final Image image) {
        if (mExtractionOrder == null) {
            mTariffApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
                @Override
                public void onError(final Exception e) {
                    //TODO
                }

                @Override
                public void onSuccess(final ExtractionOrder extractionOrder) {
                    mExtractionOrder = extractionOrder;

                    uploadImage(image);

                }
            });
        } else {
            File imageFile = getFileFromUri(image.getUri());

            byte[] data = new byte[0];
            try {
                data = getBytesFromFile(imageFile);
            } catch (IOException e) {
                //TODO
                e.printStackTrace();
            }

            mTariffApi.addPage(mExtractionOrder.getPages(), data,
                    new NetworkCallback<ExtractionOrderPage>() {
                        @Override
                        public void onError(final Exception e) {
                            //TODO
                        }

                        @Override
                        public void onSuccess(final ExtractionOrderPage page) {
                            if (page.getStatus() != ExtractionOrderPage.Status.failed) {
                                mUploadedImages.add(image);
                            } else {

                            }
                        }
                    });
        }
    }
}
