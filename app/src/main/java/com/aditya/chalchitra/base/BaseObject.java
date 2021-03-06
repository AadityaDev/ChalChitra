package com.aditya.chalchitra.base;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseObject<T> implements Parcelable {


    protected BaseObject(Parcel in) {
    }

    public static final Creator<BaseObject> CREATOR = new Creator<BaseObject>() {
        @Override
        public BaseObject createFromParcel(Parcel in) {
            return new BaseObject(in);
        }

        @Override
        public BaseObject[] newArray(int size) {
            return new BaseObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
