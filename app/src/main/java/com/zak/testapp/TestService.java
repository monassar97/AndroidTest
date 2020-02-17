package com.zak.testapp;

import android.Manifest;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.sac.speech.GoogleVoiceTypingDisabledException;
import com.sac.speech.Speech;
import com.sac.speech.SpeechDelegate;
import com.sac.speech.SpeechRecognitionNotAvailable;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;
import java.util.Locale;

public class TestService implements SpeechDelegate, Speech.stopDueToDelay {

    public static SpeechDelegate delegate;

    private static MessageResult messageResult;
    private static String language = "ar";
    private final Context context;

    public TestService(Context applicationContext) {
        this.context = applicationContext;
    }

    public void startListen() {
        delegate = this;
        Speech.getInstance().setListener(this);
        Locale locale = new Locale(language);
        Speech.getInstance().setLocale(locale);
        if (Speech.getInstance().isListening()) {
            Speech.getInstance().stopListening();
            muteBeepSoundOfRecorder();
        } else {
            System.setProperty("rx.unsafe-disable", "True");
            RxPermissions.getInstance(context).request(Manifest.permission.RECORD_AUDIO).subscribe(granted -> {
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
                    Toast.makeText(context, "permission required", Toast.LENGTH_LONG).show();
                }
            });
            muteBeepSoundOfRecorder();
        }

    }

    @Override
    public void onSpecifiedCommandPronounced(String event) {

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
        messageResult = new MessageResult(result, true);
    }


    public MessageResult getMessageResult() {
        return messageResult;
    }

    public void stopServices() {
        Speech.getInstance().stopTextToSpeech();
        Speech.getInstance().stopListening();
        Speech.getInstance().shutdown();
    }

    private void muteBeepSoundOfRecorder() {

        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (manager != null) {
            manager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
            manager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
            manager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            manager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
            manager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
        }
    }
}