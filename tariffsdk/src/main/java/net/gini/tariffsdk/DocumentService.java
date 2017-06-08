package net.gini.tariffsdk;


import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.List;

interface DocumentService {

    void addDocumentListener(@NonNull DocumentListener listener);

    void cleanup();

    void deleteImage(@NonNull final Uri uri);

    List<Image> getImageList();

    void keepImage(@NonNull final Uri uri);

    void removeDocumentListener(@NonNull DocumentListener listener);

    Image saveImage(@NonNull final byte[] data, final int cameraOrientation);

    interface DocumentListener {
        void onImageStatChanged(@NonNull final Image image);
    }
}
