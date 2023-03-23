package com.example.timeclock.models;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class AdminUser extends User {

    public AdminUser() {

    }

    public static final Creator<AdminUser> CREATOR = new Creator<AdminUser>() {
        @Override
        public AdminUser createFromParcel(Parcel source) {
            return new AdminUser(source);
        }

        @Override
        public AdminUser[] newArray(int size) {
            return new AdminUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected AdminUser(Parcel in) {
        super(in);
    }
}
