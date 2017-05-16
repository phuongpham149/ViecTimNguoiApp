package com.example.phuong.viectimnguoiapp.activities;

import android.os.Handler;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.CategoryJob;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.example.phuong.viectimnguoiapp.utils.SharedPreferencesUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.EActivity;

import java.util.HashMap;

/**
 * Created by asiantech on 06/03/2017.
 */
@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    private DatabaseReference mFirebaseDistrict;
    private DatabaseReference mFirebaseCategoryJob;
    private RealmHelper mData;

    @Override
    void inits() {
        if (SharedPreferencesUtils.getInstance().isUserLogin(getApplicationContext()).equals("true")) {
            MainActivity_.intent(SplashActivity.this).start();
        }
        mData = new RealmHelper(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataDistrict();
                getDataCategoryJob();
                LoginActivity1_.intent(SplashActivity.this).start();
            }
        }, 3000);
    }

    public void getDataDistrict() {
        mFirebaseDistrict = FirebaseDatabase.getInstance().getReference("/districts");
        mFirebaseDistrict.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    HashMap<String, Object> map = (HashMap<String, Object>) obj.getValue();
                    District district = new District(map.get("id").toString(), map.get("name").toString());
                    mData.addDistrict(district);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getDataCategoryJob() {
        mFirebaseCategoryJob = FirebaseDatabase.getInstance().getReference("/categoryJobs");
        mFirebaseCategoryJob.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    HashMap<String, Object> map = (HashMap<String, Object>) obj.getValue();
                    CategoryJob categoryJob = new CategoryJob(map.get("id").toString(), map.get("name").toString());
                    mData.addCategoryJob(categoryJob);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
