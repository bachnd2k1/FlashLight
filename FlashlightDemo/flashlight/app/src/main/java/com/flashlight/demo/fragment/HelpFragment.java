package com.flashlight.demo.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.flashlight.demo.R;
//import com.flashlight.demo.fabric.MyTracking;
//import com.flashlight.demo.fabric.PreLoadBannerMediumUtils;
//import com.flashlight.demo.fabric.TrackingConstants;
import com.flashlight.demo.morse.FlashlightWithMorseCode;
import com.flashlight.demo.morse.SOSWithMorseCode;
import com.flashlight.demo.morse.TextToMorseCode;

import java.util.List;

public class HelpFragment extends Fragment {

    private Context mContext;
    private AsyncTask helpAsyncTask;
    private LinearLayout llMorseCode;
    private LinearLayout llText;
    private LinearLayout llViewMorseCode;
    private LottieAnimationView sosAnimation;
    private boolean isPlay = false;

    public HelpFragment() {
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle) {
        View view = layoutinflater.inflate(R.layout.fragment_help, viewgroup, false);
        mContext = view.getContext();

        llViewMorseCode = view.findViewById(R.id.llViewMorseCode);
        llMorseCode = view.findViewById(R.id.llMorseCode);
        llText = view.findViewById(R.id.llText);
        sosAnimation = view.findViewById(R.id.sosAnimation);
        sosAnimation.setSpeed(2.0f);
        sosAnimation.cancelAnimation();
        sosAnimation.setOnClickListener(v -> {
            if (isPlay) {
                if (helpAsyncTask != null) {
                    helpAsyncTask.cancel(true);
                }
                llViewMorseCode.setVisibility(View.INVISIBLE);
                llMorseCode.removeAllViewsInLayout();
                llText.removeAllViewsInLayout();
                sosAnimation.cancelAnimation();
                isPlay = false;
            } else {
                playMorseCode();
                sosAnimation.playAnimation();
                isPlay = true;
            }
        });
        return view;
    }

    /**
     * run SOS with morse code
     */
    private void playMorseCode() {
        llViewMorseCode.setVisibility(View.VISIBLE);
        llMorseCode.removeAllViewsInLayout();
        llText.removeAllViewsInLayout();
        String[] sosText = {"S", "O", "S"};
        String sos = "SOS";
        TextToMorseCode textToMorseCode = new TextToMorseCode(sos);
        List<String> listCode = textToMorseCode.convertTextToMorseCode();
        // TODO: add ImageView for morse code (dot (.) or dash (-)) with morse code of text
        int k = 0;
        for (int i = 0; i < listCode.size(); i++) {
            String[] morseCode = listCode.get(i).split("/");
            if (morseCode.length > 0) {
                for (String s : morseCode) {
                    if (s.equals(".")) {
                        llMorseCode.addView(addImageView(R.drawable.ic_short_normal), k);
                    } else if (s.equals("-")) {
                        llMorseCode.addView(addImageView(R.drawable.ic_long_normal), k);
                    }
                    k++;
                }
            }
            llText.addView(addTextView(sosText[i], "#FFFFFF"), i);
        }

        //todo run flash with morse code
        new SOSWithMorseCode(listCode, new SOSWithMorseCode.OnMorseCodeListener() {

            @Override
            public void onStart(AsyncTask asyncTask) {
                helpAsyncTask = asyncTask;
            }

            @Override
            public void onShowView(String s, int position) {
                //todo remove and draw view at position
                if (s.equals(".")) {
                    if (helpAsyncTask != null) {
                        if (!helpAsyncTask.isCancelled()) {
                            llMorseCode.removeViewAt(position);
                            llMorseCode.addView(addImageView(R.drawable.ic_short), position);
                        }
                    }
                } else if (s.equals("-")) {
                    if (helpAsyncTask != null) {
                        if (!helpAsyncTask.isCancelled()) {
                            llMorseCode.removeViewAt(position);
                            llMorseCode.addView(addImageView(R.drawable.ic_long), position);
                        }
                    }

                } else {
                    if (helpAsyncTask != null) {
                        if (!helpAsyncTask.isCancelled()) {
                            llMorseCode.removeViewAt(position);
                            llMorseCode.addView(addImageView(R.drawable.ic_blank), position);
                        }
                    }
                }
            }

            @Override
            public void onShowText(int position) {
                //todo set text color corresponding to morse is running
                if (helpAsyncTask != null) {
                    if (!helpAsyncTask.isCancelled()) {
                        llText.removeViewAt(position);
                        llText.addView(addTextView(sosText[position], "#FFDA00"), position);
                    }
                }
            }

            @Override
            public void onEndMorseCode() {

                //todo replay when the loop is end
                if (helpAsyncTask != null) {
                    if (!helpAsyncTask.isCancelled()) {
                        playMorseCode();
                    }
                }
            }
        });
    }

    /**
     * @param resId create image view and setImageResource with resId
     * @return ImageView
     */
    private ImageView addImageView(int resId) {
        ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginEnd((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, Resources.getSystem().getDisplayMetrics()));
        imageView.setImageResource(resId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    /**
     * @param text Add TextView with text
     * @param color set textColor for TextView
     * @return TextView
     */
    private TextView addTextView(String text, String color) {
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMarginEnd((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, Resources.getSystem().getDisplayMetrics()));
        textView.setText(text);
        textView.setTypeface(ResourcesCompat.getFont(mContext, R.font.quicksand_bold));
        textView.setTextColor(Color.parseColor(color));
        textView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, Resources.getSystem().getDisplayMetrics()));
        textView.setTextSize(16);
        return textView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (helpAsyncTask != null) {
            helpAsyncTask.cancel(true);
        }
    }
}
