package com.example.timeclock.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    public enum UserType {
        administrator,
        nonadministrator
    }
    @SerializedName("id")
    private String userId;
    private String password;
    @SerializedName("Role")
    private UserType userType;
    @SerializedName("Name")
    private String name;
    @SerializedName("Image")
    private String image;

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(this.userType == null ? -1 : this.userType.ordinal());
    }

    protected User(Parcel in) {
        userId = in.readString();
        password = in.readString();
        name = in.readString();
        image = in.readString();
        int tmpUserType = in.readInt();
        this.userType = tmpUserType == -1 ? null : UserType.values()[tmpUserType];
    }
}
