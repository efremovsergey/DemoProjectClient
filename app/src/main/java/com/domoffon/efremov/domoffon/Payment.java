package com.domoffon.efremov.domoffon;

public class Payment {

    private int PaymentImg;
    private String PaymentName;
    private String calories;

    public Payment(int PaymentImg, String PaymentName,String calories) {
        super();
        this.setPaymentImg(PaymentImg);
        this.setPaymentName(PaymentName);
        this.setCalories(calories);
    }

    String getPaymentName() {
        return PaymentName;
    }

    private void setPaymentName(String PaymentName) {
        this.PaymentName = PaymentName;
    }

    int getPaymentImg() {
        return PaymentImg;
    }

    private void setPaymentImg(int PaymentImg) {
        this.PaymentImg = PaymentImg;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}
