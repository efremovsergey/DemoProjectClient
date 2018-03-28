package com.domoffon.efremov.domoffon;

/**
 * Created by 1 on 10.12.2017.
 */

public class Profile {
    private    static final String TAG = "Shop";

    private String ProfileName;
    private String calories;

    Profile(String PaymentName, String calories) {
        super();
        this.setProfileName(PaymentName);
        this.setCalories(calories);
    }

    String getProfileName() {
        return ProfileName;
    }

    private void setProfileName(String PaymentName) {
        this.ProfileName = PaymentName;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}
