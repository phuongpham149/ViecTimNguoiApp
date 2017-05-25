package com.example.phuong.viectimnguoiapp.objects;

import java.util.List;

import lombok.Data;

@Data
public class SubscribeSetting {
    private List<SubscribeElement<CategoryJob>> categoryJobs;
    private List<SubscribeElement<District>> districts;
    private boolean enable;
}
