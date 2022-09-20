package com.flashlight.demo.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.flashlight.demo.R;
//import com.flashlight.demo.fabric.PreLoadBannerMediumUtils;
import com.flashlight.demo.util.MyUtils;
//import com.google.android.gms.ads.AdView;

import java.util.Objects;

public class ExitAppDialog extends DialogFragment {
    public Dialog dialog;
    private Context mContext;
    public ImageView imCloseDialog;
    private TextView tvYes, tvNo, tvTitleDialog;
    private FrameLayout viewAdsParent;
    private int resIdStringTitle;
    private boolean showAdsView;

    @SuppressLint("ValidFragment")
    public ExitAppDialog(int resIdStringTitle, boolean showAdsView) {
        this.resIdStringTitle = resIdStringTitle;
        this.showAdsView = showAdsView;
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
        dialog.setCancelable(false);
        Point size = new Point();
        Display display = Objects.requireNonNull(this.getActivity()).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        int w = MyUtils.getDisplayInfo(this.getActivity()).widthPixels;
        if (dialog != null) {
            Objects.requireNonNull(dialog.getWindow()).setLayout(4 * w / 5, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_exit_app);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_exit_app, container);
        mContext = view.getContext();
        tvTitleDialog = view.findViewById(R.id.tvTitleDialog);
        imCloseDialog = view.findViewById(R.id.imCloseDialog);
        tvNo = view.findViewById(R.id.tvNo);
        tvYes = view.findViewById(R.id.tvYes);
        viewAdsParent = view.findViewById(R.id.viewAdsParent);
        if (showAdsView) {
        } else viewAdsParent.setVisibility(View.GONE);

        tvTitleDialog.setText(resIdStringTitle);

        setListener();
        return view;
    }

    private void setListener() {
        tvYes.setOnClickListener(view -> {
            if (listener != null) {
                listener.OnYesClickListener();
            }
        });

        tvNo.setOnClickListener(view -> {
            if (listener != null) {
                listener.OnNoClickListener();
            }
        });

        imCloseDialog.setOnClickListener(view -> {
            if (listener != null) {
                listener.OnCloseDialogClickListener();
            }
        });
    }

    public interface AppDialogListener {
        void OnYesClickListener();

        void OnNoClickListener();

        void OnCloseDialogClickListener();
    }

    private AppDialogListener listener;

    public void setExitDialogListener(AppDialogListener listener) {
        this.listener = listener;
    }
}

