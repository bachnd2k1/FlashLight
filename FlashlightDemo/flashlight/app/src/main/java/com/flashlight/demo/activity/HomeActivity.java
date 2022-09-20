package com.flashlight.demo.activity;

import android.Manifest;
//import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
//import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.flashlight.demo.util.PermissionUtil;
import com.flashlight.demo.util.ToastUtil;
import com.flashlight.demo.fragment.CompassFragment;
import com.flashlight.demo.fragment.HelpFragment;
import com.flashlight.demo.fragment.MorseFragment;
import com.flashlight.demo.fragment.SettingFragment;
import com.flashlight.demo.R;
import com.flashlight.demo.dialog.BottomSheetFlashDialog;
import com.flashlight.demo.dialog.ExitAppDialog;
import com.flashlight.demo.handler.FlashModeHandler;
import com.flashlight.demo.handler.NotificationHandler;
import com.flashlight.demo.model.Flashlight;
import com.flashlight.demo.util.Config;
import com.flashlight.demo.util.MyConstants;
import com.flashlight.demo.util.PreferencesHelper;

import java.util.HashMap;
import java.util.Objects;

public class HomeActivity extends BaseActivity {

    private static final int HOME_FOCUS = 0;
    private static final int MORSE_FOCUS = 1;
    private static final int HELP_FOCUS = 2;
    private static final int COMPASS_FOCUS = 3;
    private static final int SETTING_FOCUS = 4;

    private Context mContext;

    private ImageView  imAddTorch, imFlashFlicker, imFlashOn, imFlashOff;
    private TextView tvTitleMain;
    private ConstraintLayout clViewHome, clAction;

    private LinearLayout llHome, llMorse, llHelp, llCompass, llSetting, llAdsViewParent;
    private ImageView imHome, imMorse, imHelp, imShare, imSetting;
    private TextView tvHome, tvMorse, tvHelp, tvCompass, tvSetting;

    HashMap<String, Object> firebaseDefaultMap;

//    private String UPDATE_URL;
    private int selectedView = 0;

    private MorseFragment morseFragment;
    private HelpFragment helpFragment;
    private CompassFragment compassFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext = this;

//        UPDATE_URL = "http://play.google.com/store/apps/details?id=" + getPackageName();
        firebaseDefaultMap = new HashMap<>();

        showAds();
        initView();
        initData();
        setListener();
        setTypeFlashlight();
    }

    private void initView() {
        imFlashOn = findViewById(R.id.imFlashOn);
        imFlashOff = findViewById(R.id.imFlashOff);
        imAddTorch = findViewById(R.id.imAddTorch);
        imFlashFlicker = findViewById(R.id.imFlashFlicker);
        tvTitleMain = findViewById(R.id.tvTitleMain);
        clAction = findViewById(R.id.clAction);
        clViewHome = findViewById(R.id.clViewHome);

        llHome = findViewById(R.id.llHome);
        llMorse = findViewById(R.id.llMorse);
        llHelp = findViewById(R.id.llHelp);
        llCompass = findViewById(R.id.llCompass);
        llSetting = findViewById(R.id.llSetting);

        imHelp = findViewById(R.id.imHelp);
        imMorse = findViewById(R.id.imMorse);
        imHome = findViewById(R.id.imHome);
        imShare = findViewById(R.id.imCompass);
        imSetting = findViewById(R.id.imSetting);

        tvHelp = findViewById(R.id.tvHelp);
        tvMorse = findViewById(R.id.tvMorse);
        tvHome = findViewById(R.id.tvHome);
        tvCompass = findViewById(R.id.tvCompass);
        tvSetting = findViewById(R.id.tvSetting);
        llAdsViewParent = findViewById(R.id.llAdsViewParent);
        setSelectedView(HOME_FOCUS);

    }

    private void initData() {
        if (!isFlashSupported()) {
            ToastUtil.longToast(getApplicationContext(), getString(R.string.not_support));
        } else {
            setSwitchListener();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    PermissionUtil.requestPermission(HomeActivity.this, Config.RequestCode.PERMISSION_CAMERA, Manifest.permission.CAMERA);
                } else {
                    Log.i("CheckPermission", "Permission OK");
                    setupCamera();
                }
            } else {
                Log.i("CheckPermission", "<M");
                setupCamera();
            }
        }

        setSwitchMode(Flashlight.getInstance().isFlashLightOn());

    }

    private void setListener() {

        //todo show morse fragment
        llMorse.setOnClickListener(v -> {
            setSelectedView(MORSE_FOCUS);
            if (morseFragment==null) {
                morseFragment = new MorseFragment();
            }
            onChangeFragment(morseFragment);
        });

        //todo show help fragment
        llHelp.setOnClickListener(v -> {
            setSelectedView(HELP_FOCUS);
            if (helpFragment==null) {
                helpFragment = new HelpFragment();
            }
            onChangeFragment(helpFragment);
        });

        //todo show compass fragment
        llCompass.setOnClickListener(v -> {
            setSelectedView(COMPASS_FOCUS);
            if (compassFragment==null) {
                compassFragment = new CompassFragment();
            }
            onChangeFragment(compassFragment);
        });

        //todo show setting fragment
        llSetting.setOnClickListener(v -> {
            setSelectedView(SETTING_FOCUS);
            if (settingFragment==null) {
                settingFragment = new SettingFragment(getSupportFragmentManager());
            }
            onChangeFragment(settingFragment);
        });

        //todo show home
        llHome.setOnClickListener(v -> {
            setSelectedView(HOME_FOCUS);
            clAction.setVisibility(View.GONE);
            clViewHome.setVisibility(View.VISIBLE);
        });


        imAddTorch.setOnClickListener(v -> startActivity(new Intent(mContext, ShopFlashlightActivity.class)));

        imFlashFlicker.setOnClickListener(v -> {
            BottomSheetFlashDialog flashDialog = new BottomSheetFlashDialog();
            flashDialog.show(getSupportFragmentManager(), "flash flicker");
        });
    }


