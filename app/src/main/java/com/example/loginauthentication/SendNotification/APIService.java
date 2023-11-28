package com.example.loginauthentication.SendNotification;

import static com.example.loginauthentication.SendNotification.ValuesClass.CONTENT_TYPE;
import static com.example.loginauthentication.SendNotification.ValuesClass.SERVER_KEY;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({"Authorization: "+SERVER_KEY,"Content-type: "+CONTENT_TYPE})
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender notificationSender);
}