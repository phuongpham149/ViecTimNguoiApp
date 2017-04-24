package com.example.phuong.viectimnguoiapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.objects.User;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Helpers;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.example.phuong.viectimnguoiapp.utils.TrackGPS;
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
    protected String mId = "";

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
    @ViewById(R.id.toolBarDetail)
    Toolbar mToolbarDetail;

    private User mUser = new User();
    private Firebase mFirebaseUserInfo;
    private Firebase mFirebasePing;
    private Firebase mFirebaseHistoryPingByUser;

    private SharedPreferences mSharedPreferencesLogin;

    private boolean mCheckPing = false;
    private RealmHelper mData;
    private NewItem mNew;

    private TextView mTvTitleToolbar;
    private ProgressBar mProgressBarLoading;
    private TrackGPS mTrackGPS;

    @Click(R.id.imgGoToMapAndroid)
    public void goToMapAction() {
        if (Network.checkNetWork(this, Constant.TYPE_NETWORK) || Network.checkNetWork(this, Constant.TYPE_WIFI)) {
            mTrackGPS = new TrackGPS(this);
            if (mTrackGPS.canGetLocation()) {
                if (Common.isGoogleMapsInstalled(this)) {
                    String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + mTrackGPS.getLatitude() + "," + mTrackGPS.getLongitude() + "&daddr=" + Common.getRoomLocation(mNew.getAddress() + " " + mData.getDistrictItem(mNew.getIdDistrict()).first().getName(), this).latitude + "," + Common.getRoomLocation(mNew.getAddress() + " " + mData.getDistrictItem(mNew.getIdDistrict()).first().getName(), this).longitude;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage(getString(R.string.package_map));
                    startActivity(intent);
                } else {
                    String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + mTrackGPS.getLatitude() + "," + mTrackGPS.getLongitude() + "&daddr=" + Common.getRoomLocation(mNew.getAddress() + " " + mData.getDistrictItem(mNew.getIdDistrict()).first().getName(), this).latitude + "," + Common.getRoomLocation(mNew.getAddress() + " " + mData.getDistrictItem(mNew.getIdDistrict()).first().getName(), this).longitude;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
            } else {
                mTrackGPS.showSettingsAlert();
            }
        } else {
            Common.createDialog(DetailNewActivity.this, "Vui lòng kiểm tra kết nối mạng");
        }

    }

    @Click(R.id.btnBack)
    public void backAction() {
        finish();
    }

    @Override
    void inits() {
        mData = new RealmHelper(DetailNewActivity.this);
        mNew = mData.getItemNew(mId).first();
        Firebase.setAndroidContext(this);

        mTvTitleToolbar = (TextView) mToolbarDetail.findViewById(R.id.tvtitleToolbar);
        mProgressBarLoading = (ProgressBar) mToolbarDetail.findViewById(R.id.prograssBarLoading);
        mTvTitleToolbar.setText("Chi tiết công việc");
        mProgressBarLoading.setVisibility(View.VISIBLE);

        mSharedPreferencesLogin = getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);

        if (mNew != null) {
            if (mNew.getIdUser() != "") {
                getUserInfor(mNew.getIdUser());
            } else {
                mTvCoin.setText("Đang cập nhật");
                mTvUserName.setText("Đang cập nhật");
            }
            mTvTitle.setText(mData.getCategoryJobItem(mNew.getIdCat()).first().getName());
            mTvDetail.setText(mNew.getNote());
            mTvDate.setText(mNew.getTimeCreated());
            mTvDeadline.setText(mNew.getTimeDeadline());
            mTvAddressNew.setText(mNew.getAddress() + " " + mData.getDistrictItem(mNew.getIdDistrict()).first().getName());

            if (Network.checkNetWork(this, Constant.TYPE_NETWORK) || Network.checkNetWork(this, Constant.TYPE_WIFI)) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTvUserName.setText(mUser.getUsername());
                        mTvCoin.setText(mUser.getPoint());
                        mProgressBarLoading.setVisibility(View.GONE);
                    }
                }, 1000);
            } else {
                mTvUserName.setText("Đang cập nhật");
                mTvCoin.setText("Đang cập nhật");
                mProgressBarLoading.setVisibility(View.GONE);
            }
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
                    mUser.setPoint(map.get("point").toString());
                    mUser.setType(map.get("type").toString());
                    mUser.setStatus(map.get("status").toString());
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
    public void ProfileUser() {
        ProfileUserActivity_.intent(this).idUser(mNew.getIdUser()).start();
    }

    public void checkUserPing() {
        mFirebaseHistoryPingByUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                if (map.get("idPost").toString().equals(mNew.getId())) {
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
        if (Network.checkNetWork(this, Constant.TYPE_NETWORK) || Network.checkNetWork(this, Constant.TYPE_WIFI)) {
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
                        if (!idUser.equals(mNew.getIdUser())) {
                            //show dialog bao gia
                            showDialogRatePrice(idUser, username);
                        } else {
                            Common.createDialog(DetailNewActivity.this, "Bạn không thực hiện được chức năng này");
                        }
                    } else {
                        Common.createDialog(DetailNewActivity.this, "Bạn đã đặt chỗ bài viết này.");
                    }
                }
            }, 1000);
        } else {
            Common.createDialog(DetailNewActivity.this, "Vui lòng kiểm tra kết nối mạng");
        }
    }

    public void showDialogRatePrice(final String idUser, final String username) {
        final Dialog dialog = new Dialog(DetailNewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rate_price);
        final EditText edtPrice = (EditText) dialog.findViewById(R.id.edtPrice);
        edtPrice.requestFocus();
        Helpers.showSoftKeyboard(DetailNewActivity.this, DetailNewActivity.this.getCurrentFocus());

        Button btnOk = (Button) dialog.findViewById(R.id.tvBtnOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(edtPrice.getText().toString())) {
                    edtPrice.getError();
                } else {
                    if ("".equals(idUser)) {
                        Common.createDialog(DetailNewActivity.this, "Hiện không có thông tin về người dùng nên không thực hiện được chức năng này");
                    } else {
                        mFirebasePing = new Firebase("https://viectimnguoi-469e6.firebaseio.com/pings/" + mNew.getIdUser() + "/" + mNew.getId() + "/" + idUser);
                        String messageText = "Tài khoản " + username + " đăng ký làm việc ";
                        Map<String, String> map = new HashMap<>();
                        map.put("message", messageText);
                        map.put("price", edtPrice.getText().toString() + "VNĐ");
                        mFirebasePing.push().setValue(map);


                        Map<String, String> mapHistory = new HashMap<>();
                        mapHistory.put("idPost", mNew.getId());
                        mapHistory.put("price", edtPrice.getText().toString() + "VNĐ");
                        mFirebaseHistoryPingByUser.push().setValue(mapHistory);

                        Toast.makeText(DetailNewActivity.this, "Bạn đã đặt chỗ thành công", Toast.LENGTH_SHORT).show();
                    }

                }
                dialog.dismiss();

            }
        });
        dialog.show();
    }

}
