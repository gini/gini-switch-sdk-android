package net.gini.tariffsdk.documentservice;


import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.List;

public interface DocumentService {

    void addDocumentListener(@NonNull DocumentListener listener);

    void deleteImage(@NonNull final Uri uri);

    List<Image> getImageList();

    void keepImage(@NonNull final Uri uri);

    void removeDocumentListener(@NonNull DocumentListener listener);

    Image saveImage(@NonNull final byte[] data);

    interface DocumentListener {
        void onDocumentProcessed(@NonNull final Image image);
    }
}
