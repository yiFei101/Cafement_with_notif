package com.example.loginauthentication.CustomerFoodPanel;

public class Cart {

    private String MerchantId, DishID, DishName, DishQuantity, Price, Totalprice;

    public Cart(String merchantId, String dishID, String dishName, String dishQuantity, String price, String totalprice) {
        MerchantId = merchantId;
        DishID = dishID;
        DishName = dishName;
        DishQuantity = dishQuantity;
        Price = price;
        Totalprice = totalprice;
    }

    public Cart() {
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

    public String getTotalprice() {
        return Totalprice;
    }

    public void setTotalprice(String totalprice) {
        Totalprice = totalprice;
    }
}