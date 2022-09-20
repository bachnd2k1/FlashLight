package com.flashlight.demo.adapter.myadapter;

import android.content.Context;

import com.flashlight.demo.adapter.ClickCompassItemListener;
import com.flashlight.demo.adapter.DisplayableItem;
import com.flashlight.demo.adapter.itemdelegate.CompassDelegate;
import com.flashlight.demo.adapter.itemdelegate.FlashlightDelegate;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

public class MyAdapter extends MultiItemTypeAdapter<DisplayableItem> {
    public MyAdapter(Context context, List<DisplayableItem> datas) {
        super(context, datas);

        FlashlightDelegate flashlightDelegate = new FlashlightDelegate();
        flashlightDelegate.setOnClickItemListener((holder, displayableItem, position) -> {
            if (listener!=null){
                listener.onClickItemListener(holder, displayableItem, position);
            }
        });

        CompassDelegate compassDelegate = new CompassDelegate();
        compassDelegate.setOnClickCompassItemListener((holder, displayableItem, position) -> {
            if (listener!=null){
                listener.onClickItemListener(holder, displayableItem, position);
            }
        });

        addItemViewDelegate(flashlightDelegate);
        addItemViewDelegate(compassDelegate);
    }

    private ClickCompassItemListener listener;
    public void setAdapterItemClickListener(ClickCompassItemListener listener){
        this.listener = listener;
    }
}
