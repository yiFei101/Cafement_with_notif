package com.example.loginauthentication.MerchantPanel;

public class FoodDetails {
    public String Dishes,Quantity,Price,Description,ImageURL,RandomUID,MerchantId ;
    // Alt+insert

    public FoodDetails(String dishes, String quantity, String price, String description, String imageURL, String randomUID, String merchantId) {
        Dishes = dishes;
        Quantity = quantity;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID = randomUID;
        MerchantId = merchantId;
    }
}

