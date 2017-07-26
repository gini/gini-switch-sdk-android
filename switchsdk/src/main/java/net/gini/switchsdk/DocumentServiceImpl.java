package net.gini.switchsdk;


import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.media.ExifInterface;

import net.gini.switchsdk.network.ExtractionOrder;
import net.gini.switchsdk.network.ExtractionOrderPage;
import net.gini.switchsdk.network.ExtractionOrderState;
import net.gini.switchsdk.network.NetworkCallback;
import net.gini.switchsdk.network.SwitchApi;
import net.gini.switchsdk.utils.ExifUtils;
import net.gini.switchsdk.utils.Logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

class DocumentServiceImpl implements DocumentService {

    private static final int POLLING_INTERVAL_IN_MS = 1000;
    @VisibleForTesting
    final Map<Image, String> mImageUrls;
    private final Context mContext;
    private final Set<DocumentListener> mDocumentListeners;
    private final List<Image> mImageList;
    private final SwitchApi mSwitchApi;

    @VisibleForTesting
    ExtractionOrder mExtractionOrder;
    private String mExtractionUrl;
    private Handler mPollingHandler;
    private final Runnable mPollingRunnable = new Runnable() {
        @Override
        public void run() {
            //only fetch the state when we have one image uploaded
            if (mImageUrls.size() > 0) {
                fetchOrderState();
            }
            mPollingHandler.postDelayed(mPollingRunnable, POLLING_INTERVAL_IN_MS);
        }
    };

