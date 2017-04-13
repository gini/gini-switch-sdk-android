package net.gini.tariffsdk.takepictures;


import net.gini.tariffsdk.documentservice.DocumentService;

class TakePicturePresenter implements TakePictureContract.Presenter {

    private final TakePictureView mView;
    private final DocumentService mDocumentService;

    TakePicturePresenter(final TakePictureView view, final DocumentService documentService) {

        mView = view;
        mDocumentService = documentService;
    }
}
