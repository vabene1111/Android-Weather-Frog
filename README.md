# Android Weather Frog

Sometimes Google does not allow the user to create a shortcut to the build in google weather app (also known as the Google weather frog).

This app simply contains an intend to open the google app with the required parameters.

## Intall

An APK for download can be found [here](https://github.com/vabene1111/Android-Weather-Frog/releases). If you cannot install it directly make sure to enable installing of third party apllications.

## Create your own App

If you want to create your own app simply put the following code into your starting activity.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = new Intent("android.intent.action.VIEW");
    intent.setData(Uri.parse("dynact://velour/weather/ProxyActivity"));
    intent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.apps.gsa.velour.DynamicActivityTrampoline");
    startActivity(intent);
    finish();
}
```

