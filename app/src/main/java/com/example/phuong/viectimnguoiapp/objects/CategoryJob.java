package com.example.phuong.viectimnguoiapp.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by phuong on 14/03/2017.
 */

public class CategoryJob extends RealmObject {
    @PrimaryKey
    private String id;
    private String name;

    public CategoryJob(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryJob() {
    }

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
}
