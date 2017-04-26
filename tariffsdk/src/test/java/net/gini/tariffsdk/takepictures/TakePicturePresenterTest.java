package net.gini.tariffsdk.takepictures;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.gini.tariffsdk.documentservice.DocumentService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TakePicturePresenterTest {

    private static final int AndroidLollipop = 21;

    private static final int AndroidMarshmallow = 23;
    @Mock
    DocumentService mMockDocumentService;
    @Mock
    TakePictureContract.View mMockView;

    @Test
    public void permissionDenied_initCameraNotCalled() {
        TakePicturePresenter presenter = new TakePicturePresenter(mMockView, mMockDocumentService);
        presenter.mBuildVersion = AndroidMarshmallow;

        when(mMockView.cameraPermissionsGranted()).thenReturn(false);
        presenter.start();
        verify(mMockView, never()).initCamera();
    }

    @Test
    public void permissionGranted_initCameraCalled() {
        TakePicturePresenter presenter = new TakePicturePresenter(mMockView, mMockDocumentService);
        presenter.mBuildVersion = AndroidMarshmallow;

        when(mMockView.cameraPermissionsGranted()).thenReturn(true);
        presenter.start();
        verify(mMockView).initCamera();
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void versionGreaterThan23_requestPermissions() {
        TakePicturePresenter presenter = new TakePicturePresenter(mMockView, mMockDocumentService);
        presenter.mBuildVersion = AndroidMarshmallow;

        when(mMockView.cameraPermissionsGranted()).thenReturn(false);
        presenter.start();
        verify(mMockView).requestPermissions();
    }

    @Test
    public void versionSmallerThan23_doNotRequestPermissions() {
        TakePicturePresenter presenter = new TakePicturePresenter(mMockView, mMockDocumentService);
        presenter.mBuildVersion = AndroidLollipop;

        presenter.start();
        verify(mMockView, never()).requestPermissions();
    }

    @Test
    public void startPresenter_shouldAddListener() {
        TakePicturePresenter presenter = new TakePicturePresenter(mMockView, mMockDocumentService);
        presenter.start();

        verify(mMockDocumentService).addDocumentListener(presenter);
    }

    @Test
    public void stopPresenter_shouldRemoveListener() {
        TakePicturePresenter presenter = new TakePicturePresenter(mMockView, mMockDocumentService);
        presenter.stop();

        verify(mMockDocumentService).removeDocumentListener(presenter);
    }



}