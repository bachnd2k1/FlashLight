package com.flashlight.demo.morse;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.flashlight.demo.model.Flashlight;
import com.flashlight.demo.util.MyConstants;

import java.io.IOException;
import java.util.List;

public class FlashlightWithMorseCode {

    /**
     *  Rule of morse code:
     *  Text has been convert to morse code. Each Morse code symbol is formed by a sequence of dots and dashes {@link TextToMorseCode#convertTextToMorseCode()} to show all morse code convert).
     *  A dash equal to three dots.
     *  The space between parts of the same letter is equal to one dot.
     *  THe space between two letters is equal to three dots.
     *  The space between two words is equal to seven dots.
     *
     * @param list list String morse of text need convert to morse code. Text must convert to morse before use. {@link TextToMorseCode#convertTextToMorseCode()}
     * @param listener set callback to know morse code start/end. text/code is running
     */
    @SuppressLint("StaticFieldLeak")
    public FlashlightWithMorseCode(@NonNull List<String> list, OnMorseCodeListener listener) {
        if (list.size() > 0) {
            new AsyncTask<Void, String, String>() {
                int i = 0;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (listener != null) {
                        listener.onStart(this);
                    }
                }

                @Override
                protected String doInBackground(Void... voids) {
                    for (int i = 0; i < list.size(); i++) {
                        publishProgress("text/" + i);   //todo publish text at position i for listener
                        Log.d("textABCD","text/" + i);
                        String[] morseCode = list.get(i).split("/");
                        if (morseCode.length > 0) {
                            for (String s : morseCode) {
                                //todo publish morse code at position i for listener
                                if (s.equals(".")) {
                                    if (!this.isCancelled()) {
                                        publishProgress(".");
                                        Log.d("morse",".");
                                        main(MyConstants.DOT_TIME_MILLISECONDS);
                                    }

                                }
                                if (s.equals("-")) {
                                    if (!this.isCancelled()) {
                                        publishProgress("-");
                                        Log.d("morse","-");
                                        main(3*MyConstants.DOT_TIME_MILLISECONDS);
                                    }
                                }
                            }
                            try {
                                //todo end of characters sleep 1000ms,
                                if (!this.isCancelled()) {
                                    publishProgress(" "); //todo publish for listener when end of characters
                                    Thread.sleep(7*MyConstants.DOT_TIME_MILLISECONDS);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);
                    if (listener != null) {
                        if (!values[0].contains("text/")) {
                            listener.onShowView(values[0].replace("/", ""), i); //todo set morse that is running for listener
                            i++;
                        } else {
                            listener.onShowText(Integer.parseInt(values[0].replace("text/", ""))); //todo set text thaT is running for listener
                            Log.d("check",values[0].replace("text/", ""));
                        }
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (listener != null) {
                        listener.onEndMorseCode(); //todo end one loop of morse code
                    }

                }
            }.execute();
        }

    }

    private Camera cam;
    private Camera.Parameters params;
    private boolean isLightOn = false;
    private CameraManager cameraManager;
    private String cameraId;

    /**
     * @param sleep flip flash with sleep time (milliseconds) and re flip flash with 200ms
     */
    private void main(long sleep) {

        if (Build.VERSION.SDK_INT >= 22) {
            cam = Camera.open();
        }else {
            cam = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }
        try {
            params = cam.getParameters();
            cam.setPreviewTexture(new SurfaceTexture(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cam.startPreview();

        try {
            if (Build.VERSION.SDK_INT >= 22) {
                flipFlash();
                Thread.sleep(sleep);
                flipFlash();
                Thread.sleep(MyConstants.DOT_TIME_MILLISECONDS);
            }else {
                Flashlight.getInstance().toggle(true);
                Thread.sleep(sleep);
                Flashlight.getInstance().toggle(false);
                Thread.sleep(MyConstants.DOT_TIME_MILLISECONDS);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cam.stopPreview();
        cam.release();
    }


    /**
     * flip flashlight: if flashlight is on. turn of flashlight, if flashlight is off. turn on flashlight
     */
    private void flipFlash() {
        if (isLightOn) {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            cam.setParameters(params);
            Flashlight.getInstance().toggle(true);
            isLightOn = false;
        } else {
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            cam.setParameters(params);
            Flashlight.getInstance().toggle(false);

            isLightOn = true;
        }
    }

    public interface OnMorseCodeListener {
        void onStart(AsyncTask asyncTask);

        void onShowView(String s, int position);

        void onShowText(int position);

        void onEndMorseCode();
    }

    public void toggle() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (cameraManager == null || TextUtils.isEmpty(cameraId))
                    return;
                try {
                    cameraManager.setTorchMode(cameraId, isLightOn);
                    isLightOn=!isLightOn;
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        String[] array = cameraManager.getCameraIdList();
                        for (int i = 1; i < array.length; i++) {
                            try {
                                cameraId = cameraManager.getCameraIdList()[i];
                                cameraManager.setTorchMode(cameraId, isLightOn);

                                return;
                            } catch (Exception a) {
                                a.printStackTrace();
                            }
                        }
                        isLightOn=!isLightOn;
                    } catch (Exception b) {
                        b.printStackTrace();
                    }
                }

            } else {
                if (params == null || cam == null)
                    return;
                try {
                    if (isLightOn) {
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        cam.setParameters(params);
                        cam.startPreview();
                        isLightOn = false;
                    } else {
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        cam.setParameters(params);
                        isLightOn = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }
}
