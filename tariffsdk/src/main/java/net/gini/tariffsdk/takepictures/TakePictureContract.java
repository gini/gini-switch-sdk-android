package net.gini.tariffsdk.takepictures;


interface TakePictureContract {

    interface Presenter {

    }

    interface View {

        void setPresenter(TakePictureContract.Presenter presenter);

        void initCamera();
    }
}
