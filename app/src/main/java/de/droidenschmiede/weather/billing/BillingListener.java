package de.droidenschmiede.weather.billing;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;

import java.util.List;

public interface BillingListener {

    void onPurchasesUpdated(List<Purchase> purchases);
    void onConsumeFinished(BillingResult result, String purchaseToken);
}
