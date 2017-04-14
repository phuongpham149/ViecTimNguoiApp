package com.example.phuong.viectimnguoiapp.activities;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Map;

/**
 * Created by asiantech on 25/03/2017.
 */
@EActivity(R.layout.activity_profile_user)
public class ProfileUserActivity extends BaseActivity {

    @Extra
    String idUser;

    @ViewById(R.id.tvNameUser)
    TextView mTvNameUser;

    @ViewById(R.id.tvEmail)
    TextView mTvContact;

    @ViewById(R.id.tvAddress)
    TextView mTvAddress;

    @ViewById(R.id.tvPoint)
    TextView mTvPoint;

    @ViewById(R.id.btnSendMessage)
    Button mBtnSendMessage;

    private User mUser;
    private Firebase mFirebaseUserInfor;
    private Firebase mFirebaseAddress;
    private String mNameDistrict;

    @Override
    void inits() {
        mBtnSendMessage.setVisibility(View.GONE);
        getUserInfor();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvNameUser.setText(mUser.getUsername());
                mTvContact.setText(mUser.getEmail().equals("") ? "Đang cập nhật" : mUser.getEmail());
                mTvAddress.setText(mUser.getAddress().equals("") ? "Đang cập nhật" : mUser.getAddress()+" "+mNameDistrict);
                mTvPoint.setText(mUser.getAddress().equals("") ? "0" : mUser.getPoint());
                if(!mUser.getId().equals("")){
                    mBtnSendMessage.setVisibility(View.VISIBLE);
                }
            }
        }, 2000);
    }

    @Click(R.id.btnSendMessage)
    public void sendMessage(){
        SendMessageActivity_.intent(this).idUserContact(mUser.getId()).start();
    }

    public void getUserInfor() {
        mFirebaseUserInfor = new Firebase("https://viectimnguoi-469e6.firebaseio.com/users");
        mFirebaseUserInfor.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                if (idUser.equals(map.get("id").toString())) {
                    mUser = new User();
                    mUser.setId(map.get("id").toString());
                    mUser.setEmail(map.get("email").toString());
                    mUser.setAddress(map.get("address").toString());
                    mUser.setUsername(map.get("username").toString());
                    mUser.setPoint(map.get("point").toString());
                    getDistrict(map.get("idDistrict").toString());
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

    public void getDistrict(final String idDistrict) {
        mFirebaseAddress = new Firebase("https://viectimnguoi-469e6.firebaseio.com/districts");
        mFirebaseAddress.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                if (map.get("id").toString().equals(idDistrict)) {
                    mNameDistrict = map.get("name").toString();
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
}
