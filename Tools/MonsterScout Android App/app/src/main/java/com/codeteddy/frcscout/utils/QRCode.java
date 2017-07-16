package com.codeteddy.frcscout.utils;

/**
 * @author Alex
 * Created by Alex on 10.03.2017.
 */

public class QRCode {

    private int id;
    private String type;
    private String qrcode;
    private boolean uploaded;

    public QRCode(int id, String type, String qrcode, boolean uploaded) {
        this.id = id;
        this.type = type;
        this.qrcode = qrcode;
        this.uploaded = uploaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
}
