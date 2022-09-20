package com.flashlight.demo.api.request;

import com.flashlight.demo.adapter.DisplayableItem;

public class FlashType implements DisplayableItem {

    private String eventName;
    private int eventId;
    private String urlThumb;
    private boolean isPro;
    private String toggleON;
    private String toggleOFF;
    private String urlShop;

    public FlashType() {
    }

    public FlashType(String eventName, int eventId, String urlThumb, String toggleON, String toggleOFF, String urlShop, boolean isPro) {
        this.eventName = eventName;
        this.eventId = eventId;
        this.urlThumb = urlThumb;
        this.toggleON = toggleON;
        this.toggleOFF = toggleOFF;
        this.urlShop = urlShop;
        this.isPro = isPro;
    }

    public FlashType(String eventName, int eventId, String urlThumb, String urlPreview, boolean isPro) {
        this.eventName = eventName;
        this.eventId = eventId;
        this.urlThumb = urlThumb;
        this.isPro = isPro;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getUrlThumb() {
        return urlThumb;
    }

    public void setUrlThumb(String urlThumb) {
        this.urlThumb = urlThumb;
    }

    public boolean isPro() {
        return isPro;
    }

    public void setPro(boolean pro) {
        isPro = pro;
    }

    public String getToggleON() {
        return toggleON;
    }

    public void setToggleON(String toggleON) {
        this.toggleON = toggleON;
    }

    public String getToggleOFF() {
        return toggleOFF;
    }

    public void setToggleOFF(String toggleOFF) {
        this.toggleOFF = toggleOFF;
    }

    public String getUrlShop() {
        return urlShop;
    }

    public void setUrlShop(String urlShop) {
        this.urlShop = urlShop;
    }


}
