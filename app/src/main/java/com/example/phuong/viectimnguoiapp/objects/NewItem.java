package com.example.phuong.viectimnguoiapp.objects;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by phuong on 21/02/2017.
 */
@NoArgsConstructor()
@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class NewItem implements Parcelable {
    private String id;
    private int idCat;
    private String timeDeadline;
    private String address;
    private String timeCreated;
    private String idUser;
    private int idDistrict;
    private String note;

    protected NewItem(Parcel in) {
        id = in.readString();
        idCat = in.readInt();
        timeCreated = in.readString();
        timeDeadline = in.readString();
        note = in.readString();
        address = in.readString();
        idDistrict = in.readInt();
        idUser = in.readString();
    }

    public static final Creator<NewItem> CREATOR = new Creator<NewItem>() {
        @Override
        public NewItem createFromParcel(Parcel in) {
            return new NewItem(in);
        }

        @Override
        public NewItem[] newArray(int size) {
            return new NewItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeInt(idCat);
        parcel.writeString(timeCreated);
        parcel.writeString(timeDeadline);
        parcel.writeString(note);
        parcel.writeString(address);
        parcel.writeInt(idDistrict);
        parcel.writeString(idUser);
    }
}
