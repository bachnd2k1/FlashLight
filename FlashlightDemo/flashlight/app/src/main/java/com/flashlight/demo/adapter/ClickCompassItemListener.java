package com.flashlight.demo.adapter;

import com.zhy.adapter.recyclerview.base.ViewHolder;

public interface ClickCompassItemListener {
    void onClickItemListener(ViewHolder holder, DisplayableItem displayableItem, int position);
}
