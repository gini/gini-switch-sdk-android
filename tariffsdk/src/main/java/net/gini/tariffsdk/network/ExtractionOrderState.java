package net.gini.tariffsdk.network;


import java.util.List;

public class ExtractionOrderState {
    private final ExtractionOrder mOrder;
    private final boolean mOrderComplete;
    private final List<ExtractionOrderPage> mOrderPages;

    public ExtractionOrderState(
            final List<ExtractionOrderPage> orderPages, final ExtractionOrder order,
            final boolean orderComplete) {
        mOrderPages = orderPages;
        mOrder = order;
        mOrderComplete = orderComplete;
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
