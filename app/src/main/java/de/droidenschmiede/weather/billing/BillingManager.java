package de.droidenschmiede.weather.billing;

import android.annotation.SuppressLint;
import androidx.annotation.Nullable;
import android.util.Log;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.droidenschmiede.weather.MainActivity;

import static android.content.ContentValues.TAG;

public class BillingManager implements PurchasesUpdatedListener{

    public MainActivity m;

    private BillingClient billingClient;


    private BillingListener listener;

    private AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener;

    public String premiumUpgradePrice;
    public String premiumUpgradePriceNoDiscount;

    public List<SkuDetails> skuDetailsAll;

    private Set<String> mTokensToBeConsumed;

    private final List<Purchase> mPurchases = new ArrayList<>();

    private int mBillingClientResponseCode = BillingClient.BillingResponseCode.BILLING_UNAVAILABLE;

    private boolean mIsServiceConnected;

    /* BASE_64_ENCODED_PUBLIC_KEY should be YOUR APPLICATION'S PUBLIC KEY
     * (that you got from the Google Play developer console). This is not your
     * developer public key, it's the *app-specific* public key.
     *
     * Instead of just storing the entire literal string here embedded in the
     * program,  construct the key at runtime from pieces or
     * use bit manipulation (for example, XOR with some other string) to hide
     * the actual key.  The key itself is not secret information, but we don't
     * want to make it easy for an attacker to replace the public key with one
     * of their own and then fake messages from the server.
     */
    private static final String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqZm5fS2oJw7Ii6x39xUla00pbr3dYraTDOkDhVB80073LtBI3QgDzOKYbVOPUImJl0aMfmWb3HaxkuKVNebjeznzx3OavZVZC05nUIedZIa8+sBKZX2OGxKX2IcO95rXcs7JYjfD48meWN8JY1GfNVeJ2MISrXTZlZRJi5mG/LLsAvh6m18HPlRUadn/O1OGQFAv7Q8RxNG3TCPPg7ZsAfC6mO88Eo3H/bRg5AWcaiel+N96vTT6j4e5M5aEyK1YMNFLeF9b0P+ujppJAoAgEpH79kHiK6fZxYcCMfEr/Z59R/R9+ip1lE3zZHTegeOVbUoawOFVPkDuE+riTidW1wIDAQAB";

    public BillingManager(MainActivity m){

        this.m = m;
    }

