package com.zak.testapp;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import java.util.Locale;

public class RSSPullService extends IntentService {
    Handler handler;
    Intent workIntent;
    public RSSPullService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
      //  String dataString = workIntent.getDataString();
        // Do work here, based on the contents of dataString

    }

}