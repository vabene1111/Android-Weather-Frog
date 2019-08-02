package de.droidenschmiede.weather.billing;

import android.view.View;
import android.widget.ImageView;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;

import java.util.List;

import de.droidenschmiede.weather.MainActivity;

public class PurchaseManager {

    public MainActivity m;

    //Inventory

    private PremiumState premiumState = PremiumState.CHECKING;
    private Purchase purPremium;

    public PurchaseManager(MainActivity m) {
        this.m = m;

        billingListener();
    }

    public void billingListener(){

        m.billingManager.setBillingListener(new BillingListener() {
            @Override
            public void onPurchasesUpdated(List<Purchase> purchases) {

                premiumState = PremiumState.CHECKING;
                purPremium = null;

                for(Purchase purchase: purchases){

                    if(purchase.getSku().equals("android.test.purchased")){

                        premiumState = PremiumState.CONFIRMED;
                        purPremium = purchase;

                        consumeSpende();
                        m.dialogManager.showDankeDialog();

                    }
                }

                //togglePremiumIcon();
            }

            @Override
            public void onConsumeFinished(BillingResult result, String purchaseToken) {

                m.billingManager.queryPurchases();
            }
        });
    }

    public void consumeSpende(){

        if(premiumState == PremiumState.CONFIRMED){

            m.billingManager.consumeAsync(purPremium.getPurchaseToken());
        }
    }

    public boolean isPremiumActive(){

        if(premiumState == PremiumState.CONFIRMED){
            return true;
        }
        else{
            return false;
        }
    }

}
