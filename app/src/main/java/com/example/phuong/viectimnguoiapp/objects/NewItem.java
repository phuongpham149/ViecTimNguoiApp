package com.example.phuong.viectimnguoiapp.objects;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;

/**
 * Created by phuong on 21/02/2017.
 */
@Data
public class NewItem extends RealmObject implements Serializable {
    @PrimaryKey
    private String id;
    private String idCat;
    private String timeDeadline;
    private String address;
    private String timeCreated;
    private String idUser;
    private String idDistrict;
    private String note;
    private String status;

}
