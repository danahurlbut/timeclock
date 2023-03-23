package com.example.timeclock.models;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Shift implements Parcelable {
    private Date startDateTime;
    private Date endDateTime;
    private String startPicture;
    private String endPicture;
    private Location startLocation;
    private Location endLocation;

    public Shift() {

    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getStartPicture() {
        return startPicture;
    }

    public void setStartPicture(String startPicture) {
        this.startPicture = startPicture;
    }

    public String getEndPicture() {
        return endPicture;
    }

    public void setEndPicture(String endPicture) {
        this.endPicture = endPicture;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.startDateTime);
        dest.writeSerializable(this.endDateTime);
        dest.writeString(this.startPicture);
        dest.writeString(this.endPicture);
        dest.writeParcelable(this.startLocation, flags);
        dest.writeParcelable(this.endLocation, flags);
    }

    protected Shift(Parcel in) {
        this.startDateTime = (Date) in.readSerializable();
        this.endDateTime = (Date) in.readSerializable();
        this.startPicture = in.readString();
        this.endPicture = in.readString();
        this.startLocation = in.readParcelable(Location.class.getClassLoader());
        this.endLocation = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Shift> CREATOR = new Creator<Shift>() {
        @Override
        public Shift createFromParcel(Parcel source) {
            return new Shift(source);
        }

        @Override
        public Shift[] newArray(int size) {
            return new Shift[size];
        }
    };
}
