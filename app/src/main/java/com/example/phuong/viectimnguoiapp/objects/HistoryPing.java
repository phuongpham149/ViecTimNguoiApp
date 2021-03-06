package com.example.phuong.viectimnguoiapp.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asiantech on 27/03/2017.
 */

public class HistoryPing extends RealmObject {
    @PrimaryKey
    private String idPost;
    private String titlePost;
    private String timeDeadline;
    private String price;
    private String note;
    private String address;
    private String userOwner;
    private String idUser;
    private String nameDistrict;
    private String choice;
    private String report;
    private String confirm;

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public HistoryPing() {
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getTitlePost() {
        return titlePost;
    }

    public void setTitlePost(String titlePost) {
        this.titlePost = titlePost;
    }

    public String getTimeDeadline() {
        return timeDeadline;
    }

    public void setTimeDeadline(String timeDeadline) {
        this.timeDeadline = timeDeadline;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(String userOwner) {
        this.userOwner = userOwner;
    }

    public String getNameDistrict() {
        return nameDistrict;
    }

    public void setNameDistrict(String nameDistrict) {
        this.nameDistrict = nameDistrict;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    @Override
    public String toString() {
        return "HistoryPing{" +
                "idPost='" + idPost + '\'' +
                ", titlePost='" + titlePost + '\'' +
                ", timeDeadline='" + timeDeadline + '\'' +
                ", price='" + price + '\'' +
                ", note='" + note + '\'' +
                ", address='" + address + '\'' +
                ", userOwner='" + userOwner + '\'' +
                ", idUser='" + idUser + '\'' +
                ", nameDistrict='" + nameDistrict + '\'' +
                '}';
    }
}
