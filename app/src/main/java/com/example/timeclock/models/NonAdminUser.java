package com.example.timeclock.models;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class NonAdminUser extends User {
    private Shift currentShift;

    public NonAdminUser() {

    }

    public Shift getCurrentShift() {
        return currentShift;
    }

    public void setCurrentShift(Shift currentShift) {
        this.currentShift = currentShift;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.currentShift, flags);
    }

    protected NonAdminUser(Parcel in) {
        super(in);
        this.currentShift = in.readParcelable(Shift.class.getClassLoader());
    }

    public static final Creator<NonAdminUser> CREATOR = new Creator<NonAdminUser>() {
        @Override
        public NonAdminUser createFromParcel(Parcel source) {
            return new NonAdminUser(source);
        }

        @Override
        public NonAdminUser[] newArray(int size) {
            return new NonAdminUser[size];
        }
    };
}
