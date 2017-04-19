package net.gini.tariffsdk.documentservice;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.io.File;

public interface DocumentService {

    void deleteImage(final Uri imageUri);

    void keepImage(final Uri imageUri);

    Uri saveImage(@NonNull final byte[] data, File directory);

    SimpleArrayMap<Uri, Boolean> getImageList();
}
