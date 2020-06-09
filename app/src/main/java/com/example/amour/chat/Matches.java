package com.example.amour.chat;


public class Matches {
    public String username, image_link;

    public Matches()
    {

    }

    public Matches(String username, String image_link) {
        this.username = username;

        this.image_link = image_link;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }
}
