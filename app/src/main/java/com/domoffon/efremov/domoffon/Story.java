package com.domoffon.efremov.domoffon;

/**
 * Created by 1 on 10.12.2017.
 */

public class Story {
    private    static final String TAG = "Shop";

    private int StoryImg;
    private String StoryName;
    private String calories;

    public Story(int PaymentImg, String PaymentName, String calories) {
        super();
        this.setStoryImg(PaymentImg);
        this.setStoryName(PaymentName);
        this.setCalories(calories);
    }

    public String getStoryName() {
        return StoryName;
    }

    public void setStoryName(String StoryName) {
        this.StoryName = StoryName;
    }

    public int getStoryImg() {
        return StoryImg;
    }

    public void setStoryImg(int PaymentImg) {
        this.StoryImg = PaymentImg;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}
