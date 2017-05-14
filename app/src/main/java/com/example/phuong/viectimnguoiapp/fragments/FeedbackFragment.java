package com.example.phuong.viectimnguoiapp.fragments;

import android.content.SharedPreferences;
import android.widget.EditText;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.utils.Common;
import com.example.phuong.viectimnguoiapp.utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by asiantech on 22/04/2017.
 */
@EFragment(R.layout.fragment_send_feedback)
public class FeedbackFragment extends BaseFragment {

    @ViewById(R.id.edtFeedback)
    EditText mEdtFeekback;

    private DatabaseReference mFirebaseFeedback;
    private SharedPreferences mSharedPreferencesUserLogin;

    @Click(R.id.btnFeedback)
    public void sendFeedbackAction() {
        if (mEdtFeekback.getText().toString().equals("")) {
            Common.createDialog(getActivity(), "Vui lòng điền nội dung phản hồi");
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("feedback", mEdtFeekback.getText().toString());
            mFirebaseFeedback.push().setValue(map);
            Common.createDialog(getActivity(), "Cảm ơn bạn đã phản hồi với chúng tôi");
        }

    }

    @Override
    void inits() {
        mSharedPreferencesUserLogin = getActivity().getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        mFirebaseFeedback = FirebaseDatabase.getInstance().getReference("/feedback/" + mSharedPreferencesUserLogin.getString(Constant.ID_USER_LOGIN, ""));
    }
}
