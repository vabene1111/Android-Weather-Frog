package de.droidenschmiede.weather;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import de.droidenschmiede.weather.billing.BillingManager;
import de.droidenschmiede.weather.billing.PurchaseManager;
import de.droidenschmiede.weather.dialogs.AboutTyp;

public class MainActivity extends AppCompatActivity {

    public Button btnWeather;
    public Button btnDonate;
    public Button btnRate;

    public BillingManager billingManager;
    public PurchaseManager purchaseManager;
    public AdManager adManager;
    public DialogManager dialogManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        billingManager = new BillingManager(this);
        purchaseManager = new PurchaseManager(this);
        dialogManager = new DialogManager(this);

        billingManager.initBilling();

        adManager.init();

        btnWeather = findViewById(R.id.btn_main_openWeather);
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWeatherApp();
            }
        });

        btnDonate = findViewById(R.id.btn_main_donate);
        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogManager.showDonateDialog();
            }
        });

        btnRate = findViewById(R.id.btn_main_rate);
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_main_github:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/vabene1111/Android-Weather-Frog"));
                startActivity(browserIntent);
                return true;
            case R.id.mi_main_donate:
                dialogManager.showDonateDialog();
                return true;
            case R.id.mi_main_legal:
                dialogManager.showAboutDialog(AboutTyp.IMPRESSUM);
                return true;
            case R.id.mi_main_info:
                dialogManager.showAboutDialog(AboutTyp.INFO);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openWeatherApp(){

        try{

            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("dynact://velour/weather/ProxyActivity"));
            intent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.apps.gsa.velour.DynamicActivityTrampoline");
            startActivity(intent);
            finish();

        } catch ( ActivityNotFoundException e) {
            e.printStackTrace();

            Toast.makeText(this, getResources().getText(R.string.activity_not_found), Toast.LENGTH_LONG).show();
        }

    }
}
