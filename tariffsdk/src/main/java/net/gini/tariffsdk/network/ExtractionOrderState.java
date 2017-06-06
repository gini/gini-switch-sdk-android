package net.gini.tariffsdk.network;


import java.util.List;

public class ExtractionOrderState {
    private final ExtractionOrder mOrder;
    private final List<ExtractionOrderPage> mOrderPages;

    public ExtractionOrderState(
            final List<ExtractionOrderPage> orderPages, final ExtractionOrder order) {
        mOrderPages = orderPages;
        mOrder = order;
    }

    public ExtractionOrder getOrder() {
        return mOrder;
    }

    public List<ExtractionOrderPage> getOrderPages() {
        return mOrderPages;
    }
}
