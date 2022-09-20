package com.flashlight.demo.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.flashlight.demo.R;
import com.flashlight.demo.util.MyUtils;

import java.util.Objects;

@SuppressLint("ValidFragment")
public class LoadingDialog extends DialogFragment {

    public Dialog dialog;
    private LottieAnimationView mLoadingImg;
    private TextView tvTextDialog;
    private int stringResId;

    @SuppressLint("ValidFragment")
    public LoadingDialog(@NonNull int stringResId) {
        this.stringResId = stringResId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NO_TITLE;
        int theme = 0;
        setStyle(style, theme);
    }

    @Override
    public void onStart() {
        super.onStart();
        dialog = this.getDialog();
        Point size = new Point();
        Display display = Objects.requireNonNull(this.getActivity()).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int w = MyUtils.getDisplayInfo(this.getActivity()).widthPixels;
        int h = MyUtils.getDisplayInfo(this.getActivity()).heightPixels / 5;
        if (dialog != null) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(w, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading, container);
        mLoadingImg = view.findViewById(R.id.loadingAnimation);
        tvTextDialog = view.findViewById(R.id.tvTextDialog);
        tvTextDialog.setText(stringResId);
        mLoadingImg.playAnimation();
        if (this.dialog != null) {
            this.dialog.setCancelable(false);
        }
        return view;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.commitAllowingStateLoss();
            ft.add(this, tag);
            ft.commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void dismissDialog(Fragment fragment, FragmentManager manager) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.commitAllowingStateLoss();
            ft.remove( fragment);
            ft.commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}