    public void initBilling(){


        billingClient = BillingClient.newBuilder(m).setListener(this)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                    // Notifying the listener that billing client is ready

                   // mBillingUpdatesListener.onBillingClientSetupFinished();

                    mIsServiceConnected = true;

                    queryPurchases();

                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.

                mIsServiceConnected = false;
                billingClient.startConnection(this);

                mIsServiceConnected = false;

                Log.d("Billing", " disconnected");
            }
        });

        acknowledgePurchaseResponseListener = new AcknowledgePurchaseResponseListener() {
            @Override
            public void onAcknowledgePurchaseResponse(BillingResult billingResult) {

            }
        };
    }

    public void showPurchaseWindow(){

        Log.d("Billing", "PurchaseWindow");
        querySkuDetailsAsync(BillingClient.SkuType.INAPP, BillingConstants.getSkuList(), null);

    }

    /*public List<String> getSkuList(){

        List<String> skuList = new ArrayList<>();
        //skuList.add("premium_upgrade_1");
        skuList.add("android.test.purchased");
        return skuList;
    }*/

    public void querySkuDetailsAsync(@BillingClient.SkuType final String itemType, final List<String> skuList,
                                     final SkuDetailsResponseListener listener) {

        Log.d("Billing", "querySkuDetailsAsync");

        // Creating a runnable from the request to use it inside our connection retry policy below
        Runnable queryRequest = new Runnable() {
            @Override
            public void run() {
                // Query the purchase async

                Log.d("Billing", "querySkuDetailsAsync run");

                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(skuList).setType(itemType);
                billingClient.querySkuDetailsAsync(params.build(),
                        new SkuDetailsResponseListener() {
                            @Override
                            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {

                                Log.d("Billing", skuDetailsList.toString());

                                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                                    skuDetailsAll = skuDetailsList;

                                    for (SkuDetails skuDetails : skuDetailsList) {
                                        String sku = skuDetails.getSku();
                                        if(BillingConstants.getSku().equals(sku)){

                                            //skuDetails.getPrice();

                                            launchBillingFlow(skuDetails);

                                            Log.d("Billing", "2");
                                        }
                                        else{
                                            Log.d("Billing", "3");
                                        }
                                    }


                                }
                            }
                        });
            }
        };

        executeServiceRequest(queryRequest);
    }

    public void launchBillingFlow(SkuDetails skuDetails){

        // Retrieve a value for "skuDetails" by calling querySkuDetailsAsync().
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails)
                .build();
        BillingResult result = billingClient.launchBillingFlow(m, flowParams);

    }

    /**
     * Checks if subscriptions are supported for current client
     * <p>Note: This method does not automatically retry for RESULT_SERVICE_DISCONNECTED.
     * It is only used in unit tests and after queryPurchases execution, which already has
     * a retry-mechanism implemented.
     * </p>
     */
    public boolean areSubscriptionsSupported() {
        BillingResult result = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            Log.w(TAG, "areSubscriptionsSupported() got an error response: " + result.getResponseCode());
        }
        return result.getResponseCode() == BillingClient.BillingResponseCode.OK;
    }

    /**
     * Query purchases across various use cases and deliver the result in a formalized way through
     * a listener
     */
    public void queryPurchases() {

            Runnable queryToExecute = new Runnable() {
                @Override
                public void run() {
                    long time = System.currentTimeMillis();
                    Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP);
                    Log.i(TAG, "Querying purchases elapsed time: " + (System.currentTimeMillis() - time)
                            + "ms");
                    // If there are subscriptions supported, we add subscription rows as well
                    if (areSubscriptionsSupported()) {
                        Purchase.PurchasesResult subscriptionResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                        Log.i(TAG, "Querying purchases and subscriptions elapsed time: "
                                + (System.currentTimeMillis() - time) + "ms");
                        if(subscriptionResult != null && subscriptionResult.getPurchasesList() != null){
                            Log.i(TAG, "Querying subscriptions result code: "
                                    + subscriptionResult.getResponseCode()
                                    + " res: " + subscriptionResult.getPurchasesList().size());

                            if (subscriptionResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                purchasesResult.getPurchasesList().addAll(
                                        subscriptionResult.getPurchasesList());
                            } else {
                                Log.e(TAG, "Got an error response trying to query subscription purchases");
                            }
                        }

                    } else if (purchasesResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Log.i(TAG, "Skipped subscription purchases query since they are not supported");
                    } else {
                        Log.w(TAG, "queryPurchases() got an error response code: "
                                + purchasesResult.getResponseCode());
                    }
                    onQueryPurchasesFinished(purchasesResult);
                }
            };

            executeServiceRequest(queryToExecute);


    }

    private void onQueryPurchasesFinished(Purchase.PurchasesResult result) {
        // Have we been disposed of in the meantime? If so, or bad result code, then quit
        if (billingClient == null || result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            Log.w(TAG, "Billing client was null or result code (" + result.getResponseCode()
                    + ") was bad - quitting");
            return;
        }

        Log.d(TAG, "Query inventory was successful.");

        // Update the UI and purchases inventory with new list of purchases
        mPurchases.clear();
        onPurchasesUpdated(result.getBillingResult(), result.getPurchasesList());
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }


            //mBillingUpdatesListener.onPurchasesUpdated(mPurchases);

            listener.onPurchasesUpdated(mPurchases);



        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }

    }

    /**
     * Handles the purchase
     * <p>Note: Notice that for each purchase, we check if signature is valid on the client.
     * It's recommended to move this check into your backend.
     * </p>
     * @param purchase Purchase to be handled
     */
    private void handlePurchase(Purchase purchase) {

        /*
        if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
            Log.i(TAG, "Got a purchase: " + purchase + "; but signature is bad. Skipping...");
            return;
        }

        */


        Log.d(TAG, "Got a verified purchase: " + purchase);

        mPurchases.add(purchase);

        acknowledgePurchase(purchase);
    }

    private void acknowledgePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user.

            Log.d("Billing", "purchased");

            // Acknowledge the purchase if it hasn't already been acknowledged.
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }
        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
            // Here you can confirm to the user that they've started the pending
            // purchase, and to complete it, they should follow instructions that
            // are given to them. You can also choose to remind the user in the
            // future to complete the purchase if you detect that it is still
            // pending.

            Log.d("Billing", "pending");
        }
    }

    public void consumeAsync(final String purchaseToken) {
        // If we've already scheduled to consume this token - no action is needed (this could happen
        // if you received the token when querying purchases inside onReceive() and later from
        // onActivityResult()
        if (mTokensToBeConsumed == null) {
            mTokensToBeConsumed = new HashSet<>();
        } else if (mTokensToBeConsumed.contains(purchaseToken)) {
            Log.i(TAG, "Token was already scheduled to be consumed - skipping...");
            return;
        }
        mTokensToBeConsumed.add(purchaseToken);

        // Generating Consume Response listener
        final ConsumeResponseListener onConsumeListener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(BillingResult result, String purchaseToken) {
                // If billing service was disconnected, we try to reconnect 1 time
                // (feel free to introduce your retry policy here).
                listener.onConsumeFinished(result, purchaseToken);
            }
        };

        // Creating a runnable from the request to use it inside our connection retry policy below
        Runnable consumeRequest = new Runnable() {
            @Override
            public void run() {
                // Consume the purchase async
                billingClient.consumeAsync(ConsumeParams.newBuilder()
                        .setPurchaseToken(purchaseToken)
                        .build(), onConsumeListener);
            }
        };

        executeServiceRequest(consumeRequest);
    }

    public void startServiceConnection(final Runnable executeOnSuccess) {

        billingClient.startConnection(new BillingClientStateListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.

                    mIsServiceConnected = true;

                    if (executeOnSuccess != null) {
                        executeOnSuccess.run();
                    }


                }

                mBillingClientResponseCode = billingResult.getResponseCode();
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.

                mIsServiceConnected = false;

                billingClient.startConnection(this);
            }
        });
    }

    private void executeServiceRequest(Runnable runnable) {

        Log.d("Billing", "executeServiceRequest");

        if(runnable != null) {

            if (mIsServiceConnected) {
                runnable.run();
            } else {

                Log.d("Billing", "executeServiceRequest: mService is not connected!");
                // If billing service was disconnected, we try to reconnect 1 time.
                // (feel free to introduce your retry policy here).

                //!!! ENTFERNT :
                //startServiceConnection(runnable);
            }
        }
        else{
            Log.d("Billing", "executeServiceRequest: runnable is null!");
        }
    }

    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     */
    private boolean verifyValidSignature(String signedData, String signature) {
        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (BASE_64_ENCODED_PUBLIC_KEY.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please update your app's public key at: "
                    + "BASE_64_ENCODED_PUBLIC_KEY");
        }

        try {
            return Security.verifyPurchase(BASE_64_ENCODED_PUBLIC_KEY, signedData, signature);
        } catch (IOException e) {
            Log.e(TAG, "Got an exception trying to validate a purchase: " + e);
            return false;
        }
    }
    /**
     * Returns the value Billing client response code or BILLING_MANAGER_NOT_INITIALIZED if the
     * clien connection response was not received yet.
     */
    public int getBillingClientResponseCode() {
        return mBillingClientResponseCode;
    }


    public void setBillingListener(BillingListener listener){
        this.listener = listener;
    }

}
