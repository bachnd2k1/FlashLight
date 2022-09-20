package com.flashlight.demo.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flashlight.demo.R;
//import com.flashlight.demo.activity.PremiumActivity;
//import com.flashlight.demo.fabric.MyTracking;
//import com.flashlight.demo.fabric.TrackingConstants;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetOptionDialog extends BottomSheetDialogFragment {

    private Context mContext;
    private ImageView imCloseDialog;
    private TextView tvUpToPro, tvWatchVideo;
    private String nameScreen;

    public BottomSheetOptionDialog(String nameScreen) {
        this.nameScreen = nameScreen;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().setFlags(1024, 1024);
        View contentView = View.inflate(getContext(), R.layout.dialog_bottom_sheet_option, null);
        dialog.setContentView(contentView);
        mContext = dialog.getContext();
        imCloseDialog = contentView.findViewById(R.id.imCloseDialog);
        tvUpToPro = contentView.findViewById(R.id.tvUpToPro);
        tvUpToPro.setVisibility(View.GONE);
        tvWatchVideo = contentView.findViewById(R.id.tvWatchVideo);
        setListener();
    }

    private void setListener(){
        imCloseDialog.setOnClickListener(v -> dismiss());

        tvUpToPro.setOnClickListener(v -> {
            dismiss();
            if (listener!=null){
                listener.OnShowPremium();
            }
        });

        tvWatchVideo.setOnClickListener(v -> {
            if (listener!=null){
                listener.OnShowAdsVideo();
            }
        });
    }

    public interface BottomSheetListener{
        void OnShowAdsVideo();
        void OnShowPremium();
    }

    private BottomSheetListener listener;

    public void setBottomSheetListener(BottomSheetListener listener){
        this.listener = listener;
    }

}
