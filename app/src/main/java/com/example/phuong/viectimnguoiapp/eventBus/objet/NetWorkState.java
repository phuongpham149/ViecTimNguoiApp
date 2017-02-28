package com.example.phuong.viectimnguoiapp.eventBus.objet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by phuong on 28/02/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@NoArgsConstructor
@Data
public class NetWorkState {
    private String state;
}
