package com.example.loginauthentication.CustomerFoodPanel;

import static java.security.AccessController.getContext;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginauthentication.CustomerFoodPanel_BottomNavigation;
import com.example.loginauthentication.MerchantPanel.ChefPreparedOrderView;
import com.example.loginauthentication.R;
import com.example.loginauthentication.ReusableCodeForAll;
import com.example.loginauthentication.SendNotification.APIService;
import com.example.loginauthentication.SendNotification.Data;
import com.example.loginauthentication.SendNotification.MyResponse;
import com.example.loginauthentication.SendNotification.NotificationSender;
import com.example.loginauthentication.utilities.ApiClient;
import com.example.loginauthentication.utilities.ApiService;
import com.example.loginauthentication.utilities.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerPayment extends AppCompatActivity {

    TextView  CashPayment;
    String RandomUID, MerchantId, currentUserToken,merchantToken,currentUserName,userID;
    String  merchantID;
    private ProgressDialog progressDialog;


    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);

        CashPayment = (TextView) findViewById(R.id.cashPaymentButton);
        RandomUID = getIntent().getStringExtra("RandomUID");



        CashPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            final CustomerPaymentOrders customerPaymentOrders = dataSnapshot1.getValue(CustomerPaymentOrders.class);
                            HashMap<String, String> hashMap = new HashMap<>();
                            String dishid = customerPaymentOrders.getDishId();
                            hashMap.put("MerchantId", customerPaymentOrders.getMerchantId());
                            hashMap.put("DishId", customerPaymentOrders.getDishId());
                            hashMap.put("DishName", customerPaymentOrders.getDishName());
                            hashMap.put("DishPrice", customerPaymentOrders.getDishPrice());
                            hashMap.put("DishQuantity", customerPaymentOrders.getDishQuantity());
                            hashMap.put("RandomUID", RandomUID);
                            hashMap.put("TotalPrice", customerPaymentOrders.getTotalPrice());
                            hashMap.put("UserId", customerPaymentOrders.getUserId());
                            FirebaseDatabase.getInstance().getReference("StudentFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes").child(dishid).setValue(hashMap);
                        }
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                final CustomerPaymentOrders1 customerPaymentOrders1 = dataSnapshot.getValue(CustomerPaymentOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("Address", customerPaymentOrders1.getAddress());
                                hashMap1.put("GrandTotalPrice", customerPaymentOrders1.getGrandTotalPrice());
                                hashMap1.put("Note", customerPaymentOrders1.getNote());
                                hashMap1.put("RandomUID", RandomUID);
                                hashMap1.put("Status", "Your order is waiting to be prepared by Merchant...");
                                FirebaseDatabase.getInstance().getReference("StudentFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
                                        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final CustomerPaymentOrders customerPaymentOrderss = snapshot.getValue(CustomerPaymentOrders.class);
                                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                                    String dishid = customerPaymentOrderss.getDishId();
                                                    MerchantId = customerPaymentOrderss.getMerchantId();
                                                    hashMap2.put("Merchant", customerPaymentOrderss.getMerchantId());
                                                    hashMap2.put("DishId", customerPaymentOrderss.getDishId());
                                                    hashMap2.put("DishName", customerPaymentOrderss.getDishName());
                                                    hashMap2.put("DishPrice", customerPaymentOrderss.getDishPrice());
                                                    hashMap2.put("DishQuantity", customerPaymentOrderss.getDishQuantity());
                                                    hashMap2.put("RandomUID", RandomUID);
                                                    hashMap2.put("TotalPrice", customerPaymentOrderss.getTotalPrice());
                                                    hashMap2.put("UserId", customerPaymentOrderss.getUserId());
                                                    FirebaseDatabase.getInstance().getReference("MerchantWaitingOrders").child(MerchantId).child(RandomUID).child("Dishes").child(dishid).setValue(hashMap2);
                                                }
                                                //for user token
                                                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Tokens").child(userID);
                                                db1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            currentUserToken = dataSnapshot.getValue(String.class);

                                                        }
                                                        else {
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Toast.makeText(v.getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                                //for merchant token
                                                DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("Tokens").child(merchantID);
                                                db2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            merchantToken = dataSnapshot.getValue(String.class);
                                                        }
                                                        else {
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Toast.makeText(v.getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                                    }
                                                });


                                                DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        CustomerPaymentOrders1 customerPaymentOrders11 = dataSnapshot.getValue(CustomerPaymentOrders1.class);
                                                        HashMap<String, String> hashMap3 = new HashMap<>();
                                                        hashMap3.put("Address", customerPaymentOrders11.getAddress());
                                                        hashMap3.put("GrandTotalPrice", customerPaymentOrders11.getGrandTotalPrice());
                                                        hashMap3.put("Note", customerPaymentOrders11.getNote());
                                                        hashMap3.put("RandomUID", RandomUID);
                                                        hashMap3.put("Status", "Your order is waiting to be prepared by Merchant...");
                                                        FirebaseDatabase.getInstance().getReference("MerchantWaitingOrders").child(MerchantId).child(RandomUID).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                FirebaseDatabase.getInstance().getReference("MerchantPaymentOrders").child(MerchantId).child(RandomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        FirebaseDatabase.getInstance().getReference("MerchantPaymentOrders").child(MerchantId).child(RandomUID).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(MerchantId).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                        try{
                                                                                                            JSONArray tokens = new JSONArray();
                                                                                                            tokens.put(merchantToken);

                                                                                                            JSONObject data = new JSONObject();
                                                                                                            data.put(Constants.KEY_USER_ID, userID);
                                                                                                            data.put(Constants.KEY_NAME, "New Order");
                                                                                                            data.put(Constants.KEY_FCM_TOKEN, currentUserToken);
                                                                                                            data.put(Constants.KEY_MESSAGE, "You have new order from "+currentUserName);

                                                                                                            JSONObject body = new JSONObject();
                                                                                                            body.put(Constants.REMOTE_MSG_DATA, data);
                                                                                                            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                                                                                                            sendNotification(body.toString());


                                                                                                        }catch(Exception exception){
                                                                                                            Toast.makeText(v.getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                                                                                        }

                                                                                                        progressDialog.dismiss();
                                                                                                        ReusableCodeForAll.ShowAlert(v.getContext(), "", "Your Order has been Ready for Pickup ");
                                                                                                    }


                                                                                                    @Override
                                                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                                                    }
                                                                                                });

                                                                                            }

                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    private void sendNotification(String messageBody){
        ApiClient.getClient().create(ApiService.class).sendMessage(
                Constants.getRemoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    try{
                        if(response.body() != null){
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("result");
                            if(responseJson.getInt("failure") == 1){
                                JSONObject error = (JSONObject) results.get(0);
                                Toast.makeText(CustomerPayment.this, "Error.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    //  showToast("Notification sent successfully");

                }else{
                    Toast.makeText(CustomerPayment.this, "Error.", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(CustomerPayment.this, "Error.", Toast.LENGTH_LONG).show();
            }
        });

    }

}



