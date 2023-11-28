package com.example.loginauthentication.MerchantPanel;

import static java.security.AccessController.getContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.R;
import com.example.loginauthentication.ReusableCodeForAll;
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

public class ChefOrdertobePrepareView extends AppCompatActivity {
    RecyclerView recyclerViewdish;
    private List<ChefWaitingOrders> chefWaitingOrdersList;
    private ChefOrdertobePrepareViewAdapter adapter;
    DatabaseReference reference;
    String RandomUID, userid, merchantID, merchantName, merchantToken, userToken;


    TextView grandtotal, note, address;
    LinearLayout l1;
    Button Preparing;
    private ProgressDialog progressDialog;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_ordertobe_prepare_view);
        recyclerViewdish = findViewById(R.id.Recycle_viewOrder);
        grandtotal = findViewById(R.id.rupees);
        note = findViewById(R.id.NOTE);
        address = findViewById(R.id.ad);
        l1 = findViewById(R.id.button1);
        Preparing = findViewById(R.id.preparing);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        progressDialog = new ProgressDialog(ChefOrdertobePrepareView.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(ChefOrdertobePrepareView.this));
        chefWaitingOrdersList = new ArrayList<>();
        CheforderdishesView();
    }

    private void CheforderdishesView() {
        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("MerchantWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chefWaitingOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChefWaitingOrders chefWaitingOrders = snapshot.getValue(ChefWaitingOrders.class);
                    chefWaitingOrdersList.add(chefWaitingOrders);
                }
                if (chefWaitingOrdersList.size() == 0) {
                    l1.setVisibility(View.INVISIBLE);

                } else {
                    l1.setVisibility(View.VISIBLE);
                    Preparing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            progressDialog.setMessage("Please wait...");
                            progressDialog.show();

                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("MerchantWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        final ChefWaitingOrders chefWaitingOrders = dataSnapshot1.getValue(ChefWaitingOrders.class);
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        String dishid = chefWaitingOrders.getDishId();
                                        userid = chefWaitingOrders.getUserId();
                                        hashMap.put("MerchantId", chefWaitingOrders.getMerchantId());
                                        hashMap.put("DishId", chefWaitingOrders.getDishId());
                                        hashMap.put("DishName", chefWaitingOrders.getDishName());
                                        hashMap.put("DishPrice", chefWaitingOrders.getDishPrice());
                                        hashMap.put("DishQuantity", chefWaitingOrders.getDishQuantity());
                                        hashMap.put("RandomUID", RandomUID);
                                        hashMap.put("TotalPrice", chefWaitingOrders.getTotalPrice());
                                        hashMap.put("UserId", chefWaitingOrders.getUserId());
                                        FirebaseDatabase.getInstance().getReference("MerchantFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes").child(dishid).setValue(hashMap);
                                    }
                                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("MerchantWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final ChefWaitingOrders1 chefWaitingOrders1 = dataSnapshot.getValue(ChefWaitingOrders1.class);
                                            HashMap<String, String> hashMap1 = new HashMap<>();
                                            hashMap1.put("Address", chefWaitingOrders1.getAddress());
                                            hashMap1.put("GrandTotalPrice", chefWaitingOrders1.getGrandTotalPrice());
                                            hashMap1.put("RandomUID", RandomUID);
                                            hashMap1.put("Status", "Merchant is preparing your Order...");


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

                                            FirebaseDatabase.getInstance().getReference("MerchantFinalOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseDatabase.getInstance().getReference("StudentFinalOrders").child(userid).child(RandomUID).child("OtherInformation").child("Status").setValue("Merchant is preparing your order...").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            FirebaseDatabase.getInstance().getReference("MerchantWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    FirebaseDatabase.getInstance().getReference("MerchantWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                                                                        data.put(Constants.KEY_NAME, "Preparing Order");
                                                                                        data.put(Constants.KEY_FCM_TOKEN, merchantToken);
                                                                                        data.put(Constants.KEY_MESSAGE, merchantName + " is Preparing Your Order.");

                                                                                        JSONObject body = new JSONObject();
                                                                                        body.put(Constants.REMOTE_MSG_DATA, data);
                                                                                        body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                                                                                        sendNotification(body.toString());


                                                                                    }catch(Exception exception){
                                                                                        Toast.makeText(v.getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                                                                    }

                                                                                    progressDialog.dismiss();
                                                                                    ReusableCodeForAll.ShowAlert(v.getContext(), "", "Your Order has been Preparing please wait for Pickup ");
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
                adapter = new ChefOrdertobePrepareViewAdapter(ChefOrdertobePrepareView.this, chefWaitingOrdersList);
                recyclerViewdish.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MerchantWaitingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("OtherInformation");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChefWaitingOrders1 chefWaitingOrders1 = dataSnapshot.getValue(ChefWaitingOrders1.class);
                grandtotal.setText("₱ " + chefWaitingOrders1.getGrandTotalPrice());
                note.setText(chefWaitingOrders1.getNote());
                address.setText(chefWaitingOrders1.getAddress());


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
                                Toast.makeText(ChefOrdertobePrepareView.this, "Error.", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    //  showToast("Notification sent successfully");

                }else{
                    Toast.makeText(ChefOrdertobePrepareView.this, "Error.", Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(ChefOrdertobePrepareView.this, "Error.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
