package com.example.phuong.viectimnguoiapp.activities;

import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.objects.HistoryPing;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

/**
 * Created by asiantech on 28/03/2017.
 */
@EActivity(R.layout.activity_history_ping_detail)
public class HistoryPingDetailActivity extends BaseActivity {
    @Extra
    HistoryPing mHistoryPing;

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

    @Override
    void inits() {
        mTvTitlePost.setText(mHistoryPing.getTitlePost());
        mTvAddressPost.setText(mHistoryPing.getAddress());
        mTvNotePost.setText(mHistoryPing.getNote());
        mTvUserPost.setText(mHistoryPing.getUserOwner());
        mTvPrice.setText(mHistoryPing.getPrice());
    }
}
