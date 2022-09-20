package com.flashlight.demo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.flashlight.demo.R;
import com.flashlight.demo.util.LocaleHelper;
import com.flashlight.demo.util.MyConstants;
import com.flashlight.demo.util.PreferencesHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CompassActivity extends BaseActivity implements SensorEventListener {

    private Context mContext;

    private ImageView imCompass, imOrientation, imMoreCompass;
    private TextView tvTime, tvOrientation;
    private SensorManager directionSensor;
    private float currentDegree = 0f;

    private String typeCompass, typeNeedle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_compass);
        mContext = this;

        initView();
        setListener();
    }

    private void initView() {
        imCompass = findViewById(R.id.imCompass);
        imOrientation = findViewById(R.id.imOrientation);
        tvOrientation = findViewById(R.id.tvOrientation);
        tvTime = findViewById(R.id.tvTime);
        imMoreCompass = findViewById(R.id.imMoreCompass);
        directionSensor = (SensorManager) getSystemService(SENSOR_SERVICE);

        checkTypeCompass();

        //todo set text for view time
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

    }

    private void setListener() {
        imMoreCompass.setOnClickListener(v ->startActivity(new Intent(mContext, ShopCompassActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        directionSensor.registerListener(this, directionSensor.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
        if (imOrientation!=null && imCompass!=null){
            checkTypeCompass();
        }
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
    private void checkTypeCompass(){
        typeCompass = PreferencesHelper.getInstances().getStringSPr(MyConstants.KEY_COMPASS_TYPE, mContext); //todo get type compass from SharePreference
        typeNeedle = PreferencesHelper.getInstances().getStringSPr(MyConstants.KEY_NEEDLE_TYPE, mContext);  //todo get type compass needle from SharePreference
        if(!typeCompass.equals("null") && !typeNeedle.equals("null")){
            int srcCompassType = getResources().getIdentifier( getPackageName()+":drawable/"+typeCompass, null, null);
            int srcNeedleType = getResources().getIdentifier( getPackageName()+":drawable/"+typeNeedle, null, null);
            imCompass.setImageResource(srcCompassType);
            imOrientation.setImageResource(srcNeedleType);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase));
    }
}
