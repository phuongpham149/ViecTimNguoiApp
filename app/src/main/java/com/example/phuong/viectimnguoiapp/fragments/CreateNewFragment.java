package com.example.phuong.viectimnguoiapp.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.eventBus.object.NetWorkState;
import com.example.phuong.viectimnguoiapp.objects.CategoryJob;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
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

    @NotEmpty(message = "Vui lòng thời gian hết hạn")
    @ViewById(R.id.edtTimeDeadline)
    EditText mEdtTimeDeadline;

    private List<CategoryJob> mCategoryJobs = new ArrayList<>();
    private List<District> mDistricts = new ArrayList<>();

    private Firebase mFirebaseCategoryJob;
    private Firebase mFirebaseDistrict;
    private Firebase mFirebasePost;

    private SharedPreferences mSharedPreferencesUser;
    private ArrayAdapter<CategoryJob> mAdapterCatJob;
    private ArrayAdapter<District> mAdapterDistrict;

    private ProgressDialog mProgressDialogLoading;
    private Validator mValidator;
    private DateFormat timeFormat;

    @Override
    void inits() {
        timeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Firebase.setAndroidContext(getActivity());
        mFirebaseCategoryJob = new Firebase("https://viectimnguoi-469e6.firebaseio.com/categoryJobs");
        mFirebaseDistrict = new Firebase("https://viectimnguoi-469e6.firebaseio.com/districts");
        mFirebasePost = new Firebase("https://viectimnguoi-469e6.firebaseio.com/posts");
        mSharedPreferencesUser = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        getCategoryJob();
        getDistrics();

        mSpDistrict.setSelection(0);
        mSpCatJob.setSelection(0);

    }

    @Click(R.id.edtTimeDeadline)
    public void setTimeDeadline() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        mEdtTimeDeadline.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
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
        mProgressDialogLoading = new ProgressDialog(getActivity());
        mProgressDialogLoading.setMessage("Loading...");
        mProgressDialogLoading.show();

        if (Network.checkNetWork(getActivity(), Constant.TYPE_NETWORK) || Network.checkNetWork(getActivity(), Constant.TYPE_WIFI)) {
            CategoryJob categoryJob = null;
            District district = null;

            if (!(mSpCatJob.getSelectedItem() == null)) {
                categoryJob = (CategoryJob) mSpCatJob.getSelectedItem();
            }
            if (!(mSpDistrict.getSelectedItem() == null)) {
                district = (District) mSpDistrict.getSelectedItem();
            }

            Map<String, String> map = new HashMap<String, String>();
            map.put("id", UUID.randomUUID().toString());
            map.put("idCat", categoryJob.getId());
            map.put("idDistrict", district.getId());
            map.put("address", mEdtAddress.getText().toString());
            map.put("idUser", mSharedPreferencesUser.getString(Constant.ID_USER_LOGIN, ""));
            map.put("note", mEdtNote.getText().toString());
            map.put("status", Constant.STATUS_NEW);
            map.put("timeCreated", timeFormat.format(new Date()).toString());
            map.put("timeDeadline", mEdtTimeDeadline.getText().toString());
            mFirebasePost.push().setValue(map);
            Common.createDialog(getActivity(), "Đăng bài thành công");
        } else {
            Common.createDialog(getActivity(), "Vui lòng kiếm tra kết nối");
        }
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
                mEdtAddress.requestFocus();
            }
        }
    }
}
