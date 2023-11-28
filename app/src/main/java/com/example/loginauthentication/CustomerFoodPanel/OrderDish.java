package com.example.loginauthentication.CustomerFoodPanel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.loginauthentication.CustomerFoodPanel_BottomNavigation;
import com.example.loginauthentication.MerchantPanel.Merchant;
import com.example.loginauthentication.MerchantPanel.UpdateDishModel;
import com.example.loginauthentication.R;
import com.example.loginauthentication.Student;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class OrderDish extends AppCompatActivity {

    String RandomId, MerchantID;
    ImageView imageView;
    ElegantNumberButton additem;
    TextView Foodname, MerchantName, FoodQuantity, FoodPrice, FoodDescription;
    DatabaseReference databaseReference, dataaa, merchantdata, reference, data, dataref;
    String dishname;
    int dishprice;
    String studID;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dish);

        Foodname = findViewById(R.id.food_name);
        FoodPrice = findViewById(R.id.food_price);
        FoodDescription = findViewById(R.id.food_description);
        imageView = findViewById(R.id.image);
        additem = findViewById(R.id.number_btn);

        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Student").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student stud = dataSnapshot.getValue(Student.class);

                RandomId = getIntent().getStringExtra("FoodDetails");
                MerchantID = getIntent().getStringExtra("MerchantId");

                databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails").child(MerchantID).child(RandomId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);
                        if (updateDishModel != null) {
                            Foodname.setText(updateDishModel.getDishes());
                            String ss = "<b>" + "Description: " + "</b>" + updateDishModel.getDescription();
                            FoodDescription.setText(Html.fromHtml(ss));
                            String pri = "<b>" + "Price: ₱ " + "</b>" + updateDishModel.getPrice();
                            FoodPrice.setText(Html.fromHtml(pri));
                            Picasso.get()
                                    .load(updateDishModel.getImageURL())
                                    .into(imageView);

                            merchantdata = FirebaseDatabase.getInstance().getReference("Merchant").child(MerchantID);
                            merchantdata.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Merchant merchant = dataSnapshot.getValue(Merchant.class);
                                    if (merchant != null) {
                                        studID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(studID).child(RandomId);
                                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                Cart cart = dataSnapshot.getValue(Cart.class);
                                                if (dataSnapshot.exists() && cart != null) {
                                                    additem.setNumber(cart.getDishQuantity());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                additem.setOnClickListener(new ElegantNumberButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dataref = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Cart cart1 = null;
                                if (dataSnapshot.exists()) {
                                    int totalcount = 0;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        totalcount++;
                                    }
                                    int i = 0;
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        i++;
                                        if (i == totalcount) {
                                            cart1 = snapshot.getValue(Cart.class);
                                        }
                                    }

                                    if (cart1 != null && MerchantID.equals(cart1.getMerchantId())) {
                                        data = FirebaseDatabase.getInstance().getReference("FoodDetails").child(MerchantID).child(RandomId);
                                        data.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                UpdateDishModel update = dataSnapshot.getValue(UpdateDishModel.class);
                                                if (update != null) {
                                                    dishname = update.getDishes();
                                                    dishprice = Integer.parseInt(update.getPrice());

                                                    int num = Integer.parseInt(additem.getNumber());
                                                    int totalprice = num * dishprice;
                                                    if (num != 0) {
                                                        HashMap<String, String> hashMap = new HashMap<>();
                                                        hashMap.put("DishName", dishname);
                                                        hashMap.put("DishID", RandomId);
                                                        hashMap.put("DishQuantity", String.valueOf(num));
                                                        hashMap.put("Price", String.valueOf(dishprice));
                                                        hashMap.put("Totalprice", String.valueOf(totalprice));
                                                        hashMap.put("MerchantId", MerchantID);
                                                        studID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                        reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(studID).child(RandomId);
                                                        reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(OrderDish.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    } else {
                                                        firebaseDatabase.getInstance().getReference("Cart").child(studID).child(RandomId).removeValue();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDish.this);
                                        builder.setMessage("You can't add food items of multiple merchants at a time. Try to add items from the same merchant.");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(OrderDish.this, CustomerFoodPanel_BottomNavigation.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                } else {
                                    data = FirebaseDatabase.getInstance().getReference("FoodDetails").child(MerchantID).child(RandomId);
                                    data.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            UpdateDishModel update = dataSnapshot.getValue(UpdateDishModel.class);
                                            if (update != null) {
                                                dishname = update.getDishes();
                                                dishprice = Integer.parseInt(update.getPrice());
                                                int num = Integer.parseInt(additem.getNumber());
                                                int totalprice = num * dishprice;
                                                if (num != 0) {
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("DishName", dishname);
                                                    hashMap.put("DishID", RandomId);
                                                    hashMap.put("DishQuantity", String.valueOf(num));
                                                    hashMap.put("Price", String.valueOf(dishprice));
                                                    hashMap.put("Totalprice", String.valueOf(totalprice));
                                                    hashMap.put("MerchantId", MerchantID);
                                                    studID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(studID).child(RandomId);
                                                    reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(OrderDish.this, "Added to cart", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    firebaseDatabase.getInstance().getReference("Cart").child(studID).child(RandomId).removeValue();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
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
}
