package com.example.phuong.viectimnguoiapp.activities;

import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Network;
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

    @ViewById(R.id.tvPhone)
    TextView mTvPhone;

    @ViewById(R.id.btnSendMessage)
    Button mBtnSendMessage;

    @ViewById(R.id.toolBarDetail)
    Toolbar mToolbarUser;

    private TextView mTvTitleToolbar;
    private ProgressBar mProgressBarLoading;

    private User mUser;
    private Firebase mFirebaseUserInfor;
    private RealmHelper mData;
    private String mNameDistrict;

    @Click(R.id.btnBack)
    public void BackAction() {
        finish();
    }

    @Override
    void inits() {
        mBtnSendMessage.setVisibility(View.GONE);
        mData = new RealmHelper(this);
        mTvTitleToolbar = (TextView) mToolbarUser.findViewById(R.id.tvtitleToolbar);
        mProgressBarLoading = (ProgressBar) mToolbarUser.findViewById(R.id.prograssBarLoading);
        mTvTitleToolbar.setText("Thông tin người đăng bài");
        mProgressBarLoading.setVisibility(View.VISIBLE);

        if (Network.checkNetWork(this, Constant.TYPE_NETWORK) || Network.checkNetWork(this, Constant.TYPE_WIFI)) {
            getUserInfor();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTvNameUser.setText(mUser.getUsername());
                    mTvContact.setText(mUser.getEmail().equals("") ? "Đang cập nhật" : mUser.getEmail());
                    mTvAddress.setText(mUser.getAddress().equals("") ? "Đang cập nhật" : mUser.getAddress() + " " + mNameDistrict);
                    mTvPoint.setText(mUser.getAddress().equals("") ? "0" : mUser.getPoint());
                    mTvPhone.setText(mUser.getPhone());
                    if (!mUser.getId().equals("")) {
                        mBtnSendMessage.setVisibility(View.VISIBLE);
                    }
                    mProgressBarLoading.setVisibility(View.GONE);
                }
            }, 2000);
        } else {
            Common.createDialog(this, "Vui lòng kiểm tra kết nối mạng");
            mTvNameUser.setText("Đang cập nhật");
            mTvContact.setText("Đang cập nhật");
            mTvAddress.setText("Đang cập nhật");
            mTvPoint.setText("Đang cập nhật");
            mTvPhone.setText("Đang cập nhật");
            mProgressBarLoading.setVisibility(View.GONE);
        }
    }

    @Click(R.id.btnSendMessage)
    public void sendMessage() {
        SendMessageActivity_.intent(this).idUserContact(mUser.getId()).mNameUserContact(mUser.getUsername()).start();
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
                    mUser.setPhone(map.get("phone").toString());
                    mNameDistrict = mData.getDistrictItem(map.get("idDistrict").toString()).first().getName();
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
