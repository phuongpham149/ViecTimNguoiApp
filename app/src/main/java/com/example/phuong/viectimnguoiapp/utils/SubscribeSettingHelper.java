package com.example.phuong.viectimnguoiapp.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.CategoryJob;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.example.phuong.viectimnguoiapp.objects.SubscribeElement;
import com.example.phuong.viectimnguoiapp.objects.SubscribeSetting;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public final class SubscribeSettingHelper {
    @Getter
    private static SubscribeSettingHelper instance = new SubscribeSettingHelper();
    @Getter
    private SubscribeSetting subscribeSetting;

    public void initialize(Context context) {
        RealmHelper realmHelper = new RealmHelper(context);

        List<CategoryJob> categoryJobs = realmHelper.getCategoryJobs();
        List<SubscribeElement<CategoryJob>> categoryJobElements = new ArrayList<>(categoryJobs.size());
        for (CategoryJob categoryJob : categoryJobs) {
            SubscribeElement<CategoryJob> jobSubscribeElement = new SubscribeElement<>();
            jobSubscribeElement.setEnable(false);
            jobSubscribeElement.setElement(categoryJob);
            categoryJobElements.add(jobSubscribeElement);
        }

        List<District> districts = realmHelper.getDistricts();
        List<SubscribeElement<District>> districtElements = new ArrayList<>(districts.size());
        for (District district : districts) {
            SubscribeElement<District> subscribeElement = new SubscribeElement<>();
            subscribeElement.setEnable(false);
            subscribeElement.setElement(district);
            districtElements.add(subscribeElement);
        }

        subscribeSetting = new SubscribeSetting();
        subscribeSetting.setEnable(false);
        subscribeSetting.setCategoryJobs(categoryJobElements);
        subscribeSetting.setDistricts(districtElements);
        subscribeSetting.setEnable(SharedPreferencesUtils.getInstance().getEnableSubscribeSetting(context));
    }

    public void updateSetting(Context context) {
        String settingJob = SharedPreferencesUtils.getInstance().getSettingJob(context);
        List<SubscribeElement<CategoryJob>> categoryJobs = subscribeSetting.getCategoryJobs();
        char[] jobs = settingJob.toCharArray();
        for (SubscribeElement<CategoryJob> categoryJob : categoryJobs) {
            categoryJob.setEnable(false);
            for (char job : jobs) {
                String jobId = String.valueOf(job);
                if (categoryJob.getElement().getId().equals(jobId)) {
                    categoryJob.setEnable(true);
                }
            }
        }

        String settingAddress = SharedPreferencesUtils.getInstance().getSettingAddress(context);
        char[] addresses = settingAddress.toCharArray();
        List<SubscribeElement<District>> districts = subscribeSetting.getDistricts();
        for (SubscribeElement<District> district : districts) {
            district.setEnable(false);
            for (char address : addresses) {
                String addressId = String.valueOf(address);
                if (district.getElement().getId().equals(addressId)) {
                    district.setEnable(true);
                }
            }
        }

        subscribeSetting.setEnable(SharedPreferencesUtils.getInstance().getEnableSubscribeSetting(context));
    }

    public CategoryJob getCategoryById(String id) {
        List<SubscribeElement<CategoryJob>> categoryJobs = subscribeSetting.getCategoryJobs();
        for (SubscribeElement<CategoryJob> categoryJob : categoryJobs) {
            if (categoryJob.getElement().getId().equals(id))
                return categoryJob.getElement();
        }
        return null;
    }

    public District getDistrictById(String id) {
        List<SubscribeElement<District>> districts = subscribeSetting.getDistricts();
        for (SubscribeElement<District> district : districts) {
            if (district.getElement().getId().equals(id))
                return district.getElement();
        }
        return null;
    }

    public boolean isSuitable(@Nullable String categoryId, @Nullable String districtId) {
        if (subscribeSetting == null)
            return false;

        if (!subscribeSetting.isEnable())
            return false;

        boolean categoryOk = true;

        if (categoryId != null) {
            categoryOk = false;
            List<SubscribeElement<CategoryJob>> categoryJobs = subscribeSetting.getCategoryJobs();
            for (SubscribeElement<CategoryJob> categoryJob : categoryJobs) {
                if (categoryJob.getElement().getId().equals(categoryId) && categoryJob.isEnable()) {
                    categoryOk = true;
                    break;
                }
            }
        }

        if (!categoryOk)
            return false;

        boolean districtOk = true;

        if (districtId != null) {
            districtOk = false;
            List<SubscribeElement<District>> districts = subscribeSetting.getDistricts();
            for (SubscribeElement<District> district : districts) {
                if (district.getElement().getId().equals(districtId) && district.isEnable()) {
                    districtOk = true;
                    break;
                }
            }
        }

        return districtOk;
    }

    private SubscribeSettingHelper() {
    }
}
