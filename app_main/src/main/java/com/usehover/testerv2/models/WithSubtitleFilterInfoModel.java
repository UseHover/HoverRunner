package com.usehover.testerv2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class WithSubtitleFilterInfoModel implements Parcelable {
    private String title, subtitle;
    private boolean check;

    public WithSubtitleFilterInfoModel(String title, String subtitle, boolean check) {
        this.title = title;
        this.subtitle = subtitle;
        this.check = check;
    }

    private WithSubtitleFilterInfoModel(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        check = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeByte((byte) (check ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WithSubtitleFilterInfoModel> CREATOR = new Creator<WithSubtitleFilterInfoModel>() {
        @Override
        public WithSubtitleFilterInfoModel createFromParcel(Parcel in) {
            return new WithSubtitleFilterInfoModel(in);
        }

        @Override
        public WithSubtitleFilterInfoModel[] newArray(int size) {
            return new WithSubtitleFilterInfoModel[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
