package com.example.phuong.viectimnguoiapp.objects;

import lombok.Data;

/**
 * Created by asiantech on 19/04/2017.
 */
@Data
public class Setting {
    private String jobSetting;
    private String addressSetting;

    public Setting() {

    }

    public Setting( String jobSetting,String addressSetting) {
        this.addressSetting = addressSetting;
        this.jobSetting = jobSetting;
    }
}