package de.droidenschmiede.weather.billing;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;

import java.util.List;

import de.droidenschmiede.weather.MainActivity;

public class PurchaseManager {

    public MainActivity m;

    //Inventory

    private DonateState donateState = DonateState.CHECKING;
    private Purchase purDonation;

    public PurchaseManager(MainActivity m) {
        this.m = m;

        billingListener();
    }

    public void billingListener(){

        m.billingManager.setBillingListener(new BillingListener() {
            @Override
            public void onPurchasesUpdated(List<Purchase> purchases) {

                donateState = DonateState.CHECKING;
                purDonation = null;

                for(Purchase purchase: purchases){

                    if(purchase.getSku().equals(BillingConstants.getSku())){

                        donateState = DonateState.CONFIRMED;
                        purDonation = purchase;

                        consumeSpende();
                    }
                }

                //togglePremiumIcon();
            }

            @Override
            public void onConsumeFinished(BillingResult result, String purchaseToken) {

                m.dialogManager.showDankeDialog();

                m.billingManager.queryPurchases();
            }
        });
    }

    public void consumeSpende(){

        if(donateState == DonateState.CONFIRMED){

            m.billingManager.consumeAsync(purDonation.getPurchaseToken());
        }
    }

    public boolean isDonationActive(){

        if(donateState == DonateState.CONFIRMED){
            return true;
        }
        else{
            return false;
        }
    }

}
