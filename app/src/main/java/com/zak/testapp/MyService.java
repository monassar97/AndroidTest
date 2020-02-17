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
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.sac.speech.GoogleVoiceTypingDisabledException;
import com.sac.speech.Speech;
import com.sac.speech.SpeechDelegate;
import com.sac.speech.SpeechRecognitionNotAvailable;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class MyService extends Service implements SpeechDelegate, Speech.stopDueToDelay {

    public static SpeechDelegate delegate;
    private static String language = "ar";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((AudioManager) Objects.requireNonNull(
                        getSystemService(Context.AUDIO_SERVICE))).adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
            } else {
                ((AudioManager) Objects.requireNonNull(
                        getSystemService(Context.AUDIO_SERVICE))).setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                if (granted) {
                    try {
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
                Toast.makeText(this, "Test " + result, Toast.LENGTH_LONG).show();
                if (result.contains("اتصل بنا") || result.toLowerCase().contains("contact us")) {
                    Intent i = new Intent();
                    i.setClassName(this.getString(R.string.packageName), this.getString(R.string.contactUsActivity));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    stopServices();
                    this.onDestroy();
                }
                if (result.contains("الدوله") || result.contains("country")
                        || result.contains("دوله")) {
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName(this.getString(R.string.packageName), this.getString(R.string.countryActivity));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("الاسئله الشائعه") ||
                        result.contains("اسئله شائعه") ||
                        result.contains("frequently asked questions")) {
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName(this.getString(R.string.packageName), this.getString(R.string.faqActivity));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("اللغه") || result.contains("language") || result.contains("لغه")) {
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName(this.getString(R.string.packageName), this.getString(R.string.languageActivity));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);

                }

                if (result.contains("مواقع") || result.toLowerCase().contains("location")
                        || result.contains("مواقع")) {
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName(this.getString(R.string.packageName), this.getString(R.string.locationActivity));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("عروض") || result.contains("offers")
                        || result.contains("عرض") || result.contains("عروض البنك") || result.contains("عروض للبنك")) {
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName(this.getString(R.string.packageName), this.getString(R.string.offersActivity));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("حساب جديد") || result.toLowerCase().contains("open new account")
                        || result.contains("حساب جديد") || result.contains("فتح حساب") || result.contains("منتج")
                        || result.contains("انتاج") || result.contains("منتجات")) {
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName(this.getString(R.string.packageName), this.getString(R.string.openAccountActivity));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("منتجات") || result.toLowerCase().contains("products")) {
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName(this.getString(R.string.packageName), this.getString(R.string.productsActivity));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("اشتراك جديد") || result.toLowerCase().contains("register new user")) {
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName(this.getString(R.string.packageName), this.getString(R.string.registerUserActivity));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getBaseContext().startActivity(i);
                    this.onDestroy();
                }
                if (result.contains("ادوات") || result.toLowerCase().contains("tools") || result.contains("الادوات")) {
                    stopServices();
                    Intent i = new Intent();
                    i.setClassName(this.getString(R.string.packageName), this.getString(R.string.toolsActivity));
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

    @Override
    public void onSpecifiedCommandPronounced(String event) {

    }
}