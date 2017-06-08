package com.aditya.chalchitra.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductionCountries implements Parcelable{

    private String iso_3166_1;
    private String name;

    protected ProductionCountries(Parcel in) {
        iso_3166_1 = in.readString();
        name = in.readString();
    }

    public static final Creator<ProductionCountries> CREATOR = new Creator<ProductionCountries>() {
        @Override
        public ProductionCountries createFromParcel(Parcel in) {
            return new ProductionCountries(in);
        }

        @Override
        public ProductionCountries[] newArray(int size) {
            return new ProductionCountries[size];
        }
    };

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
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
        dest.writeString(iso_3166_1);
        dest.writeString(name);
    }
}
