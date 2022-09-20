package com.flashlight.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.flashlight.demo.util.LocaleHelper;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //todo set full screen (no action bar, Navigation button show)
        getWindow().addFlags((WindowManager.LayoutParams.FLAG_FULLSCREEN));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }

}
