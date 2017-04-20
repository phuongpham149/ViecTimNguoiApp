package com.example.phuong.viectimnguoiapp.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by phuong on 21/02/2017.
 */

public class NewItem extends RealmObject {
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

    public NewItem(String id, String idCat, String timeDeadline, String address, String timeCreated, String idUser, String idDistrict, String note, int status) {
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

    public NewItem() {
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

//    protected NewItem(Parcel in) {
//        id = in.readString();
//        idCat = in.readInt();
//        timeCreated = in.readString();
//        timeDeadline = in.readString();
//        note = in.readString();
//        address = in.readString();
//        idDistrict = in.readInt();
//        idUser = in.readString();
//        status = in.readInt();
//    }
//
//    public static final Creator<NewItem> CREATOR = new Creator<NewItem>() {
//        @Override
//        public NewItem createFromParcel(Parcel in) {
//            return new NewItem(in);
//        }
//
//        @Override
//        public NewItem[] newArray(int size) {
//            return new NewItem[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(id);
//        parcel.writeInt(idCat);
//        parcel.writeString(timeCreated);
//        parcel.writeString(timeDeadline);
//        parcel.writeString(note);
//        parcel.writeString(address);
//        parcel.writeInt(idDistrict);
//        parcel.writeString(idUser);
//        parcel.writeInt(status);
//    }
}
