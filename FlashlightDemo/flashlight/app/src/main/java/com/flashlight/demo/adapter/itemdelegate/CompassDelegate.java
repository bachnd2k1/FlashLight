package com.flashlight.demo.adapter.itemdelegate;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.flashlight.demo.R;
import com.flashlight.demo.adapter.ClickCompassItemListener;
import com.flashlight.demo.adapter.DisplayableItem;
import com.flashlight.demo.adapter.item.CompassTypeItem;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

public class CompassDelegate implements ItemViewDelegate<DisplayableItem> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_compass;
    }

    @Override
    public boolean isForViewType(DisplayableItem item, int position) {
        return item instanceof CompassTypeItem;

    }

    @Override
    public void convert(ViewHolder holder, DisplayableItem displayableItem, int position) {
        CompassTypeItem compassTypeItem = (CompassTypeItem) displayableItem;
        Context context = holder.itemView.getContext();

        int srcCompassType = context.getResources().getIdentifier( context.getPackageName()+":drawable/"+ compassTypeItem.getSrcThumbIcon(), null, null);
        holder.setImageResource(R.id.imIconCompassThumb, srcCompassType);
        holder.setText(R.id.tvNameCompassItem, compassTypeItem.getNameItem());
        holder.setText(R.id.tvIdCompassItem, compassTypeItem.getIdItem() + "");
        holder.setVisible(R.id.imSelected, compassTypeItem.isGet());
        holder.setVisible(R.id.tvGetItemCompass, !compassTypeItem.isGet());
        holder.setVisible(R.id.imCompassPro, compassTypeItem.isPro());
        LinearLayout llItemCompassParent = holder.getView(R.id.llItemCompassParent);
        llItemCompassParent.setBackgroundResource(compassTypeItem.isSelected() ? R.drawable.bg_shop_item_selected : R.drawable.bg_shop_item);
        holder.setOnClickListener(R.id.tvGetItemCompass, v -> {
            if (listener != null) {
                listener.onClickItemListener(holder, displayableItem, position);
            }
        });

        holder.setOnClickListener(R.id.llItemCompassParent, v -> {
            if (holder.getView(R.id.tvGetItemCompass).getVisibility() == View.GONE) {
                if (listener != null) {
                    listener.onClickItemListener(holder, displayableItem, position);
                }
            }
        });


    }

    private ClickCompassItemListener listener;

    public void setOnClickCompassItemListener(ClickCompassItemListener listener) {
        this.listener = listener;
    }
}
