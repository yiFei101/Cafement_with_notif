package com.example.loginauthentication.MerchantPanel;

import static java.security.AccessController.getContext;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.ChefFoodPanel_BottomNavigation;
import com.example.loginauthentication.R;
import com.example.loginauthentication.ReusableCode.ReusableCodeForAll;
import com.example.loginauthentication.SendNotification.APIService;
import com.example.loginauthentication.SendNotification.Client;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChefPreparedOrderView extends AppCompatActivity {

    RecyclerView recyclerViewdish;
    private List<ChefFinalOrders> chefFinalOrdersList;
    private ChefPreparedOrderViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID, userid, merchantID, merchantName, merchantToken, userToken;

    TextView grandtotal, address, name, number;
    LinearLayout l1;
    Button Prepared;
    private ProgressDialog progressDialog;
    private APIService apiService;
    Spinner Shipper;
    String deliveryId = "oCpc4SwLVFbKO0fPdtp4R6bmDmI3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_prepared_order_view);
        recyclerViewdish = findViewById(R.id.Recycle_viewOrder);
        grandtotal = findViewById(R.id.Gtotal);
        address = findViewById(R.id.Cadress);
        l1 = findViewById(R.id.linear);
        Prepared = findViewById(R.id.prepared);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        progressDialog = new ProgressDialog(ChefPreparedOrderView.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(ChefPreparedOrderView.this));
        chefFinalOrdersList = new ArrayList<>();
        CheforderdishesView();
    }

    private void CheforderdishesView() {

        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("MerchantFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chefFinalOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChefFinalOrders chefFinalOrders = snapshot.getValue(ChefFinalOrders.class);
                    chefFinalOrdersList.add(chefFinalOrders);
                }
                if (chefFinalOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                    Prepared.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();

                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("Merchant").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            data.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("MerchantFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
                                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                final ChefFinalOrders chefFinalOrders = dataSnapshot1.getValue(ChefFinalOrders.class);
                                                HashMap<String, String> hashMap = new HashMap<>();
                                                String dishid = chefFinalOrders.getDishId();
                                                userid = chefFinalOrders.getUserId();
                                                merchantID = chefFinalOrders.getMerchantId();
                                                hashMap.put("MerchantId", chefFinalOrders.getMerchantId());
                                                hashMap.put("DishId", chefFinalOrders.getDishId());
                                                hashMap.put("DishName", chefFinalOrders.getDishName());
                                                hashMap.put("DishPrice", chefFinalOrders.getDishPrice());
                                                hashMap.put("DishQuantity", chefFinalOrders.getDishQuantity());
                                                hashMap.put("RandomUID", RandomUID);
                                                hashMap.put("TotalPrice", chefFinalOrders.getTotalPrice());
                                                hashMap.put("UserId", chefFinalOrders.getUserId());
                                                FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(deliveryId).child(RandomUID).child("Dishes").child(dishid).setValue(hashMap);
                                            }
                                            //for merchant name
                                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Merchant").child(merchantID);
                                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        merchantName = dataSnapshot.child("First Name").getValue(String.class);
                                                    } else {
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(v.getContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                            //for user token
                                            DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Tokens").child(userid);
                                            db1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        userToken = dataSnapshot.getValue(String.class);
                                                    } else {
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(v.getContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                            //for merchant token
                                            DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("Tokens").child(merchantID);
                                            db2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        merchantToken = dataSnapshot.getValue(String.class);
                                                    } else {
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(v.getContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                                                }
                                            });


                                            DatabaseReference data = FirebaseDatabase.getInstance().getReference("MerchantFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                            data.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    final ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                                                    HashMap<String, String> hashMap1 = new HashMap<>();
                                                    String merchantid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    hashMap1.put("Address", chefFinalOrders1.getAddress());
                                                    hashMap1.put("Merchant", merchantid);
                                                    hashMap1.put("GrandTotalPrice", chefFinalOrders1.getGrandTotalPrice());
                                                    hashMap1.put("RandomUID", RandomUID);
                                                    hashMap1.put("Status", "Order is Ready to Pick Up");
                                                    hashMap1.put("UserId", userid);
                                                    FirebaseDatabase.getInstance().getReference("DeliveryShipOrders").child(deliveryId).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseDatabase.getInstance().getReference("StudentFinalOrders").child(userid).child(RandomUID).child("OtherInformation").child("Status").setValue("Order is Ready to Pick Up...").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                            try {
                                                                                JSONArray tokens = new JSONArray();
                                                                                tokens.put(userToken);

                                                                                JSONObject data = new JSONObject();
                                                                                data.put(Constants.KEY_USER_ID, merchantID);
                                                                                data.put(Constants.KEY_NAME, "Order Prepared");
                                                                                data.put(Constants.KEY_FCM_TOKEN, merchantToken);
                                                                                data.put(Constants.KEY_MESSAGE, merchantName + " is prepared Your Order please Pick up.");

                                                                                JSONObject body = new JSONObject();
                                                                                body.put(Constants.REMOTE_MSG_DATA, data);
                                                                                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);


                                                                                sendNotification(body.toString());
                                                                                Toast.makeText(v.getContext(),"Notification Sent",Toast.LENGTH_LONG).show();

                                                                            }catch(Exception exception){
                                                                                Toast.makeText(v.getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                                                            }

                                                                            ReusableCodeForAll.ShowAlert(v.getContext(),"","Wait for the Student to Pickup the Order");

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

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    });

                }
                adapter = new ChefPreparedOrderViewAdapter(ChefPreparedOrderView.this, chefFinalOrdersList);
                recyclerViewdish.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MerchantFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChefFinalOrders1 chefFinalOrders1 = dataSnapshot.getValue(ChefFinalOrders1.class);
                grandtotal.setText("₱ " + chefFinalOrders1.getGrandTotalPrice());
                address.setText(chefFinalOrders1.getAddress());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String messageBody) {
        ApiClient.getClient().create(ApiService.class).sendMessage(
                Constants.getRemoteMsgHeaders(),
                messageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray results = responseJson.getJSONArray("result");
                            if (responseJson.getInt("failure") == 1) {
                                JSONObject error = (JSONObject) results.get(0);
                                Toast.makeText(ChefPreparedOrderView.this, "Error.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //  showToast("Notification sent successfully");

                } else {
                    Toast.makeText(ChefPreparedOrderView.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(ChefPreparedOrderView.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}

