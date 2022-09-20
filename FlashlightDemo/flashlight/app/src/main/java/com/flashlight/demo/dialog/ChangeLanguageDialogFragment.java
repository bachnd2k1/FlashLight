package com.flashlight.demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flashlight.demo.R;
import com.flashlight.demo.activity.SplashActivity;
import com.flashlight.demo.adapter.myadapter.ChangeLanguageAdapter;
import com.flashlight.demo.util.LocaleHelper;

import java.util.Objects;

public class ChangeLanguageDialogFragment extends androidx.fragment.app.DialogFragment {
    private Context context;
    private RecyclerView mRecyclerView;
    private ChangeLanguageAdapter mAdapter;
    private TextView searchView;
    public Dialog dialog;
    private TextView tvComplete;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NO_TITLE;
        int theme = 0;
        setStyle(style, theme);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        dialog = this.getDialog();
        //resize dialog change language
        Point size = new Point();
        Display display = Objects.requireNonNull(this.getActivity()).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int w = 4 * size.x / 5;
        int h = 3 * size.y /4;
        if (dialog != null) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(w, h);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_language, container);
        context = view.getContext();

        searchView = view.findViewById(R.id.tvSearchLanguage);
        mRecyclerView = view.findViewById(R.id.rcv_list_language);
        tvComplete = view.findViewById(R.id.tv_complete);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        initData();

        tvComplete.setOnClickListener(v -> {
            if (!TextUtils.equals(mAdapter.languageSelected, LocaleHelper.getSelectedLanguage(context))) {
                ChangeLanguageDialogFragment.this.changeLanguage(mAdapter.languageSelected, mAdapter.getSelectedLanguage());
            } else {
                ChangeLanguageDialogFragment.this.dismiss();
            }
        });
        return view;
    }

    private void initData() {
        mAdapter = new ChangeLanguageAdapter();
        mAdapter.languageSelected = LocaleHelper.getSelectedLanguage(context);
        mAdapter.updateData(LocaleHelper.getListCountries());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAdapter.getFilter().filter(editable);
            }
        });
    }

    private void changeLanguage(String languageSelected, String selectedLanguage) {
        LocaleHelper.setSelectedLanguage(languageSelected, context);
        String languageCode = LocaleHelper.getLanguageCodeByDisplayName(selectedLanguage);
        LocaleHelper.setNewLocale(getActivity(), languageCode);
        restartToApplyLanguage();
    }

    private void restartToApplyLanguage() {
        final FragmentActivity activity = this.getActivity();
        dismiss();
        new Handler().postDelayed(() -> restartApp(activity, SplashActivity.class), 1000);
    }

    public static void restartApp(FragmentActivity activity, Class<?> activityToStartAfterRestarting) {
        if (activity != null) {
            Intent intent = new Intent(activity, activityToStartAfterRestarting);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
            Runtime.getRuntime().exit(0);
        }
    }
}


