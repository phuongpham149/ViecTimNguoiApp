package com.example.phuong.viectimnguoiapp.objects;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by phuong on 21/02/2017.
 */
@NoArgsConstructor()
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class NewItem {
    private Date date;
    private String title;
    private String detail;
    private int idUser;
}
