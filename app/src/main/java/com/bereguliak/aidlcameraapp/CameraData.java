package com.bereguliak.aidlcameraapp;

import android.os.Parcel;
import android.os.Parcelable;

public class CameraData implements Parcelable {
    public static final Creator<CameraData> CREATOR = new Creator<CameraData>() {
        @Override
        public CameraData createFromParcel(Parcel in) {
            return new CameraData(in);
        }

        @Override
        public CameraData[] newArray(int size) {
            return new CameraData[size];
        }
    };

    private final int mId;
    private final String mName;

    public CameraData(int id, String name) {
        mId = id;
        mName = name;
    }

    protected CameraData(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
    }

    //region CameraData
    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
    //endregion

    //region Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
    }
    //endregion

    //region Object
    @Override
    public String toString() {
        return "CameraData{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                '}';
    }
    //endregion
}
