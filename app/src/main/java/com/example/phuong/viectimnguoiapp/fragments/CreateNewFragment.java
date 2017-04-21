package com.example.phuong.viectimnguoiapp.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.eventBus.object.NetWorkState;
import com.example.phuong.viectimnguoiapp.objects.CategoryJob;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.firebase.client.Firebase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by asiantech on 13/03/2017.
 */
@EFragment(R.layout.fragment_create_new)
public class CreateNewFragment extends BaseFragment implements Validator.ValidationListener {
    @ViewById(R.id.spCatJob)
    Spinner mSpCatJob;
    @ViewById(R.id.spDistrict)
    Spinner mSpDistrict;

    @ViewById(R.id.edtNote)
    EditText mEdtNote;

    @NotEmpty(message = "Vui lòng điền địa chỉ")
    @ViewById(R.id.edtAddress)
    EditText mEdtAddress;

    @ViewById(R.id.btnPost)
    Button mBtnPost;

    @ViewById(R.id.prograssBarLoading)
    ProgressBar mProgressBarLoading;

    @NotEmpty(message = "Vui lòng thời gian hết hạn")
    @ViewById(R.id.edtTimeDeadline)
    EditText mEdtTimeDeadline;

    private List<String> mCategoryJobs = new ArrayList<>();
    private List<String> mDistricts = new ArrayList<>();
    private Firebase mFirebasePost;

    private SharedPreferences mSharedPreferencesUser;
    private ArrayAdapter<String> mAdapterCatJob;
    private ArrayAdapter<String> mAdapterDistrict;

    private Validator mValidator;
    private DateFormat timeFormat;
    private RealmHelper mData;
    private String dateDeadline = "";

    @Override
    void inits() {
        mData = new RealmHelper(getActivity());
        timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        mFirebasePost = new Firebase("https://viectimnguoi-469e6.firebaseio.com/posts");
        mSharedPreferencesUser = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);


        List<CategoryJob> listJobs = mData.getCategoryJobs();
        for (CategoryJob categoryJob : listJobs) {
            mCategoryJobs.add(categoryJob.getName());
        }

        List<District> listDistricts = mData.getDistricts();
        for (District district : listDistricts) {
            mDistricts.add(district.getName());
        }

        mAdapterCatJob = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mCategoryJobs);
        mAdapterCatJob.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpCatJob.setAdapter(mAdapterCatJob);

        mAdapterDistrict = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mDistricts);
        mAdapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpDistrict.setAdapter(mAdapterDistrict);

        mSpDistrict.setSelection(0);
        mSpCatJob.setSelection(0);

    }

    @Click(R.id.edtTimeDeadline)
    public void setTimeDeadline() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final String dateCurrent = timeFormat.format(new Date());

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateDeadline = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        if (dateDeadline.compareTo(dateCurrent) > 0) {
                            Common.createDialog(getActivity(), "Thời gian hết hạn chưa chính xác");
                            mEdtTimeDeadline.setText(dateDeadline);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Click(R.id.btnPost)
    public void postAction() {
        mValidator.validate();
    }

    @Subscribe
    public void receiverStateOnNetwork(NetWorkState netWorkState) {
        if (netWorkState.getState().equals(Constant.WIFI_ENABLE) || netWorkState.getState().equals(Constant.MOBILE_DATA_ENABLE)) {
            mValidator.validate();
        }
    }

    public void doCreateNew() {
        mProgressBarLoading.setVisibility(View.VISIBLE);

        if (Network.checkNetWork(getActivity(), Constant.TYPE_NETWORK) || Network.checkNetWork(getActivity(), Constant.TYPE_WIFI)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("id", UUID.randomUUID().toString());
            map.put("idCat", getCạtobSelected());
            map.put("idDistrict", getDistrictSelected());
            map.put("address", mEdtAddress.getText().toString());
            map.put("idUser", mSharedPreferencesUser.getString(Constant.ID_USER_LOGIN, ""));
            map.put("note", mEdtNote.getText().toString());
            map.put("status", Constant.STATUS_NEW);
            map.put("timeCreated", timeFormat.format(new Date()));
            map.put("timeDeadline", mEdtTimeDeadline.getText().toString());
            mFirebasePost.push().setValue(map);
            Common.createDialog(getActivity(), "Đăng bài thành công");
            mProgressBarLoading.setVisibility(View.GONE);
        } else {
            Common.createDialog(getActivity(), "Vui lòng kiếm tra kết nối");
            mProgressBarLoading.setVisibility(View.GONE);
        }
    }

    public String getDistrictSelected() {
        String result = "";
        switch (mSpDistrict.getSelectedItemPosition()) {
            case 0:
                result = "1";
                break;
            case 1:
                result = "2";
                break;
            case 2:
                result = "7";
                break;
            case 3:
                result = "3";
                break;
            case 4:
                result = "4";
                break;
            case 5:
                result = "5";
                break;
            case 6:
                result = "6";
                break;
        }
        return result;
    }

    public String getCạtobSelected() {
        String result = "";
        switch (mSpCatJob.getSelectedItemPosition()) {
            case 0:
                result = "1";
                break;
            case 1:
                result = "2";
                break;
            case 2:
                result = "3";
                break;
            case 3:
                result = "3";
                break;
            case 4:
                result = "5";
                break;
            case 5:
                result = "5";
                break;
        }
        return result;
    }

    @Override
    public void onValidationSucceeded() {
        doCreateNew();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        String errorMessage = errors.get(0).getCollatedErrorMessage(getActivity());
        if (errorMessage != null) {
            String[] messageErrors = errorMessage.split("\n");
            if (messageErrors.length > 0) {
                Common.createDialog(getActivity(), messageErrors[0]);
            }
        }
    }
}
