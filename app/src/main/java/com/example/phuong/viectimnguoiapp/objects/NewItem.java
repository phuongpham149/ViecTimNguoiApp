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
    private String date;
    private String title;
    private String detail;
    private int idUser;

    protected NewItem(Parcel in) {
        date = in.readString();
        title = in.readString();
        detail = in.readString();
        idUser = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(title);
        dest.writeString(detail);
        dest.writeInt(idUser);
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
