package com.example.loginauthentication.MerchantPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.util.HashMap;
import java.util.List;
import com.example.loginauthentication.SendNotification.APIService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChefPendingOrdersAdapter extends RecyclerView.Adapter<ChefPendingOrdersAdapter.ViewHolder> {
    private APIService apiService;
    private Context context;
    private List<ChefPendingOrders1> chefPendingOrders1list;
    String userid, dishid, merchantID, merchantName, merchantToken, userToken;


    public ChefPendingOrdersAdapter(Context context, List<ChefPendingOrders1> chefPendingOrders1list) {
        this.chefPendingOrders1list = chefPendingOrders1list;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chef_orders, parent, false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        return new ChefPendingOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ChefPendingOrders1 chefPendingOrders1 = chefPendingOrders1list.get(position);
        holder.Address.setText(chefPendingOrders1.getAddress());
        holder.grandtotalprice.setText("Total: ₱ " + chefPendingOrders1.getGrandTotalPrice());
        final String random = chefPendingOrders1.getRandomUID();
        holder.Vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chef_order_dishes.class);
                intent.putExtra("RandomUID", random);
                context.startActivity(intent);
            }
        });

        holder.Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Dishes");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final ChefPendingOrders chefPendingOrders = snapshot.getValue(ChefPendingOrders.class);
                            HashMap<String, String> hashMap = new HashMap<>();
                            String merchantid = chefPendingOrders.getMerchantId();
                            String dishid = chefPendingOrders.getDishId();
                            hashMap.put("MerchantId", chefPendingOrders.getMerchantId());
                            hashMap.put("DishId", chefPendingOrders.getDishId());
                            hashMap.put("DishName", chefPendingOrders.getDishName());
                            hashMap.put("DishPrice", chefPendingOrders.getPrice());
                            hashMap.put("DishQuantity", chefPendingOrders.getDishQuantity());
                            hashMap.put("RandomUID", random);
                            hashMap.put("TotalPrice", chefPendingOrders.getTotalPrice());
                            hashMap.put("UserId", chefPendingOrders.getUserId());
                            FirebaseDatabase.getInstance().getReference("MerchantPaymentOrders").child(merchantid).child(random).child("Dishes").child(dishid).setValue(hashMap);
                        }
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation");
                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                ChefPendingOrders1 chefPendingOrders1 = dataSnapshot.getValue(ChefPendingOrders1.class);
                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("Address", chefPendingOrders1.getAddress());
                                hashMap1.put("GrandTotalPrice", chefPendingOrders1.getGrandTotalPrice());
                                hashMap1.put("MobileNumber", chefPendingOrders1.getMobileNumber());
                                hashMap1.put("Name", chefPendingOrders1.getName());
                                hashMap1.put("Note",chefPendingOrders1.getNote());
                                hashMap1.put("RandomUID", random);
                                FirebaseDatabase.getInstance().getReference("MerchantPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        DatabaseReference Reference = FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Dishes");
                                        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    final ChefPendingOrders chefPendingOrders = snapshot.getValue(ChefPendingOrders.class);
                                                    HashMap<String, String> hashMap2 = new HashMap<>();
                                                    userid = chefPendingOrders.getUserId();
                                                    dishid = chefPendingOrders.getDishId();
                                                    merchantID = chefPendingOrders.getMerchantId();
                                                    hashMap2.put("MerchantId", chefPendingOrders.getMerchantId());
                                                    hashMap2.put("DishId", chefPendingOrders.getDishId());
                                                    hashMap2.put("DishName", chefPendingOrders.getDishName());
                                                    hashMap2.put("DishPrice", chefPendingOrders.getPrice());
                                                    hashMap2.put("DishQuantity", chefPendingOrders.getDishQuantity());
                                                    hashMap2.put("RandomUID", random);
                                                    hashMap2.put("TotalPrice", chefPendingOrders.getTotalPrice());
                                                    hashMap2.put("UserId", chefPendingOrders.getUserId());
                                                    FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(userid).child(random).child("Dishes").child(dishid).setValue(hashMap2);
                                                }

                                                //for merchant name
                                                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Merchant").child(merchantID);
                                                db.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            merchantName = dataSnapshot.child("First Name").getValue(String.class);
                                                        }
                                                        else {
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Toast.makeText(v.getContext(), "Something went wrong.",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                //for user token
                                                DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Tokens").child(userid);
                                                db1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            userToken = dataSnapshot.getValue(String.class);
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


                                                DatabaseReference dataa = FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation");
                                                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        ChefPendingOrders1 chefPendingOrders1 = dataSnapshot.getValue(ChefPendingOrders1.class);
                                                        HashMap<String, String> hashMap3 = new HashMap<>();
                                                        hashMap3.put("Address", chefPendingOrders1.getAddress());
                                                        hashMap3.put("MobileNumber", chefPendingOrders1.getMobileNumber());
                                                        hashMap3.put("GrandTotalPrice", chefPendingOrders1.getGrandTotalPrice());
                                                        hashMap3.put("Name", chefPendingOrders1.getName());
                                                        hashMap3.put("Note",chefPendingOrders1.getNote());
                                                        hashMap3.put("RandomUID", random);
                                                        FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(userid).child(random).child("OtherInformation").setValue(hashMap3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                FirebaseDatabase.getInstance().getReference("StudentPendingOrders").child(userid).child(random).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        FirebaseDatabase.getInstance().getReference("StudentPendingOrders").child(userid).child(random).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                        FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                        try{
                                                                                                            JSONArray tokens = new JSONArray();
                                                                                                            tokens.put(userToken);

                                                                                                            JSONObject data = new JSONObject();
                                                                                                            data.put(Constants.KEY_USER_ID, merchantID);
                                                                                                            data.put(Constants.KEY_NAME, "Order Payment");
                                                                                                            data.put(Constants.KEY_FCM_TOKEN, merchantToken);
                                                                                                            data.put(Constants.KEY_MESSAGE, merchantName+" is waiting for your payment.");

                                                                                                            JSONObject body = new JSONObject();
                                                                                                            body.put(Constants.REMOTE_MSG_DATA, data);
                                                                                                            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                                                                                                            sendNotification(body.toString());
                                                                                                            Toast.makeText(v.getContext(),"Notification Sent",Toast.LENGTH_LONG).show();

                                                                                                        }catch(Exception exception){
                                                                                                            Toast.makeText(v.getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                                                                                        }

                                                                                                        ReusableCodeForAll.ShowAlert(context,"","Wait for the Student to make Payment");

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

        holder.Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Dishes");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            final ChefPendingOrders chefPendingOrders = snapshot.getValue(ChefPendingOrders.class);
                            userid = chefPendingOrders.getUserId();
                            dishid = chefPendingOrders.getDishId();
                            merchantID = chefPendingOrders.getMerchantId();
                            //for merchant name
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Merchant").child(merchantID);
                            db.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        merchantName = dataSnapshot.child("First Name").getValue(String.class);
                                        //Toast.makeText(v.getContext(), "Name"+merchantName,Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(v.getContext(), "Something went wrong.",Toast.LENGTH_LONG).show();
                                }
                            });
                            //for user token
                            DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Tokens").child(userid);
                            db1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        userToken = dataSnapshot.getValue(String.class);
                                     //   Toast.makeText(v.getContext(), "UserToken"+userToken,Toast.LENGTH_LONG).show();
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
                                       // Toast.makeText(v.getContext(), "merchantToken"+merchantToken,Toast.LENGTH_LONG).show();

                                        try{
                                            JSONArray tokens = new JSONArray();
                                            tokens.put(userToken);

                                            JSONObject data = new JSONObject();
                                            data.put(Constants.KEY_USER_ID, merchantID);
                                            data.put(Constants.KEY_NAME, "Order Rejected");
                                            data.put(Constants.KEY_FCM_TOKEN, merchantToken);
                                            data.put(Constants.KEY_MESSAGE, merchantName+" has rejected your order.");

                                            JSONObject body = new JSONObject();
                                            body.put(Constants.REMOTE_MSG_DATA, data);
                                            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                                            sendNotification(body.toString());
                                            Toast.makeText(v.getContext(),"Notification Sent",Toast.LENGTH_LONG).show();

                                        }catch(Exception exception){
                                            Toast.makeText(v.getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else {
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(v.getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                }
                            });
                        }



                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                FirebaseDatabase.getInstance().getReference("StudentPendingOrders").child(userid).child(random).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        FirebaseDatabase.getInstance().getReference("StudentPendingOrders").child(userid).child(random).child("OtherInformation").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("Dishes").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(random).child("OtherInformation").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(userid).child("isOrdered").setValue("false");
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
                                Toast.makeText(context,"Error.",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    //  showToast("Notification sent successfully");

                }else{
                    Toast.makeText(context,"Error: " + response.code(),Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return chefPendingOrders1list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Address, grandtotalprice;
        Button Vieworder, Accept, Reject;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Address = itemView.findViewById(R.id.AD);
            grandtotalprice = itemView.findViewById(R.id.TP);
            Vieworder = itemView.findViewById(R.id.vieww);
            Accept = itemView.findViewById(R.id.accept);
            Reject = itemView.findViewById(R.id.reject);
        }
    }
}