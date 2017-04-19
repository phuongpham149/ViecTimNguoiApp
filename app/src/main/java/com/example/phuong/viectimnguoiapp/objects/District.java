package com.example.phuong.viectimnguoiapp.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asiantech on 14/03/2017.
 */

public class District extends RealmObject{
    @PrimaryKey
    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public District(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public District() {
    }
}
