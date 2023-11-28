package com.example.loginauthentication.CustomerFoodPanel;

public class CustomerPendingOrders {

    private String MerchantId, DishID, DishName, DishQuantity, Price, TotalPrice;

    public CustomerPendingOrders(String dishID, String dishName, String dishQuantity, String price, String totalPrice, String merchantId) {
        MerchantId = merchantId;
        DishID = dishID;
        DishName = dishName;
        DishQuantity = dishQuantity;
        Price = price;
        TotalPrice = totalPrice;

    }

    public CustomerPendingOrders() {

    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getDishID() {
        return DishID;
    }

    public void setDishID(String dishID) {
        DishID = dishID;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getDishQuantity() {
        return DishQuantity;
    }

    public void setDishQuantity(String dishQuantity) {
        DishQuantity = dishQuantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }


}
