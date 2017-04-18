package net.gini.tariffsdk.documentservice;


import android.net.Uri;

public interface DocumentService {

    void deleteImage(final Uri imageUri);

    void keepImage(final Uri imageUri);
}
