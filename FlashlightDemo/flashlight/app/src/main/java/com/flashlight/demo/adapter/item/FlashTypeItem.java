package com.flashlight.demo.adapter.item;

import com.flashlight.demo.adapter.DisplayableItem;

public class FlashTypeItem implements DisplayableItem {
    private String nameItem;
    private int idItem;
    private String urlThumb;
    private String urlToggleOn;
    private String urlToggleOff;
    private String urlShop;
    private boolean isPro;
    private boolean isGet;
    private boolean isSelected;


    public FlashTypeItem() {
    }

    public FlashTypeItem(String nameItem, int idItem, String urlThumb, String urlToggleOn, String urlToggleOff, String urlShop, boolean isPro, boolean isGet, boolean isSelected) {
        this.nameItem = nameItem;
        this.idItem = idItem;
        this.urlThumb = urlThumb;
        this.urlToggleOn = urlToggleOn;
        this.urlToggleOff = urlToggleOff;
        this.urlShop = urlShop;
        this.isPro = isPro;
        this.isGet = isGet;
        this.isSelected = isSelected;
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

    public String getUrlThumb() {
        return urlThumb;
    }

    public void setUrlThumb(String urlThumb) {
        this.urlThumb = urlThumb;
    }

    public String getUrlToggleOn() {
        return urlToggleOn;
    }

    public void setUrlToggleOn(String urlToggleOn) {
        this.urlToggleOn = urlToggleOn;
    }

    public String getUrlToggleOff() {
        return urlToggleOff;
    }

    public void setUrlToggleOff(String urlToggleOff) {
        this.urlToggleOff = urlToggleOff;
    }

    public String getUrlShop() {
        return urlShop;
    }

    public void setUrlShop(String urlShop) {
        this.urlShop = urlShop;
    }

    public boolean isPro() {
        return isPro;
    }

    public void setPro(boolean pro) {
        isPro = pro;
    }

    public boolean isGet() {
        return isGet;
    }

    public void setGet(boolean get) {
        isGet = get;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
