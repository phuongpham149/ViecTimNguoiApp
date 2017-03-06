package com.example.phuong.viectimnguoiapp.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by phuong on 21/02/2017.
 */
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class MenuItem {
    private int icon;
    private String title;
}
