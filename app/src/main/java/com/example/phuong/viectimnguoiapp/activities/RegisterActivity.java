package com.example.phuong.viectimnguoiapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.eventBus.BusProvider;
import com.example.phuong.viectimnguoiapp.eventBus.objet.NetWorkState;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Helpers;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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

    @Length(trim = true, min = 8, message = "Mật khẩu ít nhất chứa 8 kí tự")
    @NotEmpty(message = "Vui lòng điền mật khẩu")
    @ViewById(R.id.edtPassword)
    EditText mEdtPassword;

    @Length(trim = true, min = 8, message = "Mật khẩu ít nhất chứa 8 kí tự")
    @NotEmpty(message = "Vui lòng điền lại mật khẩu")
    @ViewById(R.id.edtRePassword)
    EditText mEdtRePassword;

    @ViewById(R.id.radioHide)
    RadioButton mRdHide;

    @ViewById(R.id.radioWork)
    RadioButton mRdWork;

    private Validator mValidator;
    private boolean check = true;
    private String role = Constant.USER_WORK;
    private ProgressDialog pd;
    private Firebase mFirebase;

    @Override
    void inits() {
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://viectimnguoi-469e6.firebaseio.com/users");
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void receiverStateOnNetwork(NetWorkState netWorkState) {
        if (netWorkState.getState().equals(Constant.WIFI_ENABLE) || netWorkState.getState().equals(Constant.MOBILE_DATA_ENABLE)) {
            mValidator.validate();
        }
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
        if (Network.checkNetWork(this, Constant.TYPE_NETWORK) || Network.checkNetWork(this, Constant.TYPE_WIFI)) {
            if (check) {
                if (mEdtPassword.getText().toString().equals(mEdtRePassword.getText().toString())) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id", UUID.randomUUID().toString());
                    map.put("username", mEdtUsername.getText().toString());
                    map.put("password", Helpers.sha256(mEdtPassword.getText().toString()));
                    map.put("address", "");
                    map.put("email", "");
                    map.put("role", Constant.USER_ACTIVE);
                    map.put("status", role);
                    mFirebase.push().setValue(map);
                    pd.dismiss();
                    showDialogSuccess("Đăng ký thành công.Mời bạn đăng nhập.");
                } else {
                    Common.createDialog(this, "Đăng ký thất bại", "", false, pd);
                }
            } else {
                Common.createDialog(this, "username đã tồn tại, Vui lòng điền username khác", "", false, pd);
            }
        } else {
            Common.createDialog(this, "Please check your network", "", false, pd);
        }

    }

    @Override
    public void onValidationSucceeded() {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();
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
                Common.createDialog(this, messageErrors[0], "", false, pd);
                mEdtUsername.setText("");
                mEdtPassword.setText("");
                mEdtRePassword.setText("");
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

    @CheckedChange(R.id.radioWork)
    public void onCheckChangeRadioWork() {
        role = Constant.USER_WORK;
    }

    @CheckedChange(R.id.radioHide)
    public void onCheckChangeRadioHide() {
        role = Constant.USER_HIDE;
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

