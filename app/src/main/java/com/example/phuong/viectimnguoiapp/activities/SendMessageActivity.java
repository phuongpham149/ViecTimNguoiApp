package com.example.phuong.viectimnguoiapp.activities;

import android.content.SharedPreferences;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.utils.Constant;
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
 * Created by phuong on 23/02/2017.
 */
@EActivity(R.layout.activity_send_message)
public class SendMessageActivity extends BaseActivity {
    @Extra
    protected String idUserContact;
    @Extra
    protected String mNameUserContact;

    @ViewById(R.id.scrollView)
    ScrollView mScrollView;
    @ViewById(R.id.layoutContact)
    LinearLayout mLlContact;
    @ViewById(R.id.imgSend)
    ImageView mImgSend;
    @ViewById(R.id.edtMessageArea)
    EditText mEdtMessageArea;
    @ViewById(R.id.toolBarChat)
    Toolbar mToolbarChat;

    private TextView mTvTitle;
    private Firebase mFirebaseUser;
    private Firebase mFirebaseFriend;
    private SharedPreferences mSharedPreferences;
    private String idUser;

    @Click(R.id.btnBack)
    public void backAction() {
        finish();
    }

    @Override
    void inits() {
        mTvTitle = (TextView) mToolbarChat.findViewById(R.id.tvtitleToolbar);
        mTvTitle.setText(mNameUserContact);

        mSharedPreferences = getSharedPreferences(Constant.DATA_NAME_USER_LOGIN, 0);
        idUser = mSharedPreferences.getString(Constant.ID_USER_LOGIN, "");

        mFirebaseUser = new Firebase("https://viectimnguoi-469e6.firebaseio.com/messages/" + idUser + "_" + idUserContact);
        mFirebaseFriend = new Firebase("https://viectimnguoi-469e6.firebaseio.com/messages/" + idUserContact + "_" + idUser);

        mImgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = mEdtMessageArea.getText().toString();
                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", idUser);
                    mFirebaseUser.push().setValue(map);
                    mFirebaseFriend.push().setValue(map);
                    mEdtMessageArea.setText("");
                }
            }
        });

        mFirebaseUser.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(idUser)) {
                    addMessageBox(message, 1);
                } else {
                    addMessageBox(message, 2);
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

    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(SendMessageActivity.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        if (type == 1) {
            lp.setMargins(10, 0, 0, 10);
            lp.gravity = Gravity.RIGHT;
            textView.setLayoutParams(lp);
            textView.setTextSize(18);
            textView.setPadding(5, 10, 5, 10);
            textView.setTextColor(getResources().getColor(R.color.bg_default));
            textView.setBackgroundResource(R.drawable.rounded_corner1);

        } else {
            lp.setMargins(10, 0, 0, 10);
            textView.setTextSize(18);
            textView.setLayoutParams(lp);
            textView.setBackgroundResource(R.drawable.rounded_corner2);
        }

        mLlContact.addView(textView);
        mScrollView.fullScroll(View.FOCUS_DOWN);
    }
}
