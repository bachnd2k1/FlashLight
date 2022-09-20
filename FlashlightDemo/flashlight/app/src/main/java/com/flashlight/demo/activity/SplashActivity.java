package com.flashlight.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.LinearLayout;

import com.flashlight.demo.R;
//import com.flashlight.demo.ads.Advertisement;
//import com.flashlight.demo.fabric.InterstitialAdsUtils;
import com.flashlight.demo.util.MyConstants;
import com.flashlight.demo.util.MyUtils;
import com.flashlight.demo.util.PreferencesHelper;
public class SplashActivity extends BaseActivity {

    private Context mContext;

    private LinearLayout llAdsViewParent;

    private int countOpenApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        llAdsViewParent = findViewById(R.id.llAdsViewParent);

        countOpenApp = PreferencesHelper.getInstances().getCountOpenApp(MyConstants.COUNT_OPEN_APP, mContext);
        PreferencesHelper.getInstances().setCountOpenApp(MyConstants.COUNT_OPEN_APP, countOpenApp + 1, mContext);

        new Handler().postDelayed(this::nextScreen, 3000);
    }

    private void nextScreen() {
        int countPopupOpen = PreferencesHelper.getInstances().getTimeShowPopupOpen(MyConstants.COUNT_POPUP_OPEN, mContext);
        Intent intentMain = new Intent(SplashActivity.this,
                HomeActivity.class);
        if (countPopupOpen != 0) {
            intentMain.putExtra(MyConstants.COUNT_OPEN_APP, countOpenApp % countPopupOpen == 0);
        } else intentMain.putExtra(MyConstants.COUNT_OPEN_APP, countOpenApp % 3 == 0);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentMain);
        finish();
    }

}
