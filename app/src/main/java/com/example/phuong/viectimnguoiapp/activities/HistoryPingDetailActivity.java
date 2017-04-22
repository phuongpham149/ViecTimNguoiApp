package com.example.phuong.viectimnguoiapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.objects.HistoryPing;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.example.phuong.viectimnguoiapp.utils.TrackGPS;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by asiantech on 28/03/2017.
 */
@EActivity(R.layout.activity_history_ping_detail)
public class HistoryPingDetailActivity extends BaseActivity {
    @Extra
    String mIdPost;

    @ViewById(R.id.tvTitlePost)
    TextView mTvTitlePost;

    @ViewById(R.id.tvUserPost)
    TextView mTvUserPost;

    @ViewById(R.id.tvAddressPost)
    TextView mTvAddressPost;

    @ViewById(R.id.tvNotePost)
    TextView mTvNotePost;

    @ViewById(R.id.tvPrice)
    TextView mTvPrice;

    @ViewById(R.id.tvDateNew)
    protected TextView mTvTimeCreated;

    @ViewById(R.id.tvDeadlineNew)
    protected TextView mtvTimeDeadline;

    @ViewById(R.id.toolBarDetailPing)
    protected Toolbar mToolbalDetailPing;

    private TextView mTvTitleToolbar;
    private TrackGPS mTrackGPS;
    private HistoryPing mHistoryPing;
    private RealmHelper mData;

    @Click(R.id.btnBack)
    public void backAction() {
        finish();
    }

    @Click(R.id.imgGoToUserDetail)
    public void ProfileUser() {
        ProfileUserActivity_.intent(this).idUser(mHistoryPing.getIdUser()).start();
    }

    @Override
    void inits() {
        mTvTitleToolbar = (TextView) mToolbalDetailPing.findViewById(R.id.tvtitleToolbar);
        mTvTitleToolbar.setText("Thông tin bài đăng");
        if (mIdPost != null) {
            mData = new RealmHelper(this);
            mHistoryPing = mData.getHistoryPing(mIdPost).first();
            mTvTitlePost.setText(mHistoryPing.getTitlePost());
            mTvAddressPost.setText(mHistoryPing.getAddress() + ", " + mHistoryPing.getNameDistrict());
            mTvNotePost.setText(mHistoryPing.getNote());
            mTvUserPost.setText(mHistoryPing.getUserOwner());
            mTvPrice.setText(mHistoryPing.getPrice());
            mtvTimeDeadline.setText(mHistoryPing.getTimeDeadline());
        }

    }

    @Click(R.id.imgGoToMapAndroid)
    public void goToMapAction() {
        if (Network.checkNetWork(this, Constant.TYPE_NETWORK) || Network.checkNetWork(this, Constant.TYPE_WIFI)) {
            mTrackGPS = new TrackGPS(this);
            if (mTrackGPS.canGetLocation()) {
                if (Common.isGoogleMapsInstalled(this)) {
                    String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + mTrackGPS.getLatitude() + "," + mTrackGPS.getLongitude() + "&daddr=" + Common.getRoomLocation(mHistoryPing.getAddress() + " " + mHistoryPing.getNameDistrict(), this).latitude + "," + Common.getRoomLocation(mHistoryPing.getAddress() + " " + mHistoryPing.getNameDistrict(), this).longitude;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage(getString(R.string.package_map));
                    startActivity(intent);
                } else {
                    String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + mTrackGPS.getLatitude() + "," + mTrackGPS.getLongitude() + "&daddr=" + Common.getRoomLocation(mHistoryPing.getAddress() + " " + mHistoryPing.getNameDistrict(), this).latitude + "," + Common.getRoomLocation(mHistoryPing.getAddress() + " " + mHistoryPing.getNameDistrict(), this).longitude;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
            } else {
                mTrackGPS.showSettingsAlert();
            }
        } else {
            Common.createDialog(HistoryPingDetailActivity.this, "Vui lòng kiểm tra kết nối mạng");
        }
    }
}
