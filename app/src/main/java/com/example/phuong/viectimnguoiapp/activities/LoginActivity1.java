package com.example.phuong.viectimnguoiapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.DialogLoading;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.example.phuong.viectimnguoiapp.utils.SharedPreferencesUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright@ AsianTech.Inc
 * Created by ly.ho on 21/02/2017.
 */
@EActivity(R.layout.activity_login_2)
public class LoginActivity1 extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, FirebaseAuth.AuthStateListener {
    private static final String[] PERMISSION_FB = {"email", "public_profile", "user_posts"};
    private static final int RC_SIGN_IN = 1;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFireBaseAuth;
    private RealmHelper mData;

    @AfterViews
    void init() {
        mFireBaseAuth = FirebaseAuth.getInstance();

        if (mFireBaseAuth.getCurrentUser() != null) {
            //luu share
            SharedPreferencesUtils.getInstance().setUserLogin(getApplicationContext());

            MainActivity_.intent(LoginActivity1.this).start();
            finish();
        } else {
            mFireBaseAuth.addAuthStateListener(this);
            configLoginGoogle();
            configLoginFacebook();
        }
    }

    public void getSetting() {
        DatabaseReference mFirebaseSetting = FirebaseDatabase.getInstance().getReference("/setting");
        mFirebaseSetting.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
                if (map != null) {
                    SharedPreferencesUtils.getInstance().setSetting(LoginActivity1.this, map.get("jobSetting").toString(), map.get("addressSetting").toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserInfor() {
        mData = new RealmHelper(this);
        final DatabaseReference mFirebaseUserInfor = FirebaseDatabase.getInstance().getReference("/users");
        mFirebaseUserInfor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean check = false;
                User mUser = new User();
                mUser.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                mUser.setUsername(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                mUser.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> map = (HashMap<String, Object>) data.getValue();
                    String id = map.get("id").toString();
                    if (id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        mUser.setPhone(map.get("phone").toString());
                        mUser.setAddress(map.get("address").toString());
                        mUser.setIdDistrict(Integer.parseInt(map.get("idDistrict").toString()));
                        mUser.setPoint(map.get("point").toString());
                        mUser.setStatus(map.get("status").toString());
                        mData.addUser(mUser);
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    Map<String, String> mapUser = new HashMap<String, String>();
                    mapUser.put("address", "");
                    mapUser.put("idDistrict", "");
                    mapUser.put("phone", "");
                    mapUser.put("status", Constant.USER_ACTIVE);
                    mapUser.put("point", "0");
                    mFirebaseUserInfor.push().setValue(mapUser);
                    mUser.setPhone("");
                    mUser.setAddress("");
                    mUser.setIdDistrict(0);
                    mUser.setPoint("");
                    mUser.setStatus("");
                    mData.addUser(mUser);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void configLoginGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void configLoginFacebook() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                fireBaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // No-op
            }

            @Override
            public void onError(FacebookException error) {
                // No-op
            }
        });
    }


    @Click(R.id.btnLoginGg)
    void onClickLoginWithGoogle() {
        if (!(Network.checkNetWork(this))) {
            Toast.makeText(this, R.string.toast_text_connection_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Click(R.id.btnLoginFb)
    void onClickLoginWithFacebook() {
        if (!(Network.checkNetWork(this))) {
            Toast.makeText(this, R.string.toast_text_connection_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(PERMISSION_FB));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                fireBaseAuthWithGoogle(account);
            }
        }
    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount account) {
        DialogLoading.getInstance().showProgressDialog(this, getString(R.string.dialog_messenger_connect));
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFireBaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DialogLoading.getInstance().dismissProgressDialog();
                    }
                });
    }

    private void fireBaseAuthWithFacebook(AccessToken accessToken) {
        DialogLoading.getInstance().showProgressDialog(this, getString(R.string.dialog_messenger_connect));
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFireBaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        DialogLoading.getInstance().dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            requestLogOutGoogle();
            MainActivity_.intent(LoginActivity1.this).start();
            getUserInfor();
            getSetting();
            finish();
        } else {
        }
    }

    private void requestLogOutGoogle() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFireBaseAuth != null) {
            mFireBaseAuth.removeAuthStateListener(this);
        }
    }
}
