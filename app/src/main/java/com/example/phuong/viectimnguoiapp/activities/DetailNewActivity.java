package com.example.phuong.viectimnguoiapp.activities;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.firebase.client.Firebase;

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
    @ViewById(R.id.imgClose)
    ImageView mImgClose;

    Firebase mFirebase1, mFirebase2;
    SharedPreferences mSharedPreferencesLogin;

    @Override
    void inits() {
        Firebase.setAndroidContext(this);
        mSharedPreferencesLogin = getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        if (mNewItem != null) {
//            mTvDate.setText(mNewItem.getDate().toString());
//            mTvTitle.setText(mNewItem.getTitle());
//            mTvDetail.setText(mNewItem.getDetail());
        } else {
            Log.d("tag11", "du lieu null");
        }
    }

    @Click(R.id.btnContact)
    public void contactAction() {
        String idUser = mSharedPreferencesLogin.getString(Constant.ID_USER_LOGIN, "");
        String username = mSharedPreferencesLogin.getString(Constant.NAME_USER_LOGIN, "");
        idUser = "2";
        username = "triha";
        if ("".equals(idUser)) {
            //show dialog
        } else {
            mFirebase1 = new Firebase("https://viectimnguoi-469e6.firebaseio.com/messages/" + idUser + "_" + mNewItem.getIdUser());
            mFirebase2 = new Firebase("https://viectimnguoi-469e6.firebaseio.com/messages/" + mNewItem.getIdUser() + "_" + idUser);
            //String messageText = username + " đăng ký làm việc " + mNewItem.getTitle() + ". Bài được đăng lúc " + mNewItem.getDate();
            Map<String, String> map = new HashMap<>();
            //map.put("message", messageText);
            map.put("user", idUser);
            mFirebase1.push().setValue(map);
            mFirebase2.push().setValue(map);
            Toast.makeText(this, "Bạn đã đặt chỗ thành công", Toast.LENGTH_SHORT).show();
        }
    }

    @Click(R.id.imgClose)
    public void closeAction() {
        finish();
    }
}
