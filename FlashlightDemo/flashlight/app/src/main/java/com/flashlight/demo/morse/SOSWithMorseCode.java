package com.flashlight.demo.morse;

import android.annotation.SuppressLint;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.flashlight.demo.model.Flashlight;
import com.flashlight.demo.util.MyConstants;

import java.io.IOException;
import java.util.List;

public class SOSWithMorseCode {

    /**
     * The same {@link com.flashlight.demo.morse.FlashlightWithMorseCode} but input Text is SOS. Don't have space between tows letters
     *  @param list list String morse of text need convert to morse code. Text must convert to morse before use. {@link TextToMorseCode#convertTextToMorseCode()}
     *  @param listener set callback to know morse code start/end. text/code is running
     */
    @SuppressLint("StaticFieldLeak")
    public SOSWithMorseCode(@NonNull List<String> list, OnMorseCodeListener listener) {
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
                        publishProgress("text/" + i);
                        String[] morseCode = list.get(i).split("/");
                        if (morseCode.length > 0) {
                            for (String s : morseCode) {
                                if (s.equals(".")) {
                                    if (!this.isCancelled()) {
                                        publishProgress(".");
                                        main(MyConstants.DOT_TIME_MILLISECONDS);
                                    }

                                }
                                if (s.equals("-")) {
                                    if (!this.isCancelled()) {
                                        publishProgress("-");
                                        main(3*MyConstants.DOT_TIME_MILLISECONDS);
                                    }
                                }
                            }
//                            try {
//                                if (!this.isCancelled()) {
//                                    publishProgress(" ");
//                                    Thread.sleep(1000);
//                                }
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                        }
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(String... values) {
                    super.onProgressUpdate(values);
                    if (listener != null) {
                        if (!values[0].contains("text/")) {
                            listener.onShowView(values[0].replace("/", ""), i);
                            i++;
                        } else {
                            listener.onShowText(Integer.parseInt(values[0].replace("text/", "")));
                        }
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (listener != null) {
                        listener.onEndMorseCode();
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
                    String array[] = cameraManager.getCameraIdList();
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
