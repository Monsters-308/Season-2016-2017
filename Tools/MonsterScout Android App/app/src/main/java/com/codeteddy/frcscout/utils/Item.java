package com.codeteddy.frcscout.utils;

/**
 * Created by Alex on 12.03.2017.
 */

public class Item {

    private String name;
    private int image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Item(String name, int image) {

        this.name = name;
        this.image = image;
    }
}
