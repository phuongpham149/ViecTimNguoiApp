package com.example.phuong.viectimnguoiapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.eventBus.BusProvider;
import com.example.phuong.viectimnguoiapp.eventBus.objet.NetWorkState;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.otto.Subscribe;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by phuong on 23/02/2017.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements Validator.ValidationListener {

    @Extra
    protected String username;
    @Extra
    protected String password;

    @NotEmpty(message = "Vui lòng điền tên tài khoản")
    @ViewById(R.id.edtUsername)
    EditText mEdtUsername;

    @Length(trim = true, min = 8, message = "Mật khẩu ít nhất chứa 8 kí tự")
    @NotEmpty(message = "Vui lòng điền mật khẩu")
    @ViewById(R.id.edtPassword)
    EditText mEdtPassword;

    @ViewById(R.id.tvLoginFaceBook)
    Button mTvLoginFacebook;
    @ViewById(R.id.btnLogin)
    Button mBtnLogin;
    @ViewById(R.id.tvRegister)
    TextView mTvRegister;

    private ProgressDialog mProgressDialogLoading;
    private Validator mValidator;
    private Firebase mFirebase;
    private boolean check;
    private User mUser;
    private int mStatusBlockUser = 0;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private CallbackManager callbackManager;

    private String mSettingJob = "";
    private String mSettingAddress = "";

    @Override
    void inits() {
        if (!"".equals(username) && !"".equals(password)) {
            mEdtUsername.setText(username);
            mEdtPassword.setText(password);
        }
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://viectimnguoi-469e6.firebaseio.com/users");
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mSharedPreferences = getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        if (mSharedPreferences.getString(Constant.IS_USER_LOGIN, "").equals("true")) {
            MainActivity_.intent(this).start();
        }
        BusProvider.getInstance().register(this);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        fetchFacebookInfo(loginResult.getAccessToken(), LoginActivity.this);
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void fetchFacebookInfo(final AccessToken accessToken, final Context context) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    //check xem no da co chua
                    isCheckExistAccountFacebook(object.getString("name"), object.getString("email"));
                    if (!check) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("id", UUID.randomUUID().toString());
                        map.put("username", object.getString("name"));
                        map.put("password", "");
                        map.put("address", "");
                        map.put("email", object.getString("email"));
                        mFirebase.push().setValue(map);
                    }
                    //co roi thi k dang ky -> kiem tra setting -> qua main
                    mEditor.putString(Constant.NAME_USER_LOGIN, object.getString("name"));
                    mEditor.putString(Constant.IS_USER_LOGIN, "true");
                    mEditor.commit();
                    if (!mSharedPreferences.getString(Constant.SETTING_ADDRESS, "default").equals("") || !mSharedPreferences.getString(Constant.SETTING_JOB, "default").equals("")) {
                        MainActivity_.intent(LoginActivity.this).start();
                    } else {
                        showDialogSetting();
                    }

                } catch (JSONException e) {
                    Common.createDialog(LoginActivity.this, "Login Fail", "", false, mProgressDialogLoading);
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,gender,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Click(R.id.btnLogin)
    public void loginAction() {
        if (!"".equals(mEdtUsername.getText()) && !"".equals(mEdtPassword.getText())) {
            //check mang
            if (Network.checkNetWork(this, Constant.TYPE_NETWORK) || Network.checkNetWork(this, Constant.TYPE_WIFI)) {
                mValidator.validate();
            } else {
                Common.createDialog(this, "Vui lòng kiểm tra kết nối mạng", "", false, null);
            }
        } else {
            Common.createDialog(this, "Tên tài khoản hoặc mật khẩu không đúng", "", false, null);
        }
    }

    @Subscribe
    public void receiverStateOnNetwork(NetWorkState netWorkState) {
        if (netWorkState.getState().equals(Constant.WIFI_ENABLE) || netWorkState.getState().equals(Constant.MOBILE_DATA_ENABLE)) {
            mValidator.validate();
        }
    }

    public void doLogin() {
        isCheckAccountLogin();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (check) {
                    mEditor.putString(Constant.NAME_USER_LOGIN, mEdtUsername.getText().toString());
                    mEditor.putString(Constant.IS_USER_LOGIN, "true");
                    mEditor.putString(Constant.ID_USER_LOGIN, mUser.getId());
                    mEditor.commit();
                    if (!mSharedPreferences.getString(Constant.SETTING_ADDRESS, "").equals("") || !mSharedPreferences.getString(Constant.SETTING_JOB, "").equals("")) {
                        MainActivity_.intent(LoginActivity.this).start();
                    } else {
                        showDialogSetting();
                    }
                } else {
                    if (mStatusBlockUser == 0) {
                        Common.createDialog(LoginActivity.this, "Login Fail", "", false, mProgressDialogLoading);
                    }
                }
            }
        }, 3000);
    }

    public void isCheckAccountLogin() {
        check = false;

        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String userName = map.get("username").toString();
                String passWord = map.get("password").toString();
                String type = map.get("type").toString();
                if (!check) {
                    if (type.equals(Constant.USER_SYSTEM) && userName.equals(mEdtUsername.getText().toString()) && passWord.equals(mEdtPassword.getText().toString())) {
                        if (map.get("status").toString().equals(Constant.USER_ACTIVE)) {
                            check = true;
                            mUser = new User();
                            mUser.setUsername(mEdtUsername.getText().toString());
                            mUser.setId(map.get("id").toString());
                            return;
                        } else {
                            check = false;
                            mStatusBlockUser = 1;
                            Common.createDialog(LoginActivity.this, "Your account had been clock", "", false, mProgressDialogLoading);
                            return;
                        }
                    } else {
                        check = false;
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

    public void isCheckExistAccountFacebook(final String username, final String emailFb) {
        check = false;

        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String userName = map.get("username").toString();
                String email = map.get("email").toString();
                String type = map.get("type").toString();
                if (!check) {
                    if (type.equals(Constant.USER_FACEBOOK) && userName.equals(username) && email.equals(emailFb)) {
                        if (map.get("status").toString().equals(Constant.USER_ACTIVE)) {
                            check = true;
                            mUser = new User();
                            mUser.setUsername(username);
                            return;
                        } else {
                            check = false;
                            mStatusBlockUser = 1;
                            Common.createDialog(LoginActivity.this, "Tài khoản của bạn đã bị khóa", "", false, mProgressDialogLoading);
                            return;
                        }
                    } else {
                        check = false;
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

    @Override
    public void onValidationSucceeded() {
        mProgressDialogLoading = new ProgressDialog(this);
        mProgressDialogLoading.setMessage("Loading...");
        mProgressDialogLoading.show();
        doLogin();
    }

    @Click(R.id.tvLoginFaceBook)
    public void loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
    }

    public void showDialogSetting() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setting_job_and_address);
        final CheckBox chkFixElectronicInHouse = (CheckBox) dialog.findViewById(R.id.chkFixElectronicInHouse);
        final CheckBox chkCleanHouse = (CheckBox) dialog.findViewById(R.id.chkCleanHouse);
        final CheckBox chkDoLaundry = (CheckBox) dialog.findViewById(R.id.chkDoLaundry);
        final CheckBox chkFixWaterPipe = (CheckBox) dialog.findViewById(R.id.chkFixWaterPipe);
        final CheckBox chkPaintHouse = (CheckBox) dialog.findViewById(R.id.chkPaintHouse);

        final CheckBox chkHaiChau = (CheckBox) dialog.findViewById(R.id.chkHaiChau);
        final CheckBox chkCamLe = (CheckBox) dialog.findViewById(R.id.chkCamLe);
        final CheckBox chkLienChieu = (CheckBox) dialog.findViewById(R.id.chkLienChieu);
        final CheckBox chkThanhKhe = (CheckBox) dialog.findViewById(R.id.chkThanhKhe);
        final CheckBox chkSonTra = (CheckBox) dialog.findViewById(R.id.chkSonTra);
        final CheckBox chkNguHanhSon = (CheckBox) dialog.findViewById(R.id.chkNguHanhSon);
        final CheckBox chkHoaVang = (CheckBox) dialog.findViewById(R.id.chkHoaVang);

        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkFixElectronicInHouse.isChecked()) {
                    mSettingJob += Constant.TYPE_FIX_ELECTRONIC_IN_HOUSE + ";";
                }
                if (chkCleanHouse.isChecked()) {
                    mSettingJob += Constant.TYPE_CLEAN_HOUSE + ";";
                }
                if (chkDoLaundry.isChecked()) {
                    mSettingJob += Constant.TYPE_DO_LAUNDRY + ";";
                }
                if (chkFixWaterPipe.isChecked()) {
                    mSettingJob += Constant.TYPE_FIX_WATER_PIPE + ";";
                }
                if (chkPaintHouse.isChecked()) {
                    mSettingJob += Constant.TYPE_PAINT_HOUSE + ";";
                }

                if (chkHaiChau.isChecked()) {
                    mSettingAddress += Constant.TYPE_HAI_CHAU + ";";
                }
                if (chkCamLe.isChecked()) {
                    mSettingAddress += Constant.TYPE_CAM_LE + ";";
                }
                if (chkLienChieu.isChecked()) {
                    mSettingAddress += Constant.TYPE_LIEN_CHIEU + ";";
                }
                if (chkThanhKhe.isChecked()) {
                    mSettingAddress += Constant.TYPE_THANH_KHE + ";";
                }
                if (chkSonTra.isChecked()) {
                    mSettingAddress += Constant.TYPE_SON_TRA + ";";
                }
                if (chkNguHanhSon.isChecked()) {
                    mSettingAddress += Constant.TYPE_NGU_HANH_SON + ";";
                }
                if (chkHoaVang.isChecked()) {
                    mSettingAddress += Constant.TYPE_HOA_VANG + ";";
                }
                if (!"".equals(mSettingAddress) && !"".equals(mSettingJob)) {
                    //save local
                    mEditor.putString(Constant.SETTING_ADDRESS, mSettingAddress);
                    mEditor.putString(Constant.SETTING_JOB, mSettingJob);
                    mEditor.commit();
                    MainActivity_.intent(LoginActivity.this).start();
                } else {
                    Toast.makeText(LoginActivity.this, "Vui lòng chọn công việc và địa điểm", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    @Click(R.id.tvRegister)
    void RegisterAction() {
        RegisterActivity_.intent(this).start();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        String errorMessage = errors.get(0).getCollatedErrorMessage(this);
        if (errorMessage != null) {
            String[] messageErrors = errorMessage.split("\n");
            if (messageErrors.length > 0) {
                Common.createDialog(this, messageErrors[0], "", false, mProgressDialogLoading);
                mEdtUsername.setText("");
                mEdtPassword.setText("");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
