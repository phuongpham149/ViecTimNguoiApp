package com.example.phuong.viectimnguoiapp.activities;

import android.app.Dialog;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.eventBus.BusProvider;
import com.example.phuong.viectimnguoiapp.eventBus.objet.NetWorkState;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Helpers;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by phuong on 23/02/2017.
 */
@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity implements Validator.ValidationListener {

    @NotEmpty(message = "Vui lòng điền tên tài khoản")
    @ViewById(R.id.edtUsername)
    EditText mEdtUsername;

    @NotEmpty(message = "Vui lòng điền địa chỉ email")
    @ViewById(R.id.edtEmail)
    EditText mEdtEmail;

    @NotEmpty(message = "Vui lòng điền số điện thoại")
    @ViewById(R.id.edtPhone)
    EditText mEdtPhone;

    @NotEmpty(message = "Vui lòng điền địa chỉ")
    @ViewById(R.id.edtAddress)
    EditText mEdtAddress;

    @ViewById(R.id.spinnerAddress)
    Spinner mSpinnerAddress;

    @Length(trim = true, min = 8, message = "Mật khẩu ít nhất chứa 8 kí tự")
    @NotEmpty(message = "Vui lòng điền mật khẩu")
    @ViewById(R.id.edtPassword)
    EditText mEdtPassword;

    @ViewById(R.id.progressBarRegister)
    ProgressBar mProgressBarRegister;

    private Validator mValidator;
    private boolean check = true;
    private Firebase mFirebase;

    private List<District> mDistricts = new ArrayList<>();
    private Firebase mFirebaseDistrict;
    private ArrayAdapter<District> mAdapterDistrict;

    @Override
    void inits() {
        Helpers.hideSoftKeyboard(this, this.getCurrentFocus());
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://viectimnguoi-469e6.firebaseio.com/users");
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        getDistrics();
        mSpinnerAddress.setSelection(0);
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void receiverStateOnNetwork(NetWorkState netWorkState) {
        if (netWorkState.getState().equals(Constant.WIFI_ENABLE) || netWorkState.getState().equals(Constant.MOBILE_DATA_ENABLE)) {
            mValidator.validate();
        }
    }

    public void getDistrics() {
        mFirebaseDistrict = new Firebase("https://viectimnguoi-469e6.firebaseio.com/districts");
        mFirebaseDistrict.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot obj : snapshot.getChildren()) {
                    District district = obj.getValue(District.class);
                    mDistricts.add(district);
                }
                mAdapterDistrict = new ArrayAdapter<District>(RegisterActivity.this, android.R.layout.simple_spinner_item, mDistricts);
                mAdapterDistrict.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSpinnerAddress.setAdapter(mAdapterDistrict);
            }

            @Override
            public void onCancelled(FirebaseError error) {
            }
        });
    }

    @Click(R.id.btnRegister)
    public void RegisterAction() {
        mValidator.validate();
    }

    @Click(R.id.imgBack)
    public void BackAction() {
        LoginActivity_.intent(this).start();
    }

    public void doRegister() {
        District district = null;
        if (Network.checkNetWork(this, Constant.TYPE_NETWORK) || Network.checkNetWork(this, Constant.TYPE_WIFI)) {
            if (check) {
                if (!(mSpinnerAddress.getSelectedItem() == null)) {
                    district = (District) mSpinnerAddress.getSelectedItem();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", UUID.randomUUID().toString());
                    map.put("username", mEdtUsername.getText().toString());
                    map.put("password", Helpers.sha256(mEdtPassword.getText().toString()));
                    map.put("address", mEdtAddress.getText().toString());
                    map.put("idDistrict", district.getId());
                    map.put("email", mEdtEmail.getText().toString());
                    map.put("phone", mEdtPhone.getText().toString());
                    map.put("status", Constant.USER_ACTIVE);
                    map.put("type", Constant.USER_SYSTEM);
                    map.put("point", "0");
                    mFirebase.push().setValue(map);
                    mProgressBarRegister.setVisibility(View.GONE);
                    showDialogSuccess("Đăng ký thành công.Mời bạn đăng nhập.");
                } else {
                    showDialogSuccess("Vui lòng chọn huyện.");
                }
            } else {
                Common.createDialog(this, "username đã tồn tại, Vui lòng điền username khác");
                mProgressBarRegister.setVisibility(View.GONE);
            }
        } else {
            Common.createDialog(this, "Vui lòng kiếm tra kết nối");
            mProgressBarRegister.setVisibility(View.GONE);
        }

    }

    @Override
    public void onValidationSucceeded() {
        mProgressBarRegister.setVisibility(View.VISIBLE);
        isCheckUsername();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doRegister();
            }
        }, 2000);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        String errorMessage = errors.get(0).getCollatedErrorMessage(this);
        if (errorMessage != null) {
            String[] messageErrors = errorMessage.split("\n");
            if (messageErrors.length > 0) {
                Common.createDialog(this, messageErrors[0]);
            }
        }
    }

    public void isCheckUsername() {
        check = true;

        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String userName = map.get("username").toString();
                if (check) {
                    if (userName.equals(mEdtUsername.getText().toString())) {
                        check = false;
                        return;
                    } else {
                        check = true;
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void showDialogSuccess(String message) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);
        TextView tvContent = (TextView) dialog.findViewById(R.id.tvContent);
        TextView btnOk = (TextView) dialog.findViewById(R.id.tvBtnOk);

        if (!"".equals(message)) {
            tvContent.setText(message);
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                LoginActivity_.intent(RegisterActivity.this).username(mEdtUsername.getText().toString()).password(mEdtPassword.getText().toString()).start();
            }
        });

        dialog.show();
    }
}

