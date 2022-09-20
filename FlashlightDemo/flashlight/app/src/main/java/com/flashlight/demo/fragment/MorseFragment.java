package com.flashlight.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.flashlight.demo.R;
//import com.flashlight.demo.fabric.MyTracking;
//import com.flashlight.demo.fabric.PreLoadBannerAdsUtils;
//import com.flashlight.demo.fabric.TrackingConstants;
import com.flashlight.demo.morse.FlashlightWithMorseCode;
import com.flashlight.demo.morse.TextToMorseCode;
import com.flashlight.demo.util.MyUtils;

import java.util.List;
import java.util.Objects;

public class MorseFragment extends Fragment {
    private Context mContext;

    private EditText edMorseText;
    private LinearLayout llMorseCode, llText, rlAdsParent;
    private TextView tvStart, tvReset;
    private boolean loop = true, running = false;
    private AsyncTask morseAsyncTask;

    public MorseFragment() {
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle) {
        View view = layoutinflater.inflate(R.layout.fragment_morse, viewgroup, false);
        mContext = view.getContext();
        initView(view);
        setListener();
        return view;
    }

    private void initView(View v) {
        edMorseText = v.findViewById(R.id.edTextMorse);
        llMorseCode = v.findViewById(R.id.llMorseCode);
        llText = v.findViewById(R.id.llText);
        tvStart = v.findViewById(R.id.tvStart);
        tvReset = v.findViewById(R.id.tvReset);
        rlAdsParent = v.findViewById(R.id.rlAdsParent);
        tvStart.setEnabled(false);
        tvReset.setEnabled(false);

    }


    private void setListener() {

        edMorseText.setOnClickListener(v -> {
            edMorseText.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        });

        //todo get text need convert to morse code from keyboard
        edMorseText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    tvStart.setEnabled(true);
                    tvStart.setBackgroundResource(R.drawable.bg_edit_search);
                }else {
                    tvStart.setEnabled(false);
                    tvStart.setBackgroundResource(R.drawable.bg_text_morse);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //todo hide keyboard and run morse code
        tvStart.setOnClickListener(v -> {
            edMorseText.setFocusable(false);
            InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            Objects.requireNonNull(imm).hideSoftInputFromWindow(edMorseText.getWindowToken(), 0);
            if (!running) {
                playMorseCode();
            } else {
                if (morseAsyncTask != null) {
                    morseAsyncTask.cancel(true);
                }
            }
            setStatus(running);
            running = !running;
        });

        tvReset.setOnClickListener(v -> {
            if (morseAsyncTask != null) {
                morseAsyncTask.cancel(true);
            }
            llMorseCode.removeAllViewsInLayout();
            llText.removeAllViewsInLayout();
            edMorseText.setText("");
            tvStart.setEnabled(false);
            tvReset.setEnabled(false);
            tvStart.setBackgroundResource(R.drawable.bg_text_morse);
            tvStart.setText(MyUtils.getString(R.string.start));
        });
    }

    /**
     * run morse with text
     */
    private void playMorseCode() {
        tvReset.setEnabled(true);
        llMorseCode.removeAllViewsInLayout(); // TODO: view parent to add view morse code
        llText.removeAllViewsInLayout(); // TODO: view parent to add view text of code
        char[] chars = edMorseText.getText().toString().toCharArray();
        TextToMorseCode textToMorseCode = new TextToMorseCode(edMorseText.getText().toString());
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
            llMorseCode.addView(addImageView(R.drawable.ic_blank_normal), k);
            llText.addView(addTextView(String.valueOf(chars[i]), "#2C3039"), i);
            k++;
        }

        //todo run flash with morse code
        new FlashlightWithMorseCode(listCode, new FlashlightWithMorseCode.OnMorseCodeListener() {

            @Override
            public void onStart(AsyncTask asyncTask) {
                morseAsyncTask = asyncTask;
            }

            @Override
            public void onShowView(String s, int position) {
                //todo remove and re draw view at position
                if (s.equals(".")) {
                    if (morseAsyncTask != null) {
                        if (!morseAsyncTask.isCancelled()) {
                            llMorseCode.removeViewAt(position);
                            llMorseCode.addView(addImageView(R.drawable.ic_short), position);
                        }
                    }
                } else if (s.equals("-")) {
                    if (morseAsyncTask != null) {
                        if (!morseAsyncTask.isCancelled()) {
                            llMorseCode.removeViewAt(position);
                            llMorseCode.addView(addImageView(R.drawable.ic_long), position);
                        }
                    }

                } else {
                    if (morseAsyncTask != null) {
                        if (!morseAsyncTask.isCancelled()) {
                            llMorseCode.removeViewAt(position);
                            llMorseCode.addView(addImageView(R.drawable.ic_blank), position);
                        }
                    }
                }
            }

            @Override
            public void onShowText(int position) {
                //todo set text color corresponding to morse is running
                if (morseAsyncTask != null) {
                    if (!morseAsyncTask.isCancelled()) {
                        llText.removeViewAt(position);
                        llText.addView(addTextView(String.valueOf(chars[position]), "#FFFFFF"), position);
                    }
                }
            }

            @Override
            public void onEndMorseCode() {
                //todo replay when the loop is end
                if (loop) {
                    playMorseCode();
                }
            }
        });

    }

    /**
     * add view to parent view morse code
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
     * add view to parent view text of code
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

    private void setStatus(boolean running) {
        if (running) {
            tvStart.setText(MyUtils.getString(R.string.start));
        } else {
            tvStart.setText(MyUtils.getString(R.string.stop));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (morseAsyncTask != null) {
            morseAsyncTask.cancel(true);
        }
    }
}
