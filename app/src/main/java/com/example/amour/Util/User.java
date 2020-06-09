package com.example.amour.Util;

import java.io.Serializable;

public class User implements Serializable {
    String userId, age, height, i_am, i_appreciate, i_like, username, pref_gender, image_link, sex, degree;
    int pref_age_min_val, pref_age_max_val, pref_height_min_val, pref_height_max_val;

    public User() {
    }

    public User(String username, String age, String height, String i_am, String i_appreciate,
                String i_like, int pref_age_min_val, int pref_age_max_val,
                int pref_height_min_val, int pref_height_max_val,
                String pref_gender, String imageLink, String sex, String degree) {
        this.username = username;
        this.age = age;
        this.height = height;
        this.i_am = i_am;
        this.i_appreciate = i_appreciate;
        this.i_like = i_like;
        this.pref_age_min_val = pref_age_min_val;
        this.pref_age_max_val = pref_age_max_val;
        this.pref_gender = pref_gender;
        this.pref_height_min_val = pref_height_min_val;
        this.pref_height_max_val = pref_height_max_val;
        this.image_link = imageLink;
        this.sex = sex;
        this.degree = degree;
    }

    public String getAge() {
        return age;
    }

    public String getHeight() {
        return height;
    }

    public String getI_am() {
        return i_am;
    }

    public String getI_appreciate() {
        return i_appreciate;
    }

    public String getI_like() {
        return i_like;
    }

    public Number getpref_age_min_val() {
        return pref_age_min_val;
    }

    public Number getPref_age_max_val() {
        return pref_age_max_val;
    }

    public String getPref_gender() {
        return pref_gender;
    }

    public String getImage_link() {
        return image_link;
    }

    public int getPref_height_min_val() {
        return pref_height_min_val;
    }

    public int getPref_height_max_val() {
        return pref_height_max_val;
    }

    public String getUsername() {
        return username;
    }

    public String getSex() {
        return sex;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setI_appreciate_text(String i_appreciate) {
        this.i_appreciate = i_appreciate;
    }

    public void setI_am(String i_am) {
        this.i_am = i_am;
    }

    public void setI_appreciate(String i_appreciate) {
        this.i_appreciate = i_appreciate;
    }

    public void setI_like(String i_like) {
        this.i_like = i_like;
    }


    public void setPref_age_min_val(int pref_age_min_val) {
        this.pref_age_min_val = pref_age_min_val;
    }

    public void setI_like_text(String i_like) {
        this.i_like = i_like;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPref_gender(String pref_gender) {
        this.pref_gender = pref_gender;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public void setPref_age_max_val(int pref_age_max_val) {
        this.pref_age_max_val = pref_age_max_val;
    }

    public void setPref_height_min_val(int pref_height_min_val) {
        this.pref_height_min_val = pref_height_min_val;
    }

    public void setPref_height_max_val(int pref_height_max_val) {
        this.pref_height_max_val = pref_height_max_val;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
