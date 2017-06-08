package com.aditya.chalchitra.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductionCompanies implements Parcelable{

    private long id;
    private String name;

    protected ProductionCompanies(Parcel in) {
        id = in.readLong();
        name = in.readString();
    }

    public static final Creator<ProductionCompanies> CREATOR = new Creator<ProductionCompanies>() {
        @Override
        public ProductionCompanies createFromParcel(Parcel in) {
            return new ProductionCompanies(in);
        }

        @Override
        public ProductionCompanies[] newArray(int size) {
            return new ProductionCompanies[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        dest.writeLong(id);
        dest.writeString(name);
    }
}
