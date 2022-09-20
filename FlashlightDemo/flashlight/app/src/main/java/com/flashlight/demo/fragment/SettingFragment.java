package com.flashlight.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.flashlight.demo.BuildConfig;
import com.flashlight.demo.util.PreferencesHelper;
import com.flashlight.demo.R;
import com.flashlight.demo.dialog.ChangeLanguageDialogFragment;
import com.flashlight.demo.handler.NotificationHandler;
import com.flashlight.demo.util.Config;

import java.util.Locale;

public class SettingFragment extends Fragment {

    private Context mContext;
    private FragmentManager fragmentManager;

    public SettingFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private TextView tvLanguage, tvVersion;
    private LinearLayout llLanguage, llPolicy, llVersionInfo;
    private ImageView imSound, imStatusBar, imIconSound;
    private RelativeLayout rlAdsView;
    private boolean isSoundOn = true, isStatusBarOn = false;
    
    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle) {
        View view = layoutinflater.inflate(R.layout.fragment_setting, viewgroup, false);
        mContext = view.getContext();
        initView(view);
        setListener();
        
        return view;
    }

    private void initView(View v) {
        rlAdsView = v.findViewById(R.id.rlAdsView);
//        PreLoadBannerAdsUtils.getInstance().showSmartBanner(mContext, rlAdsView);
        tvLanguage = v.findViewById(R.id.tvLanguage);
        tvVersion = v.findViewById(R.id.tvVersion);
        llLanguage = v.findViewById(R.id.llLanguage);
        llPolicy = v.findViewById(R.id.llPrivacy);
        llVersionInfo = v.findViewById(R.id.llVersion);
        imSound = v.findViewById(R.id.imSound);
        imStatusBar = v.findViewById(R.id.imStatusBar);
        imIconSound = v.findViewById(R.id.imIconSound);
        isSoundOn = PreferencesHelper.getInstances().getBooleanSPr(Config.SharePrefferenceKey.SOUND, mContext,  false);
        imSound.setSelected(isSoundOn);
        isStatusBarOn = PreferencesHelper.getInstances().getBooleanSPr(Config.SharePrefferenceKey.STATUS_BAR, mContext, false);
        imStatusBar.setSelected(isStatusBarOn);
        tvVersion.setText(BuildConfig.VERSION_NAME);
        tvLanguage.setText(Locale.getDefault().getDisplayLanguage());
    }

    private void setListener() {
        llLanguage.setOnClickListener(v -> changeLanguage());

        llVersionInfo.setOnClickListener(v -> versionInfo());

        llPolicy.setOnClickListener(v -> showPolicy());

        //todo save sound on/of
        imSound.setOnClickListener(v -> {
            isSoundOn = !isSoundOn;
            imSound.setSelected(isSoundOn);
            setIconSound(isSoundOn);
            PreferencesHelper.getInstances().setBooleanSPr(Config.SharePrefferenceKey.SOUND,  isSoundOn, mContext);
        });

        //todo save status bar on/off
        imStatusBar.setOnClickListener(v -> {
            isStatusBarOn = !isStatusBarOn;
            imStatusBar.setSelected(isStatusBarOn);
            PreferencesHelper.getInstances().setBooleanSPr(Config.SharePrefferenceKey.STATUS_BAR, isStatusBarOn, mContext);
            NotificationHandler handler = new NotificationHandler(mContext);
            handler.addNotify();
        });
    }

    private void changeLanguage() {
        ChangeLanguageDialogFragment changeLanguageDialogFragment = new ChangeLanguageDialogFragment();
        changeLanguageDialogFragment.show(fragmentManager, "changeLanguage");
    }

    private void versionInfo() {

    }

    private void showPolicy() {

    }

    private void setIconSound(boolean isOn) {
        if (imIconSound != null) {
            imIconSound.setImageResource(isOn ? R.drawable.ic_audio : R.drawable.ic_mute);
        }
    }
}
