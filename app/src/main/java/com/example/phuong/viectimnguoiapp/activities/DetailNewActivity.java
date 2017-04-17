package com.example.phuong.viectimnguoiapp.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Helpers;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phuong on 22/02/2017.
 */
@EActivity(R.layout.activity_detail_new)
public class DetailNewActivity extends BaseActivity {
    @Extra
    protected NewItem mNewItem;
    @ViewById(R.id.tvDateNew)
    TextView mTvDate;
    @ViewById(R.id.tvTitletNewDetail)
    TextView mTvTitle;
    @ViewById(R.id.tvDetailNewDetail)
    TextView mTvDetail;
    @ViewById(R.id.btnContact)
    Button mBtnContact;
    @ViewById(R.id.tvUsernameNew)
    TextView mTvUserName;
    @ViewById(R.id.tvCoin)
    TextView mTvCoin;
    @ViewById(R.id.tvDeadlineNew)
    TextView mTvDeadline;
    @ViewById(R.id.tvAdrressNew)
    TextView mTvAddressNew;

    private User mUser = new User();
    private Firebase mFirebaseCat;
    private Firebase mFirebaseUserInfo;
    private Firebase mFirebasePing;
    private Firebase mFirebaseHistoryPingByUser;
    private SharedPreferences mSharedPreferencesLogin;
    private String mTitleCategory;
    private ProgressDialog mProgressDialogLoading;
    private String nameDistrictNew;
    private String nameDistrictUser;
    private boolean mCheckPing = false;

    @Override
    void inits() {
        Firebase.setAndroidContext(this);
        mSharedPreferencesLogin = getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        mProgressDialogLoading = new ProgressDialog(this);
        mProgressDialogLoading.setMessage("Loading...");
        mProgressDialogLoading.show();
        if (mNewItem != null) {
            getTitleNew(mNewItem.getIdCat());

            getDistrictNew(mNewItem.getIdDistrict());
            if (mNewItem.getIdUser() != "") {
                getUserInfor(mNewItem.getIdUser());
            } else {
                mTvCoin.setText("Đang cập nhật");
                mTvUserName.setText("Đang cập nhật");
            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTvTitle.setText(mTitleCategory);
                    mTvDetail.setText(mNewItem.getNote());
                    mTvDate.setText(mNewItem.getTimeCreated().toString());
                    mTvDeadline.setText(mNewItem.getTimeDeadline());
                    mTvAddressNew.setText(mNewItem.getAddress() + " " + nameDistrictNew);
                    mTvUserName.setText(mUser.getUsername());
                    mTvCoin.setText("0");
                    mProgressDialogLoading.dismiss();
                }
            }, 3000);


        } else {
            Log.d("tag11", "du lieu null");
        }
    }

    public void getUserInfor(final String idUser) {
        mFirebaseUserInfo = new Firebase("https://viectimnguoi-469e6.firebaseio.com/users");
        mFirebaseUserInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String id = map.get("id").toString();
                if (id.equals(idUser)) {
                    mUser.setUsername(map.get("username").toString());
                    mUser.setEmail(map.get("email").toString());
                    mUser.setPhone(map.get("phone").toString());
                    mUser.setAddress(map.get("address").toString());
                    mUser.setIdDistrict(Integer.parseInt(map.get("idDistrict").toString()));
                    getDistrictUser(mUser.getIdDistrict());
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

    public void getDistrictNew(final int idDistrict) {

        Firebase firebaseDistrict = new Firebase("https://viectimnguoi-469e6.firebaseio.com/districts/");

        firebaseDistrict.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                int id = Integer.parseInt(map.get("id").toString());
                if (id == idDistrict) {
                    nameDistrictNew = map.get("name").toString();
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

    @Click(R.id.imgGoToUserDetail)
    public void ProfileUser(){
        ProfileUserActivity_.intent(this).idUser(mNewItem.getIdUser()).start();
    }

    public void getDistrictUser(final int idDistrict) {

        Firebase firebaseDistrict = new Firebase("https://viectimnguoi-469e6.firebaseio.com/districts/");

        firebaseDistrict.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                int id = Integer.parseInt(map.get("id").toString());
                if (id == idDistrict) {
                    nameDistrictUser = map.get("name").toString();
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

    public void getTitleNew(final int idCat) {
        mFirebaseCat = new Firebase("https://viectimnguoi-469e6.firebaseio.com/categoryJobs");
        mFirebaseCat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String idCategory = map.get("id").toString();
                if (Integer.parseInt(idCategory) == idCat) {
                    mTitleCategory = map.get("name").toString();
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

    public void checkUserPing() {
        mFirebaseHistoryPingByUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                if (map.get("idPost").toString().equals(mNewItem.getId())) {
                    mCheckPing = true;
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

    @Click(R.id.btnContact)
    public void contactAction() {
        final String idUser = mSharedPreferencesLogin.getString(Constant.ID_USER_LOGIN, "");
        final String username = mSharedPreferencesLogin.getString(Constant.NAME_USER_LOGIN, "");

        mFirebaseHistoryPingByUser = new Firebase("https://viectimnguoi-469e6.firebaseio.com/historyPingByUser/" + idUser);
        // kiem tra da ping chưa, chưa thì cho ping
        checkUserPing();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mCheckPing) {
                    if (!idUser.equals(mNewItem.getIdUser())) {
                        //show dialog bao gia
                        final Dialog dialog = new Dialog(DetailNewActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_rate_price);
                        final EditText edtPrice = (EditText) dialog.findViewById(R.id.edtPrice);
                        edtPrice.requestFocus();
                        Helpers.showSoftKeyboard(DetailNewActivity.this, DetailNewActivity.this.getCurrentFocus());

                        TextView btnOk = (TextView) dialog.findViewById(R.id.tvBtnOk);

                        btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ("".equals(edtPrice.getText().toString())) {
                                    edtPrice.getError();
                                } else {
                                    if ("".equals(idUser)) {
                                        Common.createDialog(DetailNewActivity.this, "Hiện không có thông tin về người dùng nên không thực hiện được chức năng này");
                                    } else {
                                        mFirebasePing = new Firebase("https://viectimnguoi-469e6.firebaseio.com/pings/" + mNewItem.getIdUser() + "/" + mNewItem.getId() + "/" + idUser);
                                        String messageText = "Tài khoản " + username + " đăng ký làm việc ";
                                        Map<String, String> map = new HashMap<>();
                                        map.put("message", messageText);
                                        map.put("price", edtPrice.getText().toString() + "VNĐ");
                                        mFirebasePing.push().setValue(map);


                                        Map<String, String> mapHistory = new HashMap<>();
                                        mapHistory.put("idPost", mNewItem.getId());
                                        mapHistory.put("price", edtPrice.getText().toString() + "VNĐ");
                                        mFirebaseHistoryPingByUser.push().setValue(mapHistory);

                                        Toast.makeText(DetailNewActivity.this, "Bạn đã đặt chỗ thành công", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                dialog.dismiss();

                            }
                        });
                        dialog.show();
                    } else {
                        Common.createDialog(DetailNewActivity.this, "Bạn không thực hiện được chức năng này");
                    }
                } else {
                    Common.createDialog(DetailNewActivity.this, "Bạn đã đặt chỗ bài viết này.");
                }
            }
        }, 1000);
    }

}
