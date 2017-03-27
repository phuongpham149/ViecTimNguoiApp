package com.example.phuong.viectimnguoiapp.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by phuong on 24/02/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@NoArgsConstructor
@Data
public class User {
    private String address;
    private String email;
    private String id;
    private String password;
    private String role;
    private String status;
    private String username;
    private String phone;
    private int idDistrict;
    private String point;
}
