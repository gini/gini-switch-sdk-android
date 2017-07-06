package net.gini.tariffsdk.network;


import android.support.annotation.Nullable;

import java.util.List;

public class ExtractionOrderState {
    @Nullable
    private final String mExtractionUrl;
    private final ExtractionOrder mOrder;
    private final boolean mOrderComplete;
    private final List<ExtractionOrderPage> mOrderPages;

    public ExtractionOrderState(
            final List<ExtractionOrderPage> orderPages, final ExtractionOrder order,
            final boolean orderComplete) {
        this(orderPages, order, orderComplete, null);
    }

    public ExtractionOrderState(
            final List<ExtractionOrderPage> orderPages, final ExtractionOrder order,
            final boolean orderComplete, final String extractionUrl) {
        mOrderPages = orderPages;
        mOrder = order;
        mOrderComplete = orderComplete;
        mExtractionUrl = extractionUrl;
    }

    @Nullable
    public String getExtractionUrl() {
        return mExtractionUrl;
    }

    public ExtractionOrder getOrder() {
        return mOrder;
    }

    public List<ExtractionOrderPage> getOrderPages() {
        return mOrderPages;
    }

    public boolean isOrderComplete() {
        return mOrderComplete;
    }
}
