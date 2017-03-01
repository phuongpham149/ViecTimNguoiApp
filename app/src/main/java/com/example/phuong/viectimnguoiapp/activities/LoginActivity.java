package com.example.phuong.viectimnguoiapp.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
import java.util.List;
import java.util.Map;

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
    private ProgressDialog mProgressDialogLoading;
    private Validator mValidator;
    private Firebase mFirebase;
    private boolean check;
    private User mUser;
    private int mStatusBlockUser = 0;
    private SharedPreferences mSharedPreferences;
    private CallbackManager callbackManager;
    private String mRoleUserFaceBook;

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
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                try {
                    editor.putString(Constant.NAME_USER_LOGIN, object.getString("name"));
                    editor.putString(Constant.ROLE_USER_LOGIN, mRoleUserFaceBook);
                    editor.commit();
                    MainActivity_.intent(LoginActivity.this).start();
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
                Common.createDialog(this, "Please check your network", "", false, null);
            }
        } else {
            Common.createDialog(this, "Please check your username or password", "", false, null);
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
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(Constant.NAME_USER_LOGIN, mUser.getUsername());
                    editor.putString(Constant.ROLE_USER_LOGIN, mUser.getRole());
                    editor.commit();
                    MainActivity_.intent(LoginActivity.this).start();
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
                if (!check) {
                    if (userName.equals(mEdtUsername.getText().toString()) && passWord.equals(mEdtPassword.getText().toString())) {
                        if (map.get("status").toString().equals(Constant.USER_ACTIVE)) {
                            check = true;
                            mUser = new User();
                            mUser.setUsername(username);
                            mUser.setRole(map.get("role").toString());
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

    @Click(R.id.imgBack)
    public void BackAction() {
        finish();
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
        showDialogChoiceRole();
    }

    public void showDialogChoiceRole() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_choice_role);
        final RadioButton rdWork = (RadioButton) dialog.findViewById(R.id.radioWork);
        RadioButton rdHide = (RadioButton) dialog.findViewById(R.id.radioHide);
        Button btnOk = (Button) dialog.findViewById(R.id.tvBtnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.tvBtnCancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rdWork.isChecked()) {
                    mRoleUserFaceBook = Constant.USER_WORK;
                } else {
                    mRoleUserFaceBook = Constant.USER_HIDE;
                }

                dialog.dismiss();
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "user_friends"));
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
