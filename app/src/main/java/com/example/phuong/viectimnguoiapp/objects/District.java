package com.example.phuong.viectimnguoiapp.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by asiantech on 14/03/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@NoArgsConstructor
@Data
public class District {
    private String id;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
