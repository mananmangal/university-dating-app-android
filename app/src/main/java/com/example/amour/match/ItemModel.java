package com.example.amour.match;

public class ItemModel {
    private int image;
    private String name, totalLikes, kota;

    public ItemModel() {
    }

    public ItemModel(int image, String name, String totalLikes, String kota) {
        this.image = image;
        this.name = name;
        this.totalLikes = totalLikes;
        this.kota = kota;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public String getKota() {
        return kota;
    }
}
