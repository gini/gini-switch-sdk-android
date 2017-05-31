package net.gini.tariffsdk.network;


public class ExtractionOrder {
    private final String mPages;
    private final String mSelf;

    public ExtractionOrder(final String self, final String pages) {
        mSelf = self;
        mPages = pages;
    }

    public String getPages() {
        return mPages;
    }

    public String getSelf() {
        return mSelf;
    }
}
