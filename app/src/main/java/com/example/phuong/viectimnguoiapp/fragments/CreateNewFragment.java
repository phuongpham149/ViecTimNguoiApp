package com.example.phuong.viectimnguoiapp.fragments;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.objects.CategoryJob;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by asiantech on 13/03/2017.
 */
@EFragment(R.layout.fragment_create_new)
public class CreateNewFragment extends BaseFragment {
    @ViewById(R.id.spCatJob)
    Spinner mSpCatJob;
    @ViewById(R.id.spDistrict)
    Spinner mSpDistrict;
    @ViewById(R.id.edtNote)
    EditText mEdtNote;
    @ViewById(R.id.edtAddress)
    EditText mEdtAddress;
    @ViewById(R.id.btnPost)
    Button mBtnPost;

    private List<CategoryJob> mCategoryJobs = new ArrayList<>();
    private List<District> mDistricts = new ArrayList<>();

    private Firebase mFirebaseCategoryJob;
    private Firebase mFirebaseDistrict;
    private ArrayAdapter<CategoryJob> mAdapterCatJob;
    private ArrayAdapter<District> mAdapterDistrict;


    @Override
    void inits() {

        Firebase.setAndroidContext(getActivity());
        mFirebaseCategoryJob = new Firebase("https://viectimnguoi-469e6.firebaseio.com/categoryJobs");
        mFirebaseDistrict = new Firebase("https://viectimnguoi-469e6.firebaseio.com/districts");

        getCategoryJob();
        getDistrics();




    }

    public void getCategoryJob() {

        mFirebaseCategoryJob.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot obj : snapshot.getChildren()) {
                    CategoryJob categoryJob = obj.getValue(CategoryJob.class);
                    mCategoryJobs.add(categoryJob);
                }
                mAdapterCatJob = new ArrayAdapter<CategoryJob>(getActivity(), android.R.layout.simple_spinner_item, mCategoryJobs);
                mSpCatJob.setAdapter(mAdapterCatJob);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    public void getDistrics() {
        mFirebaseDistrict.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot obj : snapshot.getChildren()) {
                    District district = obj.getValue(District.class);
                    mDistricts.add(district);
                }
                mAdapterDistrict = new ArrayAdapter<District>(getActivity(), android.R.layout.simple_spinner_item, mDistricts);
                mSpDistrict.setAdapter(mAdapterDistrict);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }
}
