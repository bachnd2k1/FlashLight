package com.flashlight.demo.util;

import android.app.Activity;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;


import com.flashlight.demo.BaseApplication;
import com.flashlight.demo.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MyUtils {
    public static String getString(int stringId) {
        return BaseApplication.getInstance().getResources().getString(stringId);
    }

    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static String getTestDevice() {
        String android_id = Settings.Secure.getString(BaseApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        return md5(android_id).toUpperCase();
    }

    public static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    public static String stringFormat(int value){
        if (value >=10){
            return String.valueOf(value);
        }else return "0"+ value;
    }

    public static String Degree2Direction(float degreeOfDir) {
        String degreeString = String.valueOf(degreeOfDir) + (char) 0x00B0;
        if (degreeOfDir == 0 || degreeOfDir == 360) {
            return (degreeString + " " + getString(R.string.DirNorth));
        } else if (degreeOfDir > 0 && degreeOfDir < 90) {
            if (degreeOfDir < 15) {
                return (degreeString + " " + getString(R.string.DirNorth));

            } else if (degreeOfDir < 35) {
                return (degreeString + " " + getString(R.string.DirNorthNorthEast));

            } else if (degreeOfDir < 55) {
                return (degreeString + " " + getString(R.string.DirNorthEast));

            } else if (degreeOfDir < 75) {
                return (degreeString + " " + getString(R.string.DirEastNorthEast));

            } else {
                return (degreeString + " " + getString(R.string.DirEast));

            }
        } else if (degreeOfDir == 90) {
            return (degreeString + " " + getString(R.string.DirEast));

        } else if (degreeOfDir > 90 && degreeOfDir < 180) {
            if (degreeOfDir < 105) {
                return (degreeString + " " + getString(R.string.DirEast));

            } else if (degreeOfDir < 125) {
                return (degreeString + " " + getString(R.string.DirEastSouthEast));

            } else if (degreeOfDir < 145) {
                return (degreeString + " " + getString(R.string.DirSouthEast));

            } else if (degreeOfDir < 165) {
                return (degreeString + " " + getString(R.string.DirSouthSouthEast));

            } else {
                return (degreeString + " " + getString(R.string.DirSouth));

            }
        } else if (degreeOfDir == 180) {
            return (degreeString + " " + getString(R.string.DirSouth));

        } else if (degreeOfDir > 180 && degreeOfDir < 270) {
            if (degreeOfDir < 195) {
                return (degreeString + " " + getString(R.string.DirSouth));

            } else if (degreeOfDir < 215) {
                return (degreeString + " " + getString(R.string.DirSouthSouthWest));

            } else if (degreeOfDir < 235) {
                return (degreeString + " " + getString(R.string.DirSouthWest));

            } else if (degreeOfDir < 255) {
                return (degreeString + " " + getString(R.string.DirWestSouthWest));

            } else {
                return (degreeString + " " + getString(R.string.DirWest));

            }
        } else if (degreeOfDir == 270) {
            return (degreeString + " " + getString(R.string.DirWest));


        } else if (degreeOfDir > 270 && degreeOfDir < 360) {
            if (degreeOfDir < 285) {
                return (degreeString + " " + getString(R.string.DirWest));


            } else if (degreeOfDir < 305) {
                return (degreeString + " " + getString(R.string.DirWestNorthWest));


            } else if (degreeOfDir < 325) {
                return (degreeString + " " + getString(R.string.DirNorthwest));


            } else if (degreeOfDir < 345) {
                return (degreeString + " " + getString(R.string.DirNorthNorthWest));


            } else {
                return (degreeString + " " + getString(R.string.DirNorth));
            }
        } else return "";
    }
}
