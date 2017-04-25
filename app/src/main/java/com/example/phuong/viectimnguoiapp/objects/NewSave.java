package com.example.phuong.viectimnguoiapp.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by phuong on 21/02/2017.
 */

public class NewSave extends RealmObject {
    @PrimaryKey
    private String id;
    private String idCat;
    private String timeDeadline;
    private String address;
    private String timeCreated;
    private String idUser;
    private String idDistrict;
    private String note;
    private int status;

    public NewSave(String id, String idCat, String timeDeadline, String address, String timeCreated, String idUser, String idDistrict, String note, int status) {
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

    public NewSave() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCat() {
        return idCat;
    }

    public void setIdCat(String idCat) {
        this.idCat = idCat;
    }

    public String getTimeDeadline() {
        return timeDeadline;
    }

    public void setTimeDeadline(String timeDeadline) {
        this.timeDeadline = timeDeadline;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(String idDistrict) {
        this.idDistrict = idDistrict;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
