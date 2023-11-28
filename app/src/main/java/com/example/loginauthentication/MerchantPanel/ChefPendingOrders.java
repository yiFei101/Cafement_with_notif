package com.example.loginauthentication.MerchantPanel;

public class ChefPendingOrders {

    private String MerchantId,DishId,DishName,DishQuantity,Price,RandomUID,TotalPrice,UserId;

    public ChefPendingOrders(String merchantId, String dishId, String dishName, String dishQuantity, String price, String randomUID, String totalPrice, String userId) {
        MerchantId = merchantId;
        DishId = dishId;
        DishName = dishName;
        DishQuantity = dishQuantity;
        Price = price;
        RandomUID=randomUID;
        TotalPrice = totalPrice;
        UserId = userId;
    }

    public ChefPendingOrders()
    {

    }

    public String getMerchantId() {
        return  MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getDishId() {
        return DishId;
    }

    public void setDishId(String dishId) {
        DishId = dishId;
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

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}