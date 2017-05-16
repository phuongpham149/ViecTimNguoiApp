package com.example.phuong.viectimnguoiapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.eventBus.BusProvider;
import com.example.phuong.viectimnguoiapp.eventBus.object.NetWorkState;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Helpers;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    TextView mTvLoginFacebook;
    @ViewById(R.id.btnLogin)
    Button mBtnLogin;
    @ViewById(R.id.tvRegister)
    TextView mTvRegister;
    @ViewById(R.id.pbLoading)
    ProgressBar mPbLoading;

    private Validator mValidator;
    private DatabaseReference mFirebase;
    private boolean check;
    private User mUser;
    private int mStatusBlockUser = 0;
    private RealmHelper mData;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private CallbackManager callbackManager;

    @Override
    void inits() {
        Helpers.hideSoftKeyboard(this, this.getCurrentFocus());
        if (!"".equals(username) && !"".equals(password)) {
            mEdtUsername.setText(username);
            mEdtPassword.setText(password);
        }
        mFirebase = FirebaseDatabase.getInstance().getReference("/users");
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mSharedPreferences = getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        if (mSharedPreferences.getString(Constant.IS_USER_LOGIN, "").equals("true")) {
            MainActivity_.intent(this).mIsSetting(true).start();
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
                    MainActivity_.intent(LoginActivity.this).start();

                } catch (JSONException e) {
                    Common.createDialog(LoginActivity.this, "Đăng nhập thất bại");
                    mPbLoading.setVisibility(View.GONE);
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
            if (Network.checkNetWork(this)) {
                mValidator.validate();
            } else {
                Common.createDialog(this, "Vui lòng kiểm tra kết nối mạng");
                mPbLoading.setVisibility(View.GONE);
            }
        } else {
            Common.createDialog(this, "Tên tài khoản hoặc mật khẩu không đúng");
            mPbLoading.setVisibility(View.GONE);
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
                    MainActivity_.intent(LoginActivity.this).start();
                } else {
                    if (mStatusBlockUser == 0) {
                        Common.createDialog(LoginActivity.this, "Login Fail");
                        mPbLoading.setVisibility(View.GONE);
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
                    if (type.equals(Constant.USER_SYSTEM) && userName.equals(mEdtUsername.getText().toString()) && passWord.equals(Helpers.sha256(mEdtPassword.getText().toString()))) {
                        if (map.get("status").toString().equals(Constant.USER_ACTIVE)) {
                            check = true;
                            mUser = new User();
                            mUser.setUsername(mEdtUsername.getText().toString());
                            mUser.setId(map.get("id").toString());
                            getUserInfor(mUser.getId());
                            return;
                        } else {
                            check = false;
                            mStatusBlockUser = 1;
                            Common.createDialog(LoginActivity.this, "Tài khoản của bạn đã bị khóa");
                            mPbLoading.setVisibility(View.GONE);
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserInfor(final String idUser) {
        mData = new RealmHelper(this);
        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String id = map.get("id").toString();
                if (id.equals(idUser)) {
                    mUser.setId(idUser);
                    mUser.setUsername(map.get("username").toString());
                    mUser.setEmail(map.get("email").toString());
                    mUser.setPhone(map.get("phone").toString());
                    mUser.setAddress(map.get("address").toString());
                    mUser.setIdDistrict(Integer.parseInt(map.get("idDistrict").toString()));
                    mUser.setPoint(map.get("point").toString());
                    mUser.setType(map.get("type").toString());
                    mUser.setStatus(map.get("status").toString());
                    mData.addUser(mUser);
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
            public void onCancelled(DatabaseError databaseError) {

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
                            Common.createDialog(LoginActivity.this, "Tài khoản của bạn đã bị khóa");
                            mPbLoading.setVisibility(View.GONE);
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        mPbLoading.setVisibility(View.VISIBLE);
        doLogin();
    }

    @Click(R.id.tvLoginFaceBook)
    public void loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
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
                Common.createDialog(this, messageErrors[0]);
                mEdtPassword.setText("");
                mPbLoading.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
