package com.flashlight.demo.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.flashlight.demo.R;
//import com.flashlight.demo.fabric.PreLoadBannerAdsUtils;
import com.flashlight.demo.handler.FlashModeHandler;
import com.flashlight.demo.model.Flashlight;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.xw.repo.BubbleSeekBar;

import static com.flashlight.demo.util.MyUtils.dp2px;

public class BottomSheetFlashDialog extends BottomSheetDialogFragment {

    private Context mContext;
    private ImageView imClose, imIconFlash;
    private ConstraintLayout clPosition;
    private BubbleSeekBar seekBarFlash;
    private LinearLayout llAdsViewParent;
    private TextView tvPosition;


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        dialog.getWindow().requestFeature(1);
        dialog.getWindow().addFlags((WindowManager.LayoutParams.FLAG_FULLSCREEN));
        View contentView = View.inflate(getContext(), R.layout.dialog_bottom_flash, null);
        dialog.setContentView(contentView);
        mContext = contentView.getContext();

        imClose = contentView.findViewById(R.id.imCloseDialogFlash);
        clPosition = contentView.findViewById(R.id.clPosition);
        tvPosition = contentView.findViewById(R.id.tvPosition);
        imIconFlash = contentView.findViewById(R.id.imIconFlash);
        seekBarFlash = contentView.findViewById(R.id.seekBarFlash);
        llAdsViewParent = contentView.findViewById(R.id.llAdsViewParent);

        seekBarFlash.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                int check = FlashModeHandler.getInstance().getIndicator();

                imIconFlash.setImageResource(progressFloat == 0 ? R.drawable.ic_flash_off : R.drawable.ic_flash_on);
                if (check != progress) {
                    Flashlight.getInstance().playMoveSound(mContext);

                    if (progress == 0 || progress == 10)
                        Flashlight.getInstance().playEndSound(mContext);
                    float x = bubbleSeekBar.getRight() - bubbleSeekBar.getLeft() - dp2px(24);
                    clPosition.setX(x * progressFloat / 10 + bubbleSeekBar.getX());
                    tvPosition.setText(progress + "");
                    clPosition.setVisibility(View.VISIBLE);

                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                clPosition.setVisibility(View.INVISIBLE);
                imIconFlash.setImageResource(progressFloat == 0 ? R.drawable.ic_flash_off : R.drawable.ic_flash_on);

                float x = bubbleSeekBar.getRight() - bubbleSeekBar.getLeft() - dp2px(24);
                clPosition.setX(x * progressFloat / 10 + bubbleSeekBar.getX());
                tvPosition.setText(progress + "");

                FlashModeHandler.getInstance().setIndicator(progress);
                FlashModeHandler.getInstance().setMode((Activity) contentView.getContext());
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });

        imClose.setOnClickListener(v -> dismiss());

    }

}
