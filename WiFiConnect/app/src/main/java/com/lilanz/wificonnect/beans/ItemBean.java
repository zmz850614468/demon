package com.lilanz.wificonnect.beans;

public class ItemBean {

    public int imageId;

    public String name;

    public int voiceId;

    public ItemBean() {

    }

    public ItemBean(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;
        this.voiceId = 0;
    }

    public ItemBean(int imageId, String name, int voiceId) {
        this.imageId = imageId;
        this.name = name;
        this.voiceId = voiceId;
    }

}
