package com.example.loginauthentication.MerchantPanel;

public class merchantpendingorders {

    private String MerchantId, DishId, DishName, DishQuantity, Price, RandomUID, TotalPrice, UserId;

    public merchantpendingorders(String MerchantId, String DishId, String DishName, String DishQuantity, String Price, String RandomUID, String TotalPrice, String UserId) {
        this.MerchantId = MerchantId;
        this.DishId = DishId;
        this.DishName = DishName;
        this.DishQuantity = DishQuantity;
        this.Price = Price;
        this.RandomUID = RandomUID;
        this.TotalPrice = TotalPrice;
        this.UserId = UserId;
    }


    public merchantpendingorders()
    {

    }

    public String getMerchantId() {
        return MerchantId;
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


