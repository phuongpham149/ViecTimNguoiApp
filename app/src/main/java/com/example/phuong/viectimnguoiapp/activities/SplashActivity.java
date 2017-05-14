package com.example.phuong.viectimnguoiapp.activities;

import android.os.Handler;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.CategoryJob;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.EActivity;

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
                    District district = obj.getValue(District.class);
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
                    CategoryJob categoryJob = obj.getValue(CategoryJob.class);
                    mData.addCategoryJob(categoryJob);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
