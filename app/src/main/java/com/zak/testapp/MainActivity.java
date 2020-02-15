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
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

import com.afollestad.materialdialogs.MaterialDialog.Builder;

import java.util.Locale;

import static com.zak.testapp.Constants.activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button contactUsButton, countryButton, dialogButton, faqButton, languageButton, locationButton, offersButton, openAccountButton, voiceButton, productsButton, registerUserButton, toolsButton;
    Handler handler;
    private TextView returnedText;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    ImageView imageView1;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    static String language = "ar";

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

        /*if (!checkServiceRunning()) {
            startService(new Intent(MainActivity.this, MyService.class));
        }else{
            setLocale(language);
        }*/
        setXmlReference();
        setListener();


    }

    private void showTransparent() {
        Intent intent = new Intent(this, TransparentActivity.class);
        startActivity(intent);
        startActivityForResult(intent, 2000);


    }

    private void speak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar");
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "كيف يمكنني مساعدتك");

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // mSpeechRecognizerIntent.putExtra(RecognizerIntent.)
        // mSpeechRecognizerIntent.
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());

        // mSpeechRecognizer.setRecognitionListener(this);
        try {
            startActivityForResult(intent, 1000);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == 2000) {
            finishActivity(2000);
        }*/
        switch (requestCode) {
            case 1000:
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(this, "Test " + result.get(0), Toast.LENGTH_LONG).show();
                    if (result.get(0).contains("اتصل بنا") || result.get(0).contains("contact us")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, ContactUsActivity.class);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("الدوله") || result.get(0).contains("country")
                            || result.get(0).contains("دوله")) {
                        Toast.makeText(this, result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, CountryActivity.class);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("الاسئله الشائعه") ||
                            result.get(0).contains("اسئله شائعه") ||
                            result.get(0).contains("frequently asked questions")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, FaqActivity.class);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("اللغه") || result.get(0).contains("language")
                            || result.get(0).contains("لغه")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, LanguageActivity.class);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("المواقع") || result.get(0).contains("location")
                            || result.get(0).contains("مواقع")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, LocationActivity.class);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("عروض") || result.get(0).contains("offers")
                            || result.get(0).contains("عرض") || result.get(0).contains("عروض البنك") || result.get(0).contains("عروض للبنك")
                    ) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, OffersActivity.class);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("فتح حساب جديد") || result.get(0).contains("open new account")
                            || result.get(0).contains("حساب جديد") || result.get(0).contains("فتح حساب")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, OpenAccountActivity.class);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("المنتجات") || result.get(0).contains("products") || result.get(0).contains("منتج")
                            || result.get(0).contains("انتاج") || result.get(0).contains("منتجات")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, ProductsActivity.class);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("اشتراك جديد") || result.get(0).contains("register new user")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, RegisterUserActivity.class);
                        startActivity(intentContactUs);
                    }
                    if (result.get(0).contains("ادوات") || result.get(0).contains("tools")
                            || result.get(0).contains("الادوات")) {
                        Toast.makeText(this, "" + result.get(0), Toast.LENGTH_LONG).show();
                        Intent intentContactUs = new Intent(this, ToolsActivity.class);
                        startActivity(intentContactUs);
                    }


                }
        }
    }

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
        voiceButton = findViewById(R.id.voice);
        dialogButton = findViewById(R.id.voice);


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
        voiceButton.setOnClickListener(this);
        dialogButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog:

                Intent intentTest = new Intent(this, VoiceActivity.class);
                startActivity(intentTest);
                break;
            case R.id.voice:
                //showTransparent();
                speak();
                break;
            case R.id.ContactUs:
                Intent intentContactUs = new Intent(this, ContactUsActivity.class);
                startActivity(intentContactUs);
                break;
            case R.id.Country:
                Intent intentCountry = new Intent(this, CountryActivity.class);
                startActivity(intentCountry);
                break;
            case R.id.FAQ:
                Intent intentFAQ = new Intent(this, FaqActivity.class);
                startActivity(intentFAQ);
                break;
            case R.id.language:
                Intent intentLanguage = new Intent(this, LanguageActivity.class);
                startActivity(intentLanguage);
                break;
            case R.id.Location:
                Intent intentLocation = new Intent(this, LocationActivity.class);
                startActivity(intentLocation);
                break;
            case R.id.offers:
                Intent intentOffers = new Intent(this, OffersActivity.class);
                startActivity(intentOffers);
                break;
            case R.id.OpenAccount:
                Intent intentOpenAccount = new Intent(this, OpenAccountActivity.class);
                startActivity(intentOpenAccount);
                break;
            case R.id.Products:
                Intent intentProducts = new Intent(this, ProductsActivity.class);
                startActivity(intentProducts);
                break;
            case R.id.RegisterUser:
                Intent intentRegisterUser = new Intent(this, RegisterUserActivity.class);
                startActivity(intentRegisterUser);
                break;
            case R.id.Tools:
                Intent intentTools = new Intent(this, ToolsActivity.class);
                startActivity(intentTools);
                break;
            default:
                System.out.println("Do No Thing");
        }
    }

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