package com.usehover.runner.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SingleFilterInfoModel implements Parcelable {
    private String title, country = "";
    private boolean check;

   private SingleFilterInfoModel(Parcel in) {
        title = in.readString();
        country = in.readString();
        check = in.readByte() != 0;
    }

    public SingleFilterInfoModel(String title, boolean check) {
        this.title = title;
        this.check = check;
    }

    public SingleFilterInfoModel(String title, String country, boolean check) {
        this.title = title;
        this.country = country;
        this.check = check;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(country);
        dest.writeByte((byte) (check ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SingleFilterInfoModel> CREATOR = new Creator<SingleFilterInfoModel>() {
        @Override
        public SingleFilterInfoModel createFromParcel(Parcel in) {
            return new SingleFilterInfoModel(in);
        }

        @Override
        public SingleFilterInfoModel[] newArray(int size) {
            return new SingleFilterInfoModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

}
