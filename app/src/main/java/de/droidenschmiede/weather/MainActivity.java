package de.droidenschmiede.weather;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btnWeather = findViewById(R.id.btn_main_openWeather);
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWeatherApp();
            }
        });

    }

    private void openWeatherApp(){
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("dynact://velour/weather/ProxyActivity"));
        intent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.apps.gsa.velour.DynamicActivityTrampoline");
        startActivity(intent);
        finish();
    }
}
