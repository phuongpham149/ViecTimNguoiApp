package com.example.phuong.viectimnguoiapp.network.Objects;

import com.example.phuong.viectimnguoiapp.objects.User;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by phuong on 24/02/2017.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
@NoArgsConstructor
@Data
public class ResultListUser {
    private List<User> users;
}
