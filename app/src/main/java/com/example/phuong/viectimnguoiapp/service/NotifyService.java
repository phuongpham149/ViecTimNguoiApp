package com.example.phuong.viectimnguoiapp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.example.phuong.viectimnguoiapp.R;
import com.example.phuong.viectimnguoiapp.activities.MainActivity;
import com.example.phuong.viectimnguoiapp.databases.RealmHelper;
import com.example.phuong.viectimnguoiapp.fragments.ListUserPingByNew;
import com.example.phuong.viectimnguoiapp.fragments.ListUserPingByNew_;
import com.example.phuong.viectimnguoiapp.objects.CategoryJob;
import com.example.phuong.viectimnguoiapp.objects.District;
import com.example.phuong.viectimnguoiapp.objects.NewItem;
import com.example.phuong.viectimnguoiapp.utils.Network;
import com.example.phuong.viectimnguoiapp.utils.SharedPreferencesUtils;
import com.example.phuong.viectimnguoiapp.utils.SubscribeSettingHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NotifyService extends Service implements ChildEventListener {
    private DatabaseReference postsRef;
    private volatile boolean waitForClear = true;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        postsRef = FirebaseDatabase.getInstance().getReference("/posts");
        waitForClear = true;
        postsRef.addChildEventListener(this);
        wait3000();
        return super.onStartCommand(intent, flags, startId);
    }

    private void wait3000() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                waitForClear = false;
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        postsRef.removeEventListener(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        if (waitForClear)
            return;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null)
            return;

        HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();
        String categoryId = map.get("idCat").toString();
        String districtId = map.get("idDistrict").toString();
        String userId = map.get("idUser").toString();


        if (currentUser.getUid().equals(userId))
            return;

        if (SubscribeSettingHelper.getInstance().isSuitable(categoryId, districtId)) {
            CategoryJob categoryJob = SubscribeSettingHelper.getInstance().getCategoryById(categoryId);
            District district = SubscribeSettingHelper.getInstance().getDistrictById(districtId);

            String message = "";
            if (categoryJob != null)
                message = categoryJob.getName();
            if (district != null)
                message += " tại " + district.getName();

            prepareData(map);
            sendNotification(message);
        }
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Công việc mới")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void prepareData(Map<String, Object> data) {
        final RealmHelper mData = new RealmHelper(getApplicationContext());
        NewItem newItem = new NewItem();
        newItem.setId(data.get("id").toString());
        newItem.setIdUser(data.get("idUser").toString());
        newItem.setIdCat(data.get("idCat").toString());
        newItem.setIdDistrict(data.get("idDistrict").toString());
        newItem.setAddress(data.get("address").toString());
        newItem.setTimeDeadline(data.get("timeDeadline").toString());
        newItem.setTimeCreated(data.get("timeCreated").toString());
        newItem.setNote(data.get("note").toString());
        mData.addNew(newItem);
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
    public void onCancelled(DatabaseError databaseError) {

    }
}
