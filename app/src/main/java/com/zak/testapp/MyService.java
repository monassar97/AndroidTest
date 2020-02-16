package com.zak.testapp;

import android.Manifest.permission;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.sac.speech.GoogleVoiceTypingDisabledException;
import com.sac.speech.Speech;
import com.sac.speech.SpeechDelegate;
import com.sac.speech.SpeechRecognitionNotAvailable;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import static com.zak.testapp.Constants.activity;

public class MyService extends Service implements SpeechDelegate, Speech.stopDueToDelay {

    public static SpeechDelegate delegate;
    private static String language;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((AudioManager) Objects.requireNonNull(
                        getSystemService(Context.AUDIO_SERVICE))).adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
            } else {
                ((AudioManager) Objects.requireNonNull(
                        getSystemService(Context.AUDIO_SERVICE))).setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }
            /*if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
                ((AudioManager) Objects.requireNonNull(
                        getSystemService(Context.AUDIO_SERVICE))).setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        language = preferences.getString("lang", "ar");

        Speech.init(this);
        Locale locale = new Locale(language);
        Speech.getInstance().setLocale(locale);
        delegate = this;
        Speech.getInstance().setListener(this);

        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
            muteBeepSoundOfRecorder();
        } else {
            System.setProperty("rx.unsafe-disable", "True");
            RxPermissions.getInstance(this).request(permission.RECORD_AUDIO).subscribe(granted -> {
                if (granted) { // Always true pre-M
                    try {//pop up
                        Speech.getInstance().stopTextToSpeech();
                        Speech.getInstance().startListening(null, this);
                    } catch (SpeechRecognitionNotAvailable exc) {
                        //showSpeechNotSupportedDialog();

                    } catch (GoogleVoiceTypingDisabledException exc) {
                        //showEnableGoogleVoiceTyping();
                    }
                } else {
                    Toast.makeText(this, "permission required", Toast.LENGTH_LONG).show();
                }
            });
            muteBeepSoundOfRecorder();
        }
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onStartOfSpeech() {
    }

    @Override
    public void onSpeechRmsChanged(float value) {

    }

    @Override
    public void onSpeechPartialResults(List<String> results) {
        for (String partial : results) {
            Log.d("Result", partial + "");
        }
    }

    @Override
    public void onSpeechResult(String result) {
        Log.d("Result", result + "");
        if (!TextUtils.isEmpty(result)) {
            {

                //  ArrayList<String> result = result.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                Toast.makeText(this, "Test " + result, Toast.LENGTH_LONG).show();
                if (result.contains("اتصل بنا") || result.toLowerCase().contains("contact us")) {
                    Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    Intent i = new Intent();
                    i.setClassName("com.zak.testapp", "com.zak.testapp.ContactUsActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    stopServices();
                    this.onDestroy();
                }
                if (result.contains("الدوله") || result.toLowerCase().contains("country")) {
                    Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName("com.zak.testapp", "com.zak.testapp.CountryActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("الاسئله الشائعه") || result.toLowerCase().contains("frequently asked questions")) {
                    Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName("com.zak.testapp", "com.zak.testapp.FaqActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("اللغه") || result.contains("language")) {
                    Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName("com.zak.testapp", "com.zak.testapp.LanguageActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                  /*  Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    Intent intentContactUs = new Intent(this, LanguageActivity.class);
                    //EditText editText = (EditText) findViewById(R.id.editText);
                    String message = result.toLowerCase();
                    intentContactUs.putExtra("message", message);*/
          /*if (message.contains("arabic")) {
            language = "ar";
            setLocale("ar");
          } else if (message.contains("انجليزيه")) {
            language = "en";
            setLocale("en");

          }*/
                    //startActivity(intentContactUs);
                }

               /* if (result.contains("اللغه") || result.toLowerCase().contains("language")) {
                    Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    Intent intentContactUs = new Intent(this, LanguageActivity.class);
                    //EditText editText = (EditText) findViewById(R.id.editText);
                    String message = result;
                    intentContactUs.putExtra("message", message);
                    if (message.toLowerCase().contains("arabic")) {
                        language = "ar";
                        setLocale("ar");
                        Locale locale = new Locale(language);
                        Speech.getInstance().setLocale(locale);
                    }

                    if (message.contains("انجليزيه")) {
                        language = "en";
                        setLocale("en");
                        Locale locale = new Locale(language);
                        Speech.getInstance().setLocale(locale);

                    }
                      startActivity(intentContactUs);
                }*/

                if (result.contains("مواقع") || result.toLowerCase().contains("location")) {
                    Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName("com.zak.testapp", "com.zak.testapp.LocationActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("عروض") || result.toLowerCase().contains("offers")) {
                    Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName("com.zak.testapp", "com.zak.testapp.OffersActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("حساب جديد") || result.toLowerCase().contains("open new account")) {
                    Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName("com.zak.testapp", "com.zak.testapp.OpenAccountActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("منتجات") || result.toLowerCase().contains("products")) {
                    Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName("com.zak.testapp", "com.zak.testapp.ProductsActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("اشتراك جديد") || result.toLowerCase().contains("register new user")) {
                    Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName("com.zak.testapp", "com.zak.testapp.RegisterUserActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("ادوات") || result.toLowerCase().contains("tools")) {
                    Toast.makeText(this, "" + result, Toast.LENGTH_LONG).show();
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName("com.zak.testapp", "com.zak.testapp.ToolsActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }


            }
        }
    }

    private void stopServices() {
        Speech.getInstance().stopTextToSpeech();
        Speech.getInstance().stopListening();
        Speech.getInstance().shutdown();
    }


    /**
     * Function to remove the beep sound of voice recognizer.
     */
    private void muteBeepSoundOfRecorder() {
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if (amanager != null) {
            amanager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
            amanager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //Restarting the service if it is removed.
        PendingIntent service =
                PendingIntent.getService(getApplicationContext(), new Random().nextInt(),
                        new Intent(getApplicationContext(), MyService.class), PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);
        super.onTaskRemoved(rootIntent);
    }

    public void setLocale(String lang) {
        lang = language;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lang", lang);
        editor.apply();

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(activity, MainActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.finishAffinity();
        activity.startActivity(refresh);
    }

    @Override
    public void onSpecifiedCommandPronounced(String event) {

    }
}