package com.flashlight.demo.adapter.itemdelegate;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flashlight.demo.R;
import com.flashlight.demo.adapter.ClickCompassItemListener;
import com.flashlight.demo.adapter.DisplayableItem;
import com.flashlight.demo.adapter.item.FlashTypeItem;
import com.flashlight.demo.util.MyUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;

public class FlashlightDelegate implements ItemViewDelegate<DisplayableItem> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_flashlight;
    }

    @Override
    public boolean isForViewType(DisplayableItem item, int position) {
        return item instanceof FlashTypeItem;
    }

    @Override
    public void convert(ViewHolder holder, DisplayableItem displayableItem, int position) {
        Context context = holder.itemView.getContext();
        FlashTypeItem flashTypeItem = (FlashTypeItem) displayableItem;
        ImageView imThumb = holder.getView(R.id.imIconFlashlightThumb);
        String dirPath = context.getFilesDir().getAbsolutePath() + File.separator + "FlashType/" + flashTypeItem.getNameItem();
        File file= new File(dirPath);
        if (!file.exists()) {
            Glide.with(context)
                    .asBitmap()
                    .apply(new RequestOptions().centerCrop())
                    .load(flashTypeItem.getUrlThumb())
                    .into(imThumb);
        }else Glide.with(context)
                .asBitmap()
                .apply(new RequestOptions().centerCrop())
                .load(new File(dirPath+"/shop_preview.png"))
                .into(imThumb);
        holder.setText(R.id.tvNameFlashlightItem, flashTypeItem.getNameItem());
        holder.setText(R.id.tvIdFlashlightItem, MyUtils.stringFormat(flashTypeItem.getIdItem()));
        holder.setVisible(R.id.imSelected, flashTypeItem.isGet());
        holder.setVisible(R.id.imGetItemFlashlight, !flashTypeItem.isGet());
        holder.setVisible(R.id.imFlashlightPro, flashTypeItem.isPro());
        LinearLayout llItemCompassParent = holder.getView(R.id.llItemFlashlightParent);
        if (flashTypeItem.isSelected()){
            holder.setImageResource(R.id.imSelected, R.drawable.ic_checked);
        }else {
            holder.setImageResource(R.id.imSelected, R.drawable.ic_btnuse);
        }
        llItemCompassParent.setBackgroundResource(flashTypeItem.isSelected() ? R.drawable.bg_shop_item_selected : R.drawable.bg_shop_item);

        holder.setOnClickListener(R.id.imGetItemFlashlight, v -> {
           if (listener!=null){
               listener.onClickItemListener(holder, displayableItem, position);
           }
        });

        llItemCompassParent.setOnClickListener(v -> {
            if (holder.getView(R.id.imGetItemFlashlight).getVisibility()== View.GONE){
                if (listener!=null){
                    listener.onClickItemListener(holder, displayableItem, position);
                }
            }
        });
    }

    private ClickCompassItemListener listener;
    public void setOnClickItemListener(ClickCompassItemListener listener){
        this.listener = listener;
    }
}
