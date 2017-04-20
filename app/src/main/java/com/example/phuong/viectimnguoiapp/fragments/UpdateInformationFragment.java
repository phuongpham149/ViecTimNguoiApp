package com.example.phuong.viectimnguoiapp.fragments;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.MainActivity;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.example.phuong.viectimnguoiapp.utils.Constant;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asiantech on 20/04/2017.
 */
@EFragment(R.layout.fragment_update_information)
public class UpdateInformationFragment extends BaseFragment {
    @ViewById(R.id.edtUsername)
    protected EditText mEdtUsername;
    @ViewById(R.id.edtPassword)
    protected EditText mEdtPassword;
    @ViewById(R.id.edtEmail)
    protected EditText mEdtEmail;
    @ViewById(R.id.edtPhone)
    protected EditText mEdtPhone;
    @ViewById(R.id.edtAddress)
    protected EditText mEdtAddress;
    @ViewById(R.id.spinnerDistrict)
    protected Spinner mSpinnerDistrict;
    @ViewById(R.id.edtCoint)
    protected EditText mEdtCoint;

    private RealmHelper mData;
    private User mUserInfor;
    private SharedPreferences mSharedPreferencesUserLogin;
    private int districtBefore;
    private List<String> mDistrictNames;
    private List<District> mDistricts;
    private String mDistrictName;
    private String mDistrictId;


    @Override
    void inits() {
        mSharedPreferencesUserLogin = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        mData = new RealmHelper(getActivity());
        mUserInfor = mData.getUser(mSharedPreferencesUserLogin.getString(Constant.ID_USER_LOGIN, "")).first();
        mEdtUsername.setText(mUserInfor.getUsername());
        mEdtPhone.setText(mUserInfor.getPhone());
        mEdtEmail.setText(mUserInfor.getEmail());
        mEdtAddress.setText(mUserInfor.getAddress());
        mEdtCoint.setText(mUserInfor.getPoint());

        mDistricts = mData.getDistricts();
        List<String> list = new ArrayList<String>();
        //lay chuoi district cho spinner, chuoi district tu ma~ district user
        if (mDistricts != null) {
            for (int i = 0; i < mDistricts.size(); i++) {
                list.add(mDistricts.get(i).getName());
                if (mUserInfor.getIdDistrict() == Integer.parseInt(mDistricts.get(i).getId()))
                    districtBefore = i;
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>((MainActivity) getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerDistrict.setAdapter(dataAdapter);
        mSpinnerDistrict.setSelection(districtBefore);
        mSpinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDistrictName = parent.getItemAtPosition(position).toString();
                if (mDistricts != null) {
                    for (District district : mDistricts) {
                        if (mDistrictName.equals(district.getName()))
                            mDistrictId = district.getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}
