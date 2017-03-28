package com.example.phuong.viectimnguoiapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by asiantech on 27/03/2017.
 */
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class HistoryPing implements Parcelable {
    private String idPost;
    private String titlePost;
    private String timeCreated;
    private String price;
    private String note;
    private String address;
    private String userOwner;
    private String nameDistrict;

    protected HistoryPing(Parcel in) {
        idPost = in.readString();
        titlePost = in.readString();
        timeCreated = in.readString();
        price = in.readString();
        note = in.readString();
        address = in.readString();
        userOwner = in.readString();
        nameDistrict = in.readString();
    }

    public static final Creator<HistoryPing> CREATOR = new Creator<HistoryPing>() {
        @Override
        public HistoryPing createFromParcel(Parcel in) {
            return new HistoryPing(in);
        }

        @Override
        public HistoryPing[] newArray(int size) {
            return new HistoryPing[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idPost);
        parcel.writeString(titlePost);
        parcel.writeString(timeCreated);
        parcel.writeString(price);
        parcel.writeString(note);
        parcel.writeString(address);
        parcel.writeString(userOwner);
        parcel.writeString(nameDistrict);
    }
}
