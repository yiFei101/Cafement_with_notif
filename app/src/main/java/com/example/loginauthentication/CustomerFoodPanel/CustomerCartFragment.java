package com.example.loginauthentication.CustomerFoodPanel;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.R;
import com.example.loginauthentication.ReusableCodeForAll;
import com.example.loginauthentication.SendNotification.APIService;
import com.example.loginauthentication.SendNotification.Client;
import com.example.loginauthentication.SendNotification.Data;
import com.example.loginauthentication.utilities.ApiClient;
import com.example.loginauthentication.utilities.ApiService;
import com.example.loginauthentication.utilities.Constants;
import com.example.loginauthentication.SendNotification.MyResponse;
import com.example.loginauthentication.SendNotification.NotificationSender;
import com.example.loginauthentication.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCartFragment  extends Fragment {

    RecyclerView recyclecart;
    private List<Cart> cartModelList;
    private CustomerCartAdapter adapter;
    private LinearLayout TotalBtns;
    DatabaseReference databaseReference, data, reference, ref, getRef, dataa;
    public static TextView grandt;
    Button remove, placeorder;
    String address, Addnote, merchantID;
    String DishId, RandomUId, MerchantId, currentUserToken,merchantToken,currentUserName,userID;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Cart");
        View v = inflater.inflate(R.layout.fragment_customercart, null);
        recyclecart = v.findViewById(R.id.recyclecart);
        recyclecart.setHasFixedSize(true);
        recyclecart.setLayoutManager(new LinearLayoutManager(getContext()));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        cartModelList = new ArrayList<>();
        grandt = v.findViewById(R.id.GT);
        remove = v.findViewById(R.id.RM);
        placeorder = v.findViewById(R.id.PO);
        TotalBtns = v.findViewById(R.id.TotalBtns);
     //   apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        customercart();
        return v;
    }

    private void customercart() {

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cartModelList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Cart cart = snapshot.getValue(Cart.class);

                    cartModelList.add(cart);
                }
                if (cartModelList.size() == 0) {
                    TotalBtns.setVisibility(View.INVISIBLE);
                } else {
                    TotalBtns.setVisibility(View.VISIBLE);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Are you sure you want to remove Dish");
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                    FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();

                        }
                    });


                   // userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    data = FirebaseDatabase.getInstance().getReference("Student").child(userID);
                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Student student = dataSnapshot.getValue(Student.class);
                            placeorder.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //Toast.makeText(getContext(),userID,Toast.LENGTH_LONG).show();


                                    FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            String ss = "";
                                            if (dataSnapshot.exists()) {
                                                ss = dataSnapshot.getValue(String.class);
                                            }

                                            if (ss.trim().equalsIgnoreCase("false") || ss.trim().equalsIgnoreCase("")) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setTitle("Enter Name and Section");
                                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                                View view = inflater.inflate(R.layout.enter_address, null);
                                                final EditText localaddress = (EditText) view.findViewById(R.id.LA);
                                                final EditText addnote = (EditText) view.findViewById(R.id.addnote);
                                                RadioGroup group = (RadioGroup) view.findViewById(R.id.grp);
                                                final RadioButton home = (RadioButton) view.findViewById(R.id.HA);
                                               final RadioButton other = (RadioButton) view.findViewById(R.id.OA);
                                                builder.setView(view);
                                                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                        if (home.isChecked()) {

                                                        } else if (other.isChecked()) {
                                                            localaddress.getText().clear();
                                                            Toast.makeText(getContext(), "check", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        progressDialog.setMessage("Please wait...");
                                                        progressDialog.show();

                                                        reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                RandomUId = UUID.randomUUID().toString();
                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    final Cart cart1 = dataSnapshot1.getValue(Cart.class);
                                                                    DishId = cart1.getDishID();
                                                                    merchantID = cart1.getMerchantId();
                                                                    address = localaddress.getText().toString().trim();
                                                                    Addnote = addnote.getText().toString().trim();
                                                                    final HashMap<String, String> hashMap = new HashMap<>();
                                                                    hashMap.put("MerchantId", cart1.getMerchantId());
                                                                    hashMap.put("DishID", cart1.getDishID());
                                                                    hashMap.put("DishName", cart1.getDishName());
                                                                    hashMap.put("DishQuantity", cart1.getDishQuantity());
                                                                    hashMap.put("Price", cart1.getPrice());
                                                                    hashMap.put("TotalPrice", cart1.getTotalprice());
                                                                    FirebaseDatabase.getInstance().getReference("StudentPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("Dishes").child(DishId).setValue(hashMap);

                                                                }
                                                                //for user name
                                                                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Student").child(userID);
                                                                db.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()) {
                                                                            currentUserName = dataSnapshot.child("First Name").getValue(String.class);

                                                                        }
                                                                        else {
                                                                        }
                                                                    }
                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                        Toast.makeText(getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                                                    }
                                                                });
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
                                                                        Toast.makeText(getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
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
                                                                        Toast.makeText(getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                                                    }
                                                                });


                                                                ref = FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("GrandTotal");
                                                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        String grandtotal = dataSnapshot.getValue(String.class);
                                                                        HashMap<String, String> hashMap1 = new HashMap<>();
                                                                        hashMap1.put("Address", address);
                                                                        hashMap1.put("GrandTotalPrice", String.valueOf(grandtotal));
                                                                        hashMap1.put("Name", student.getFirstName() + " " + student.getLastName());
                                                                        hashMap1.put("Note", Addnote);
                                                                        FirebaseDatabase.getInstance().getReference("StudentPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("OtherInformation").setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        FirebaseDatabase.getInstance().getReference("Cart").child("GrandTotal").child(FirebaseAuth.getInstance().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                                getRef = FirebaseDatabase.getInstance().getReference("StudentPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("Dishes");
                                                                                                getRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                    @Override
                                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                                                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                                                                                            final CustomerPendingOrders customerPendingOrders = dataSnapshot2.getValue(CustomerPendingOrders.class);
                                                                                                            String d = customerPendingOrders.getDishID();
                                                                                                            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                                                                            MerchantId = customerPendingOrders.getMerchantId();
                                                                                                            final HashMap<String, String> hashMap2 = new HashMap<>();
                                                                                                            hashMap2.put("MerchantId", MerchantId);
                                                                                                            hashMap2.put("DishId", customerPendingOrders.getDishID());
                                                                                                            hashMap2.put("DishName", customerPendingOrders.getDishName());
                                                                                                            hashMap2.put("DishQuantity", customerPendingOrders.getDishQuantity());
                                                                                                            hashMap2.put("Price", customerPendingOrders.getPrice());
                                                                                                            hashMap2.put("RandomUID", RandomUId);
                                                                                                            hashMap2.put("TotalPrice", customerPendingOrders.getTotalPrice());
                                                                                                            hashMap2.put("UserId", userid);
                                                                                                            FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(MerchantId).child(RandomUId).child("Dishes").child(d).setValue(hashMap2);
                                                                                                        }

                                                                                                        dataa = FirebaseDatabase.getInstance().getReference("StudentPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId).child("OtherInformation");
                                                                                                        dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                            @Override
                                                                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                                                CustomerPendingOrders1 customerPendingOrders1 = dataSnapshot.getValue(CustomerPendingOrders1.class);
                                                                                                                HashMap<String, String> hashMap3 = new HashMap<>();
                                                                                                                hashMap3.put("Address", customerPendingOrders1.getAddress());
                                                                                                                hashMap3.put("GrandTotalPrice", customerPendingOrders1.getGrandTotalPrice());
                                                                                                                hashMap3.put("MobileNumber", customerPendingOrders1.getMobileNumber());
                                                                                                                hashMap3.put("Name", customerPendingOrders1.getName());
                                                                                                                hashMap3.put("Note", customerPendingOrders1.getNote());
                                                                                                                hashMap3.put("RandomUID", RandomUId);

                                                                                                                FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(MerchantId).child(RandomUId).child("OtherInformation").setValue(hashMap3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                    @Override
                                                                                                                    public void onSuccess(Void aVoid) {

                                                                                                                        FirebaseDatabase.getInstance().getReference("AlreadyOrdered").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isOrdered").setValue("true").addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                                                                                                                            Toast.makeText(getContext(),"Something went wrong.",Toast.LENGTH_LONG).show();
                                                                                                                                        }

                                                                                                                                        progressDialog.dismiss();
                                                                                                                                        ReusableCodeForAll.ShowAlert(getContext(), "", "Your Order has been shifted to Pending state, please wait until the Merchant accept your order.");
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
//                                                                                                            }
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

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });
//                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });

                                                        dialog.dismiss();
                                                    }
                                                });
                                                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                AlertDialog aler = builder.create();
                                                aler.show();

                                            } else {
                                                ReusableCodeForAll.ShowAlert(getContext(), "Error", "It seems you have already placed the order");
                                            }

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
                adapter = new CustomerCartAdapter(getContext(), cartModelList);
                recyclecart.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                                Toast.makeText(getContext(),"Error.",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    //  showToast("Notification sent successfully");

                }else{
                    Toast.makeText(getContext(),"Error: " + response.code(),Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

}

