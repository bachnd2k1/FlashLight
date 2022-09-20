package com.flashlight.demo.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void longToast(Context context, String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
