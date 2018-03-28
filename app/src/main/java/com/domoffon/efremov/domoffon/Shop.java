package com.domoffon.efremov.domoffon;

public class Shop {
    private    static final String TAG = "Fruit";

    private int ShopImg;
	private String ShopName;
    private String calories;

	public Shop(int fruitImg, String fruitName,String calories) {
		super();
        this.setShopImg(fruitImg);
		this.setShopName(fruitName);
        this.setCalories(calories);
	}

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String fruitName) {
        this.ShopName = fruitName;
    }

    public int getShopImg() {
        return ShopImg;
    }

    public void setShopImg(int fruitImg) {
        this.ShopImg = fruitImg;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}