package com.example.phuong.viectimnguoiapp.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by phuong on 21/02/2017.
 */
@Data
public class NewSave extends RealmObject {
    @PrimaryKey
    private String id;
    private String idCat;
    private String timeDeadline;
    private String address;

    public NewSave() {
    }

    public NewSave(String id, String idCat, String timeDeadline, String address, String timeCreated, String idUser, String idDistrict, String note, String status) {
        this.id = id;
        this.idCat = idCat;
        this.timeDeadline = timeDeadline;
        this.address = address;
        this.timeCreated = timeCreated;
        this.idUser = idUser;
        this.idDistrict = idDistrict;
        this.note = note;
        this.status = status;
    }

    private String timeCreated;
    private String idUser;
    private String idDistrict;
    private String note;
    private String status;
}
