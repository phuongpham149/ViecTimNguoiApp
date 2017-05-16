package com.example.phuong.viectimnguoiapp.objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.libs.com.zwitserloot.cmdreader.Requires;

/**
 * Created by phuong on 24/02/2017.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String id;
    @Requires("")
    private String address;
    @Requires("")
    private String email;
    @Requires("")
    private String password;
    @Requires("")
    private String type;
    private String status;
    @Requires("")
    private String username;
    private String phone;
    private int idDistrict;
    private String point;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(int idDistrict) {
        this.idDistrict = idDistrict;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public User(String id, String address, String email, String password, String type, String status, String username, String phone, int idDistrict, String point) {
        this.id = id;
        this.address = address;
        this.email = email;
        this.password = password;
        this.type = type;
        this.status = status;
        this.username = username;
        this.phone = phone;
        this.idDistrict = idDistrict;
        this.point = point;
    }

    public User() {
    }
}