//    private void rateApp() {
//        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
//        try {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//        } catch (android.content.ActivityNotFoundException anfe) {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//        }
//    }

    private void onChangeFragment(Fragment fragment) {
        clViewHome.setVisibility(View.GONE);
        clAction.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.clAction, fragment).commit();
        getSupportFragmentManager().executePendingTransactions();
    }

//    @SuppressLint("StringFormatInvalid")
//    private void share() {
//        //MyTracking.sendCustomAction(TrackingConstants.SCREEN.HOME, TrackingConstants.ACTION.SHARE_APP);
//        Intent intent = new Intent("android.intent.action.SEND");
//        intent.setType("text/plain");
//        String appName = getResources().getString(R.string.app_name);
//        String url = UPDATE_URL;
//        intent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.share_message, appName, url));
//        startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
//    }

    /**
     * @param id set view is selected
     */
    public void setSelectedView(int id) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (imHelp != null && imMorse != null && imShare != null && imHome != null && imSetting != null) {
            switch (id) {
                //todo set view when home click
                case HOME_FOCUS:
                    selectedView = HOME_FOCUS;
                    imHome.setSelected(true);
                    imMorse.setSelected(false);
                    imHelp.setSelected(false);
                    imShare.setSelected(false);
                    imSetting.setSelected(false);

                    tvHome.setTextColor(Color.parseColor("#FFDA00"));
                    tvMorse.setTextColor(Color.parseColor("#9D9D9D"));
                    tvHelp.setTextColor(Color.parseColor("#9D9D9D"));
                    tvCompass.setTextColor(Color.parseColor("#9D9D9D"));
                    tvSetting.setTextColor(Color.parseColor("#9D9D9D"));
                    break;

                //todo set view when morse click
                case MORSE_FOCUS:
                    selectedView = MORSE_FOCUS;
                    imHome.setSelected(false);
                    imMorse.setSelected(true);
                    imHelp.setSelected(false);
                    imShare.setSelected(false);
                    imSetting.setSelected(false);

                    tvHome.setTextColor(Color.parseColor("#9D9D9D"));
                    tvMorse.setTextColor(Color.parseColor("#FFDA00"));
                    tvHelp.setTextColor(Color.parseColor("#9D9D9D"));
                    tvCompass.setTextColor(Color.parseColor("#9D9D9D"));
                    tvSetting.setTextColor(Color.parseColor("#9D9D9D"));
                    break;

                //todo set view when help click
                case HELP_FOCUS:
                    selectedView = HELP_FOCUS;
                    imHome.setSelected(false);
                    imMorse.setSelected(false);
                    imHelp.setSelected(true);
                    imShare.setSelected(false);
                    imSetting.setSelected(false);

                    tvHome.setTextColor(Color.parseColor("#9D9D9D"));
                    tvMorse.setTextColor(Color.parseColor("#9D9D9D"));
                    tvHelp.setTextColor(Color.parseColor("#FFDA00"));
                    tvCompass.setTextColor(Color.parseColor("#9D9D9D"));
                    tvSetting.setTextColor(Color.parseColor("#9D9D9D"));
                    break;

                //todo set view when compass click
                case COMPASS_FOCUS:
                    selectedView = COMPASS_FOCUS;
                    imHome.setSelected(false);
                    imMorse.setSelected(false);
                    imHelp.setSelected(false);
                    imShare.setSelected(true);
                    imSetting.setSelected(false);

                    tvHome.setTextColor(Color.parseColor("#9D9D9D"));
                    tvMorse.setTextColor(Color.parseColor("#9D9D9D"));
                    tvHelp.setTextColor(Color.parseColor("#9D9D9D"));
                    tvCompass.setTextColor(Color.parseColor("#FFDA00"));
                    tvSetting.setTextColor(Color.parseColor("#9D9D9D"));
                    break;

                //todo set view when setting click
                case SETTING_FOCUS:
                    selectedView = SETTING_FOCUS;
                    imHome.setSelected(false);
                    imMorse.setSelected(false);
                    imHelp.setSelected(false);
                    imShare.setSelected(false);
                    imSetting.setSelected(true);

                    tvHome.setTextColor(Color.parseColor("#9D9D9D"));
                    tvMorse.setTextColor(Color.parseColor("#9D9D9D"));
                    tvHelp.setTextColor(Color.parseColor("#9D9D9D"));
                    tvCompass.setTextColor(Color.parseColor("#9D9D9D"));
                    tvSetting.setTextColor(Color.parseColor("#FFDA00"));
                    break;
            }
        }
    }


    /**
     * @return true when device support flash
     */
    private boolean isFlashSupported() {
        PackageManager pm = getPackageManager();
        if (pm != null)
            return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        else
            return false;
    }

    /**
     * set camera to use flash
     */
    public void setupCamera() {
        String TAG = "setupCamera()";

        if (Flashlight.getInstance().getCamera() != null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                if (camManager == null) {
                    Log.e(TAG, "camManager == null");
                    ToastUtil.longToast(getApplicationContext(), getString(R.string.failed_camera));
                    return;
                }
                Flashlight.getInstance().setCameraManager(camManager);
                String[] array = camManager.getCameraIdList();
                Log.i(TAG, "length = " + array.length);
                if (array.length > 0)
                    Flashlight.getInstance().setCameraId(camManager.getCameraIdList()[0]);
                else
                    ToastUtil.longToast(getApplicationContext(), getString(R.string.failed_camera));

            } catch (Exception e) {
                ToastUtil.longToast(getApplicationContext(), getString(R.string.failed_camera));
                Log.e(TAG, "Error: " + e.toString());
            }

        } else {
            Camera camera;
            try {
                camera = Camera.open();
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
                ToastUtil.longToast(getApplicationContext(), getString(R.string.failed_camera));
                return;
            }
            if (camera == null) {
                Log.e(TAG, "camera == null");
                ToastUtil.longToast(getApplicationContext(), getString(R.string.failed_camera));
                return;
            }
            Flashlight.getInstance().setCamera(camera);
            Flashlight.getInstance().setParameters(camera.getParameters());
        }
    }

    private void setSwitchListener() {
        imFlashOn.setOnClickListener(v -> {
            if (isFinishing()) return;
                setFlashMode(false);
                setSwitchMode(false);

        });

        imFlashOff.setOnClickListener(v -> {
            if (isFinishing()) return;
            setFlashMode(true);
            setSwitchMode(true);

        });

        setFlashMode(Flashlight.getInstance().isFlashLightOn());
    }

    /**
     * turn on/off flash
     * @param isFlashOn true = flashlight is on
     */
    private void setFlashMode(boolean isFlashOn) {
        if (isFlashOn) {
            if (Flashlight.getInstance().isFlashLightOn()) return;

            //turn on flashlight_1
            Flashlight.getInstance().setFlashLightOn(true);
            FlashModeHandler.getInstance().setMode((Activity) mContext);
            setSwitchMode(true);
            new Thread(() -> {
                Flashlight.getInstance().playToggleSound(getApplicationContext());
                NotificationHandler handler = new NotificationHandler(getApplicationContext());
                handler.addNotify();
            }).start();
            llAdsViewParent.setVisibility(View.GONE);
            tvTitleMain.setTextColor(Color.parseColor("#FFFFFF"));

        } else {
            if (!Flashlight.getInstance().isFlashLightOn()) return;

            //turn off flashlight_1
            Flashlight.getInstance().setFlashLightOn(false);
            Flashlight.getInstance().toggle(false);
            setSwitchMode(false);
            new Thread(() -> {
                Flashlight.getInstance().playToggleSound(getApplicationContext());
                NotificationHandler handler = new NotificationHandler(getApplicationContext());
                handler.addNotify();
            }).start();
            llAdsViewParent.setVisibility(View.VISIBLE);
            tvTitleMain.setTextColor(Color.parseColor("#6A6A6A"));
        }

    }


    /**
     * todo change UI when flash on/off
     * @param lightOn true is flash on
     */
    private void setSwitchMode(boolean lightOn) {
        if (lightOn) {
            imFlashOn.setVisibility(View.VISIBLE);
            imFlashOff.setVisibility(View.GONE);
        } else {
            imFlashOn.setVisibility(View.GONE);
            imFlashOff.setVisibility(View.VISIBLE);
        }
    }


    /**
     * todo set type for flashlight (image flashlight)
     */
    private void setTypeFlashlight() {
        String typeSwitchOn = PreferencesHelper.getInstances().getStringSPr(MyConstants.KEY_SWITCH_ON_TYPE, mContext); // todo get path image flashlight on type from SharePreference
        String typeSwitchOff = PreferencesHelper.getInstances().getStringSPr(MyConstants.KEY_SWITCH_OFF_TYPE, mContext); // todo get path image flashlight off type from SharePreference
        if (!typeSwitchOff.equals("null") && !typeSwitchOn.equals("null") ) {
            imFlashOn.setImageDrawable(Drawable.createFromPath(typeSwitchOn));
            imFlashOff.setImageDrawable(Drawable.createFromPath(typeSwitchOff));
        }
    }

    private void showAds() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Config.RequestCode.PERMISSION_CAMERA) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ToastUtil.longToast(getApplicationContext(), getString(R.string.warning_request_permission));
                setFlashMode(false);
                PermissionUtil.checkPermissionRationale((Activity) mContext,
                        Manifest.permission.CAMERA);

            } else {
                setupCamera();
                if (Flashlight.getInstance().isFlashLightOn())
                    FlashModeHandler.getInstance().setMode((Activity) mContext);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTypeFlashlight();
        setSwitchMode(Flashlight.getInstance().isFlashLightOn());
        llAdsViewParent.setVisibility(View.VISIBLE);

        if (Flashlight.getInstance().isFlashLightOn()) {
            // Set switch to ON mode position
            setFlashMode(true);
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                PermissionUtil.requestPermission((Activity) mContext, Config.RequestCode.PERMISSION_CAMERA,
                        Manifest.permission.CAMERA);
            }  // do something

        } else {
            // Set switch to OFF mode position
            setFlashMode(false);
        }
        NotificationHandler handler = new NotificationHandler(getApplicationContext());
        handler.addNotify();
    }

    private void confirmExit() {
        ExitAppDialog exitAppDialog = new ExitAppDialog(R.string.text_exit_app_title, true);
        exitAppDialog.show(getSupportFragmentManager(), "exit app");
        exitAppDialog.setExitDialogListener(new ExitAppDialog.AppDialogListener() {
            @Override
            public void OnYesClickListener() {
                HomeActivity.super.onBackPressed();
            }

            @Override
            public void OnNoClickListener() {
                exitAppDialog.dismiss();
            }

            @Override
            public void OnCloseDialogClickListener() {
                exitAppDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (selectedView == 0) {
            confirmExit();
        } else {
            setSelectedView(0);
            clViewHome.setVisibility(View.VISIBLE);
            clAction.setVisibility(View.GONE);
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().executePendingTransactions();
            clAction.removeAllViews();
        }
    }

    @Override
    protected void onDestroy() {
        // to stop the listener and save battery

        if (!Flashlight.getInstance().isFlashLightOn()) {
            if (Flashlight.getInstance().getCamera() != null) {
                Flashlight.getInstance().getCamera().stopPreview();
                Flashlight.getInstance().getCamera().release();
                Flashlight.getInstance().setInstance(null);
            }
        }
        FlashModeHandler.getInstance().setInstance(null);
        NotificationHandler handler = new NotificationHandler(getApplicationContext());
        handler.addNotify();
        super.onDestroy();
    }
}
