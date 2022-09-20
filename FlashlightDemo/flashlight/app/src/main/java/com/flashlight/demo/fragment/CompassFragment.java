package com.flashlight.demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flashlight.demo.R;
import com.flashlight.demo.activity.ShopCompassActivity;
//import com.flashlight.demo.fabric.MyTracking;
//import com.flashlight.demo.fabric.PreLoadBannerMediumUtils;
//import com.flashlight.demo.fabric.TrackingConstants;
import com.flashlight.demo.util.MyConstants;
import com.flashlight.demo.util.MyUtils;
import com.flashlight.demo.util.PreferencesHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.SENSOR_SERVICE;
import static android.os.Looper.getMainLooper;

public class CompassFragment extends Fragment implements SensorEventListener {

    private Context mContext;
    private ImageView imCompass, imOrientation, imMoreCompass;
    private TextView tvTime, tvOrientation;
    private SensorManager directionSensor;
    private float currentDegree = 0f;

    private String typeCompass, typeNeedle;

    public CompassFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        mContext = view.getContext();
        imCompass = view.findViewById(R.id.imCompass);
        imOrientation = view.findViewById(R.id.imOrientation);
        tvOrientation = view.findViewById(R.id.tvOrientation);
        tvTime = view.findViewById(R.id.tvTime);
        imMoreCompass = view.findViewById(R.id.imMoreCompass);
        directionSensor = (SensorManager) mContext.getSystemService(SENSOR_SERVICE);

        RelativeLayout rlAdsParent = view.findViewById(R.id.rlAdsParent);

        setListener();
        checkTypeCompass();

        // TODO: set text clock
        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String hours;
                Date date = new Date();
                hours = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date);
                tvTime.setText(hours);
                someHandler.postDelayed(this, 990);
            }
        }, 10);
        return view;

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        //get the numbers of degree of direction
        float degreeOfDir = Math.round(event.values[0]);
        String degreeString = String.valueOf(degreeOfDir) + (char) 0x00B0;
        /* you need to figure out all values that degree of circle of compass and compass that moved around.
         * understand that degree of direction the compass is clockwise, but not counter-clockwise.
         * the compass needs to be set 0 to 360 degree and set the pole  */

        //get orientation with degree
        if (degreeOfDir == 0 || degreeOfDir == 360) {
            tvOrientation.setText(degreeString + " " + getString(R.string.DirNorth));
        } else if (degreeOfDir > 0 && degreeOfDir < 90) {
            if (degreeOfDir < 15) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirNorth));

            } else if (degreeOfDir < 35) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirNorthNorthEast));

            } else if (degreeOfDir < 55) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirNorthEast));

            } else if (degreeOfDir < 75) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirEastNorthEast));

            } else {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirEast));

            }
        } else if (degreeOfDir == 90) {
            tvOrientation.setText(degreeString + " " + getString(R.string.DirEast));

        } else if (degreeOfDir > 90 && degreeOfDir < 180) {
            if (degreeOfDir < 105) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirEast));

            } else if (degreeOfDir < 125) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirEastSouthEast));

            } else if (degreeOfDir < 145) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirSouthEast));

            } else if (degreeOfDir < 165) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirSouthSouthEast));

            } else {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirSouth));

            }
        } else if (degreeOfDir == 180) {
            tvOrientation.setText(degreeString + " " + getString(R.string.DirSouth));

        } else if (degreeOfDir > 180 && degreeOfDir < 270) {
            if (degreeOfDir < 195) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirSouth));

            } else if (degreeOfDir < 215) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirSouthSouthWest));

            } else if (degreeOfDir < 235) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirSouthWest));

            } else if (degreeOfDir < 255) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirWestSouthWest));

            } else {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirWest));

            }
        } else if (degreeOfDir == 270) {
            tvOrientation.setText(degreeString + " " + getString(R.string.DirWest));


        } else if (degreeOfDir > 270 && degreeOfDir < 360) {
            if (degreeOfDir < 285) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirWest));


            } else if (degreeOfDir < 305) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirWestNorthWest));


            } else if (degreeOfDir < 325) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirNorthwest));


            } else if (degreeOfDir < 345) {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirNorthNorthWest));


            } else {
                tvOrientation.setText(degreeString + " " + getString(R.string.DirNorth));
            }
        }

        // set the rotation animation to rotate compass image around.
        RotateAnimation rotateAnimation = new RotateAnimation(currentDegree, degreeOfDir, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(210);
        rotateAnimation.setFillAfter(true);

        imOrientation.startAnimation(rotateAnimation);
        currentDegree = degreeOfDir;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * set image type for compass
     */
    private void checkTypeCompass() {
        typeCompass = PreferencesHelper.getInstances().getStringSPr(MyConstants.KEY_COMPASS_TYPE, mContext);
        typeNeedle = PreferencesHelper.getInstances().getStringSPr(MyConstants.KEY_NEEDLE_TYPE, mContext);
        if (!typeCompass.equals("null") && !typeNeedle.equals("null")) {
            int srcCompassType = getResources().getIdentifier(mContext.getPackageName() + ":drawable/" + typeCompass, null, null);
            int srcNeedleType = getResources().getIdentifier(mContext.getPackageName() + ":drawable/" + typeNeedle, null, null);
            imCompass.setImageResource(srcCompassType);
            imOrientation.setImageResource(srcNeedleType);
        }
    }

    private void setListener() {
        imMoreCompass.setOnClickListener(v -> startActivity(new Intent(mContext, ShopCompassActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();
        directionSensor.registerListener(this, directionSensor.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        if (imOrientation != null && imCompass != null) {
            checkTypeCompass();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        directionSensor.unregisterListener(this);
    }
}
