package net.gini.tariffsdk.documentservice;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

public interface DocumentService {

    void deleteImage(final Uri imageUri);

    void keepImage(final Uri imageUri);

    Uri saveImage(@NonNull final byte[] data);

    SimpleArrayMap<Uri, Boolean> getImageList();

    void addDocumentListener(@NonNull DocumentListener listener);

    void removeDocumentListener(@NonNull DocumentListener listener);

    interface DocumentListener {
        void onDocumentProcessed(final Uri uri);
    }
}
