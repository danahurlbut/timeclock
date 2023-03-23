package com.example.timeclock.viewmodels;

import android.location.Location;
import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timeclock.models.NonAdminUser;
import com.example.timeclock.models.Shift;
import com.example.timeclock.models.User;
import com.example.timeclock.repositories.UserRepository;

import java.util.Date;

public class ShiftActivityViewModel extends ViewModel {
    private final UserRepository userRepository; //FUTURE ENHANCEMENT replace with dependency injection
    private NonAdminUser user;
    private Uri picture; //TODO when real API is ready this would be a String url
    private Location location;

    //there are 3 steps in the shift process.
    //1) need to get both photo and location
    //2) have photo, need to get location
    //3) have photo and location, need to submit
    private int step = 1;
    private boolean hasStartedShift;

    public ShiftActivityViewModel() {
        userRepository = new UserRepository();
    }

    public void setUser(NonAdminUser user) {
        this.user = user;
        //TODO delete block when real API is up. Setting one user to have already started a mock shift
        if (user.getUserId().equals("rec_cf6lol7k2id522jo5m3g")) {
            Shift shift = new Shift();
            Date date = new Date(System.currentTimeMillis() - (8 * 60 * 60 * 1000));
            shift.setStartDateTime(date);
            this.user.setCurrentShift(shift);
        }
        hasStartedShift = this.user.getCurrentShift() != null;
    }

    public NonAdminUser getUser() {
        return user;
    }

    /*
    If user is in the middle of a shift, returns that shift. If not, returns null
     */
    public boolean hasStartedShift() {
        return hasStartedShift;
    }

    public void setPicture(Uri uri) {
        step = 2;
        this.picture = uri;
    }

    public Uri getPicture() {
        return picture;
    }

    public void setLocation(Location location) {
        step = 3;
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }

    public int getStep() {
        return step;
    }

    public MutableLiveData<User> startShift() {
        Shift shift = new Shift();
        shift.setStartDateTime(new Date());
        shift.setStartPicture(""); //fill in real URL from picture variable when API is ready
        shift.setStartLocation(location);
        user.setCurrentShift(shift);

        return userRepository.updateUser(user);
    }

    public MutableLiveData<User> endShift() {
        Shift shift = user.getCurrentShift();
        shift.setStartDateTime(new Date());
        shift.setStartPicture(""); //fill in real URL from picture variable when API is ready
        shift.setStartLocation(location);
        return userRepository.updateUser(user);
    }

}
