package com.zak.testapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class TransparentActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listening_dialog);


    }
}
