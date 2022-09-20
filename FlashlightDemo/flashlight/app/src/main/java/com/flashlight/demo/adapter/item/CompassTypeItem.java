package com.flashlight.demo.adapter.item;

import androidx.annotation.NonNull;

import com.flashlight.demo.adapter.DisplayableItem;

public class CompassTypeItem implements DisplayableItem {
    private String nameItem;
    private int idItem;
    private boolean isGet;
    private String srcThumbIcon;
    private boolean isPro;
    private boolean isSelected;


    public CompassTypeItem() {
    }

    public CompassTypeItem(@NonNull String nameItem, @NonNull boolean isGet, @NonNull String srcThumbIcon) {
        this.nameItem = nameItem;
        this.isGet = isGet;
        this.srcThumbIcon = srcThumbIcon;
    }

    public CompassTypeItem(@NonNull String nameItem, @NonNull int idItem, @NonNull boolean isGet, @NonNull String srcThumbIcon, boolean isPro) {
        this.nameItem = nameItem;
        this.idItem = idItem;
        this.isGet = isGet;
        this.srcThumbIcon = srcThumbIcon;
        this.isPro = isPro;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean get) {
        isGet = get;
    }

    public String getSrcThumbIcon() {
        return srcThumbIcon;
    }

    public void setSrcThumbIcon(String srcThumbIcon) {
        this.srcThumbIcon = srcThumbIcon;
    }

    public boolean isPro() {
        return isPro;
    }

    public void setPro(boolean pro) {
        isPro = pro;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
