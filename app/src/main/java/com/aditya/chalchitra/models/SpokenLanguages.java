package com.aditya.chalchitra.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SpokenLanguages implements Parcelable{

    private String iso_639_1;
    private String name;

    protected SpokenLanguages(Parcel in) {
        iso_639_1 = in.readString();
        name = in.readString();
    }

    public static final Creator<SpokenLanguages> CREATOR = new Creator<SpokenLanguages>() {
        @Override
        public SpokenLanguages createFromParcel(Parcel in) {
            return new SpokenLanguages(in);
        }

        @Override
        public SpokenLanguages[] newArray(int size) {
            return new SpokenLanguages[size];
        }
    };

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iso_639_1);
        dest.writeString(name);
    }
}