    DocumentServiceImpl(final Context context, final SwitchApi switchApi) {
        mContext = context.getApplicationContext();
        mSwitchApi = switchApi;
        mImageList = new ArrayList<>();
        mDocumentListeners = new CopyOnWriteArraySet<>();
        mImageUrls = new HashMap<>();
        mPollingHandler = new Handler(Looper.getMainLooper());
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void addDocumentListener(@NonNull final DocumentListener listener) {
        mDocumentListeners.add(listener);
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void cleanup() {
        stopStatePolling();
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
    public void createExtractionOrder() {
        mSwitchApi.createExtractionOrder(new NetworkCallback<ExtractionOrder>() {
            @Override
            public void onError(final Exception e) {
                //TODO
                Logging.e("Create Extraction Order failed.", e);
            }

            @Override
            public void onSuccess(final ExtractionOrder extractionOrder) {
                mExtractionOrder = extractionOrder;
                //we have an order we start to poll its state
                startStatePolling();
            }
        });
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void deleteImage(@NonNull final Uri uri) {

        deleteFileFromStorage(uri);

        final Image image = new Image(uri, ImageState.DELETED);
        mImageList.remove(image);

        notifyListeners(image);
        final String url = mImageUrls.get(image);
        if (url != null) {
            mSwitchApi.deletePage(url);
        }
    }

    @Override
    public String getExtractionUrl() {
        return mExtractionUrl;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public List<Image> getImageList() {
        return mImageList;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void keepImage(@NonNull final Uri uri) {
        final Image image = new Image(uri, ImageState.PROCESSING);
        mImageList.add(image);
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void removeDocumentListener(@NonNull final DocumentListener listener) {
        mDocumentListeners.remove(listener);
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public void replaceImage(@NonNull final Uri uri, final int rotationCount) {
        final Image image = new Image(uri, ImageState.PROCESSING);
        try {
            //if we rotate 4 times with 90 degrees we are at the start
            writeNewRotationIntoExif(uri, (rotationCount % 4) * 90);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mImageUrls.containsKey(image)) {
            replaceImage(mImageUrls.get(image), image);
        } else {
            uploadImage(image);
        }
        mImageList.add(image);
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Override
    public Image saveImage(@NonNull final byte[] data, final int cameraOrientation) {
        final File directory = mContext.getDir("switchsdk", Context.MODE_PRIVATE);
        final String fileName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(
                new Date());
        final File file = new File(directory, fileName + ".jpeg");
        Uri uri;
        try {
            final FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.close();

            final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            final String newRotation = Integer.toString(
                    ExifUtils.getExifFromDegrees(cameraOrientation));
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

    private void fetchOrderState() {
        mSwitchApi.getOrderState(mExtractionOrder.getSelf(),
                new NetworkCallback<ExtractionOrderState>() {
                    @Override
                    public void onError(final Exception e) {
                        //TODO might be ignored since this call is repeated in an interval
                        Logging.e("Fetching Order State failed.", e);
                    }

                    @Override
                    public void onSuccess(final ExtractionOrderState extractionOrderState) {
                        for (final ExtractionOrderPage extractionOrderPage :
                                extractionOrderState.getOrderPages()) {
                            final Image image = getImageFromUrl(extractionOrderPage.getSelf());
                            if (image != null) {
                                final ImageState imageState = getImageState(extractionOrderPage);
                                image.setProcessingState(imageState);
                                updateImageState(image);
                            }
                        }
                        mExtractionUrl = extractionOrderState.getExtractionUrl();
                        if (extractionOrderState.isOrderComplete()) {
                            for (final DocumentListener documentListener : mDocumentListeners) {
                                documentListener.onOrderCompleted(mExtractionUrl);
                                //if the order is complete there is no need for polling anymore
                                stopStatePolling();
                            }
                        }
                    }
                });
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

    private byte[] getBytesFromImage(final Image image) {
        File imageFile = getFileFromUri(image.getUri());

        byte[] data = new byte[0];
        try {
            data = getBytesFromFile(imageFile);
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }
        return data;
    }

    @NonNull
    private File getFileFromUri(final @NonNull Uri uri) {
        return new File(uri.getPath());
    }

    private Image getImageFromUrl(final String selfUrl) {
        for (final Image image : mImageUrls.keySet()) {
            if (mImageUrls.get(image).equals(selfUrl)) {
                return image;
            }
        }
        return null;
    }

    private ImageState getImageState(final ExtractionOrderPage extractionOrderPage) {
        ExtractionOrderPage.Status status =
                extractionOrderPage.getStatus();
        if (status == ExtractionOrderPage.Status.processed) {
            return ImageState.SUCCESSFULLY_PROCESSED;
        } else if (status == ExtractionOrderPage.Status.failed) {
            return ImageState.FAILED;
        }
        return ImageState.PROCESSING;
    }

    @NonNull
    private NetworkCallback<ExtractionOrderPage> getImageUploadingCallback(final Image image) {
        return new NetworkCallback<ExtractionOrderPage>() {
            @Override
            public void onError(final Exception e) {
                //TODO
                Logging.e("Uploading image to API failed.", e);
            }

            @Override
            public void onSuccess(final ExtractionOrderPage page) {
                if (page.getStatus() != ExtractionOrderPage.Status.failed) {
                    mImageUrls.put(image, page.getSelf());
                } else {

                }
            }
        };
    }

    private void notifyListeners(final Image image) {
        for (DocumentListener documentListener : mDocumentListeners) {
            documentListener.onImageStateChanged(image);
        }
    }

    private void replaceImage(@NonNull final String url, final Image image) {
        if (mExtractionOrder != null) {
            byte[] data = getBytesFromImage(image);
            mSwitchApi.replacePage(url, data, getImageUploadingCallback(image));
        }
    }

    private void startStatePolling() {
        Logging.v("Stated Polling started");
        mPollingRunnable.run();
    }

    private void stopStatePolling() {
        Logging.v("Stated Polling stopped.");
        mPollingHandler.removeCallbacks(mPollingRunnable);
    }

    private void updateImageState(final Image image) {
        final int position = mImageList.indexOf(image);
        if (position >= 0) {
            //only update if there is a change in the processing state
            if (image.getProcessingState() != mImageList.get(position).getProcessingState()) {
                mImageList.set(position, image);
                notifyListeners(image);
            }
        }
    }

    private void uploadImage(final Image image) {
        if (mExtractionOrder != null) {
            byte[] data = getBytesFromImage(image);
            mSwitchApi.addPage(mExtractionOrder.getPages(), data, getImageUploadingCallback(image));
        }
    }

    private void writeNewRotationIntoExif(final Uri uri, final int rotation) throws IOException {
        final File file = getFileFromUri(uri);
        final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
        final int oldOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
        final int oldDegrees = ExifUtils.getDegreesFromExif(oldOrientation);
        //no need to rotate 360 degrees
        final int degreesToRotate = (oldDegrees + rotation) % 360;
        final String newRotation = Integer.toString(ExifUtils.getExifFromDegrees(degreesToRotate));

        exif.setAttribute(ExifInterface.TAG_ORIENTATION, newRotation);
        exif.saveAttributes();

    }
}
