package com.example.phuong.viectimnguoiapp.objects;

import lombok.Data;

@Data
public class SubscribeElement<T> {
    private T element;
    private boolean isEnable;
}
