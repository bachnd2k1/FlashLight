package com.flashlight.demo.api;

import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.flashlight.demo.api.request.FlashType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class APIServiceWrapper {
    public static Context mContext;
    private volatile static APIServiceWrapper instance;

    public static APIServiceWrapper getInstance(Context context) {
        if (instance == null) {
            synchronized (APIServiceWrapper.class) {
                mContext = context;
                instance = new APIServiceWrapper();
            }
        }

        return instance;
    }

    public static void clearTag(String tag) {
        AndroidNetworking.cancel(tag);
    }

    public void getFlashTypeFromApi(final ParsedRequestListener<List<FlashType>> response) {
            AndroidNetworking.get("http://photo.footballtv.info/flashlight/jsondata/flashtype.json")
                    .setTag("getFlashType")
                    .setUserAgent("Android66")
                    .setPriority(Priority.IMMEDIATE)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String s) {
                            if (s != null) {
                                try {
                                    List<FlashType> listFlashType = new ArrayList<>();
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    String urlRoot = jsonObject.getString("urlRoot");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject dataFlashType = jsonArray.getJSONObject(i);
                                        String name = dataFlashType.getString("eventName");
                                        int id = dataFlashType.getInt("eventId");
//                                        String urlThumb = urlRoot + dataFlashType.getString("urlThumb");
                                        boolean isPro = dataFlashType.getBoolean("isPro");
                                        String imgON = urlRoot + dataFlashType.getString("imgON");
                                        String imgOFF = urlRoot + dataFlashType.getString("imgOFF");
                                        String urlShop = urlRoot + dataFlashType.getString("urlShop");

                                        FlashType flashType = new FlashType(name, id, "", imgON, imgOFF, urlShop, isPro);
                                        listFlashType.add(flashType);
                                        Log.d("dataAPI", name);

                                    }
                                    response.onResponse(listFlashType);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }

}
