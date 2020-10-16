package de.droidenschmiede.weather;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AdManager {

    public MainActivity m;

    public boolean pauseAdReload = false;
    public int REFRESH_RATE_IN_SECONDS = 5;
    public CardView cardAd;
    public AdView mAdView;
    public final Handler refreshHandler = new Handler();
    public final Runnable refreshRunnable = new RefreshRunnable();
    public AdRequest adRequest;

    public AdManager(MainActivity m){
        this.m = m;

        init();
    }

    public void init(){
        MobileAds.initialize(m, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadBanner1();
            }
        });


    }

    public void pauseAds(){
        pauseAdReload = true;
    }

    public void loadBanner1(){

        pauseAdReload = false;

        cardAd = m.findViewById(R.id.card_banner_ad);

        cardAd.setVisibility(View.GONE);

        mAdView = m.findViewById(R.id.adView);
        adRequest = new AdRequest.Builder()
                .addTestDevice("9AF9280FE7C9BA01E232DE17BBF355DC")
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                cardAd.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                if(pauseAdReload == false){
                    refreshHandler.removeCallbacks(refreshRunnable);
                    refreshHandler.postDelayed(refreshRunnable, REFRESH_RATE_IN_SECONDS * 1000);
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                cardAd.setVisibility(View.GONE);

                if(pauseAdReload == false){
                    refreshHandler.removeCallbacks(refreshRunnable);
                    refreshHandler.postDelayed(refreshRunnable, REFRESH_RATE_IN_SECONDS * 1000);
                }
            }
        });

        mAdView.loadAd(adRequest);
    }

    private class RefreshRunnable implements Runnable {
        @Override
        public void run() {
            mAdView.loadAd(adRequest);
        }
    }
}
