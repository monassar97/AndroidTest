package com.zak.testapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import com.afollestad.materialdialogs.MaterialDialog.Builder;

import java.util.Locale;

import static com.zak.testapp.Constants.activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button contactUsButton, countryButton, faqButton, languageButton, locationButton, offersButton, openAccountButton, productsButton, registerUserButton, toolsButton;
    Handler handler;
    //private SpeechRecognizer mSpeechRecognizer;
    //private Intent mSpeechRecognizerIntent;
static  String language="ar";
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String lang = preferences.getString("lang", "ar");
        language = lang;
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        //    handler = new Handler();
        // final int delay = 10000; //milliseconds

     //   speak();
       /* handler.postDelayed(new Runnable() {
            public void run() {
                speak();
                handler.postDelayed(this, delay);
            }
        }, delay);*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        activity = this;
        //enableAutoStart();

        if (!checkServiceRunning()) {
            startService(new Intent(MainActivity.this, MyService.class));
        }else{
            setLocale(language);
        }
        setXmlReference();
        setListener();


    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi Speak Something");



       /* mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());

        mSpeechRecognizer.setRecognitionListener(this);
*/
        try {
            startActivityForResult(intent, 1000);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) *//*{
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1000:
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(this, "Test " + result.get(0), Toast.LENGTH_LONG).show();
                    if (result.get(0).contains("اتصل بنا") || result.get(0).contains("contact us")) {
                        handler.removeCallbacksAndMessages(null);
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, ContactUsActivity.class);
                        //EditText editText = (EditText) findViewById(R.id.editText);
                        //String message = editText.getText().toString();
                        //intent.putExtra("message", message);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("الدوله") || result.get(0).contains("country")) {
                        Toast.makeText(this, result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, CountryActivity.class);
                        //EditText editText = (EditText) findViewById(R.id.editText);
                        //String message = editText.getText().toString();
                        //intent.putExtra("message", message);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("الاسئله الشائعه") || result.get(0).contains("frequently asked questions")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, FaqActivity.class);
                        //EditText editText = (EditText) findViewById(R.id.editText);
                        //String message = editText.getText().toString();
                        //intent.putExtra("message", message);
                        startActivity(intentContactUs);
                    }
                    *//*if (result.get(0).contains("اللغه") || result.get(0).contains("language")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, LanguageActivity.class);
                        //EditText editText = (EditText) findViewById(R.id.editText);
                        String message = result.get(0).toLowerCase();
                        intentContactUs.putExtra("message", message);
                        if (message.contains("arabic")) {
                            language = "ar";
                            setLocale("ar");

                        } else if (message.contains("انجليزيه")) {
                            language = "en";
                            setLocale("en");

                        }
                          startActivity(intentContactUs);
                    }*//*
                    if (result.get(0).contains("المواقع") || result.get(0).contains("location")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, LocationActivity.class);
                        //EditText editText = (EditText) findViewById(R.id.editText);
                        //String message = editText.getText().toString();
                        //intent.putExtra("message", message);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("عروض") || result.get(0).contains("offers")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, OffersActivity.class);
                        //EditText editText = (EditText) findViewById(R.id.editText);
                        //String message = editText.getText().toString();
                        //intent.putExtra("message", message);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("فتح حساب جديد") || result.get(0).contains("open new account")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, OpenAccountActivity.class);
                        //EditText editText = (EditText) findViewById(R.id.editText);
                        //String message = editText.getText().toString();
                        //intent.putExtra("message", message);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("المنتجات") || result.get(0).contains("products")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, ProductsActivity.class);
                        //EditText editText = (EditText) findViewById(R.id.editText);
                        //String message = editText.getText().toString();
                        //intent.putExtra("message", message);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("اشتراك جديد") || result.get(0).contains("register new user")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, RegisterUserActivity.class);
                        //EditText editText = (EditText) findViewById(R.id.editText);
                        //String message = editText.getText().toString();
                        //intent.putExtra("message", message);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("ادوات") || result.get(0).contains("tools")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, ToolsActivity.class);
                        //EditText editText = (EditText) findViewById(R.id.editText);
                        //String message = editText.getText().toString();
                        //intent.putExtra("message", message);
                        startActivity(intentContactUs);
                    }


                } else {
                 *//*   handler.postDelayed(new Runnable() {
                        public void run() {
                            speak();
                            handler.postDelayed(this, 10000);
                        }
                    }, 10000);*//*
                }
        }
    }*/

    private void setXmlReference() {
        contactUsButton = findViewById(R.id.ContactUs);
        countryButton = findViewById(R.id.Country);
        faqButton = findViewById(R.id.FAQ);
        languageButton = findViewById(R.id.language);
        locationButton = findViewById(R.id.Location);
        offersButton = findViewById(R.id.offers);
        openAccountButton = findViewById(R.id.OpenAccount);
        productsButton = findViewById(R.id.Products);
        registerUserButton = findViewById(R.id.RegisterUser);
        toolsButton = findViewById(R.id.Tools);

    }

    private void setListener() {
        contactUsButton.setOnClickListener(this);
        countryButton.setOnClickListener(this);
        faqButton.setOnClickListener(this);
        languageButton.setOnClickListener(this);
        locationButton.setOnClickListener(this);
        offersButton.setOnClickListener(this);
        openAccountButton.setOnClickListener(this);
        productsButton.setOnClickListener(this);
        registerUserButton.setOnClickListener(this);
        toolsButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ContactUs:
                Intent intentContactUs = new Intent(this, ContactUsActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra("message", message);
                startActivity(intentContactUs);
                break;
            case R.id.Country:
                Intent intentCountry = new Intent(this, CountryActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra("message", message);
                startActivity(intentCountry);
                break;
            case R.id.FAQ:
                Intent intentFAQ = new Intent(this, FaqActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra("message", message);
                startActivity(intentFAQ);
                break;
            case R.id.language:
                Intent intentLanguage = new Intent(this, LanguageActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra("message", message);
                startActivity(intentLanguage);
                break;
            case R.id.Location:
                Intent intentLocation = new Intent(this, LocationActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra("message", message);
                startActivity(intentLocation);
                break;
            case R.id.offers:
                Intent intentOffers = new Intent(this, OffersActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra("message", message);
                startActivity(intentOffers);
                break;
            case R.id.OpenAccount:
                Intent intentOpenAccount = new Intent(this, OpenAccountActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra("message", message);
                startActivity(intentOpenAccount);
                break;
            case R.id.Products:
                Intent intentProducts = new Intent(this, ProductsActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra("message", message);
                startActivity(intentProducts);
                break;
            case R.id.RegisterUser:
                Intent intentRegisterUser = new Intent(this, RegisterUserActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra("message", message);
                startActivity(intentRegisterUser);
                break;
            case R.id.Tools:
                Intent intentTools = new Intent(this, ToolsActivity.class);
                //EditText editText = (EditText) findViewById(R.id.editText);
                //String message = editText.getText().toString();
                //intent.putExtra("message", message);
                startActivity(intentTools);
                break;
            default:
                System.out.println("Do No Thing");
        }
    }

    /*@Override
    public void onReadyForSpeech(Bundle bundle) {
        Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();;
    }

    @Override
    public void onBeginningOfSpeech() {
        Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();;

    }

    @Override
    public void onRmsChanged(float v) {
        Toast.makeText(getApplicationContext(),"3",Toast.LENGTH_LONG).show();;

    }

    @Override
    public void onBufferReceived(byte[] bytes) {
        Toast.makeText(getApplicationContext(),"4",Toast.LENGTH_LONG).show();;

    }

    @Override
    public void onEndOfSpeech() {
        Toast.makeText(getApplicationContext(),"5",Toast.LENGTH_LONG).show();;

    }

    @Override
    public void onError(int i) {
        Toast.makeText(getApplicationContext(),"6",Toast.LENGTH_LONG).show();;

    }

    @Override
    public void onResults(Bundle bundle) {
        Toast.makeText(getApplicationContext(),"7",Toast.LENGTH_LONG).show();;

    }

    @Override
    public void onPartialResults(Bundle bundle) {
        Toast.makeText(getApplicationContext(),"8",Toast.LENGTH_LONG).show();;

    }

    @Override
    public void onEvent(int i, Bundle bundle) {
        Toast.makeText(getApplicationContext(),"9",Toast.LENGTH_LONG).show();;

    }*/

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

    private void enableAutoStart() {
        for (Intent intent : Constants.AUTO_START_INTENTS) {
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                new Builder(this).title("Enable Auto Start")
                        .content("permission")
                        .positiveText("allow")
                        .onPositive((dialog, which) -> {
                            try {
                                for (Intent intent1 : Constants.AUTO_START_INTENTS)
                                    if (getPackageManager().resolveActivity(intent1, PackageManager.MATCH_DEFAULT_ONLY)
                                            != null) {
                                        startActivity(intent1);
                                        break;
                                    }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        })
                        .show();
                break;
            }
        }
    }


    public boolean checkServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                    Integer.MAX_VALUE)) {
                if (getString(R.string.my_service_name).equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
