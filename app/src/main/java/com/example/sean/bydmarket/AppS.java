package com.example.sean.bydmarket;

import android.support.annotation.NonNull;

public class AppS implements Comparable<AppS> {

    private String name;
    private int imageId;
    private int id;
    private String result;

    public AppS(String name, int imageId, int id, String result) {
        this.name = name;
        this.imageId = imageId;
        this.id = id;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

   @Override
    public int compareTo(@NonNull AppS o) {
        if(this.getId() >= o.getId()) {
            return 1;
        } else {
            return -1;
        }
    }
}
