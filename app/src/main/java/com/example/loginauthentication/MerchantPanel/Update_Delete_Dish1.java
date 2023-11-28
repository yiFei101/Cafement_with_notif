package com.example.loginauthentication.MerchantPanel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loginauthentication.ChefFoodPanel_BottomNavigation;
import com.example.loginauthentication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Update_Delete_Dish1 extends AppCompatActivity {


    TextInputLayout desc, qty, pri;
    TextView Dishname;
    private ImageView imageView;
    Uri imageuri;
    String dburi;
    Button Update_dish, Delete_dish;
    String description, quantity, price, dishes, MerchantId;
    String RandomUId;
    StorageReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth FAuth;
    String ID;
    private ProgressDialog progressDialog;
    DatabaseReference dataaa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__delete__dish);

        desc = findViewById(R.id.description);
        qty = findViewById(R.id.quantity);
        pri = findViewById(R.id.price);
        Dishname = findViewById(R.id.dish_name);
        imageView = findViewById(R.id.imgView);
        Update_dish = findViewById(R.id.Updatedish);
        Delete_dish = findViewById(R.id.Deletedish);


        ID = getIntent().getStringExtra("updatedeletedish");

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Merchant").child(userid);
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Merchant merchant = dataSnapshot.getValue(Merchant.class);

                Update_dish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        description = desc.getEditText().getText().toString().trim();
                        quantity = qty.getEditText().getText().toString().trim();
                        price = pri.getEditText().getText().toString().trim();

                        if (isValid()) {
                            if (imageuri != null) {

                            } else {
                                updatedesc(dburi);
                            }
                        }
                    }
                });

                Delete_dish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Update_Delete_Dish1.this);
                        builder.setMessage("Are you sure you want to Delete Dish");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference("FoodDetails")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID).removeValue();

                                AlertDialog.Builder food = new AlertDialog.Builder(Update_Delete_Dish1.this);
                                food.setMessage("Your Dish has been Deleted");
                                food.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(Update_Delete_Dish1.this, ChefFoodPanel_BottomNavigation.class));
                                    }
                                });
                                AlertDialog alertt = food.create();
                                alertt.show();
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

                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                progressDialog = new ProgressDialog(Update_Delete_Dish1.this);
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails").child(useridd).child(ID);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UpdateDishModel updateDishModel = dataSnapshot.getValue(UpdateDishModel.class);

                        desc.getEditText().setText(updateDishModel.getDescription());
                        qty.getEditText().setText(updateDishModel.getQuantity());
                        Dishname.setText("Dish name: " + updateDishModel.getDishes());
                        dishes = updateDishModel.getDishes();
                        pri.getEditText().setText(updateDishModel.getPrice());

                        // Use Picasso to load the image
                        String imageURL = updateDishModel.getImageURL();
                        if (imageURL != null && !imageURL.isEmpty()) {
                            Picasso.get()
                                    .load(imageURL)
                                    .into(imageView);
                            dburi = imageURL;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                FAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails");
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private boolean isValid() {
        desc.setErrorEnabled(false);
        desc.setError("");
        qty.setErrorEnabled(false);
        qty.setError("");
        pri.setErrorEnabled(false);
        pri.setError("");

        boolean isValiDescription = false, isValidPrice = false, isvalidQuantity = false, isvalid = false;
        if (TextUtils.isEmpty(description)) {
            desc.setErrorEnabled(true);
            desc.setError("Description is Required");
        } else {
            desc.setError(null);
            isValiDescription = true;
        }
        if (TextUtils.isEmpty(quantity)) {
            qty.setErrorEnabled(true);
            qty.setError("Quantity is Required");
        } else {
            isvalidQuantity = true;
        }
        if (TextUtils.isEmpty(price)) {
            pri.setErrorEnabled(true);
            pri.setError("Price is Required");
        } else {
            isValidPrice = true;
        }
        isvalid = (isValiDescription && isvalidQuantity && isValidPrice);
        return isvalid;
    }






    private void updatedesc(String uri) {
        MerchantId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FoodDetails info = new FoodDetails(dishes, quantity, price, description, uri, ID, MerchantId);
        FirebaseDatabase.getInstance().getReference("FoodDetails")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID)
                .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        Toast.makeText(Update_Delete_Dish1.this, "Dish Updated Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    }

