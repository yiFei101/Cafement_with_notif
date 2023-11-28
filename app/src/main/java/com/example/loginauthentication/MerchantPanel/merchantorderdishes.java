package com.example.loginauthentication.MerchantPanel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class merchantorderdishes extends AppCompatActivity {

    RecyclerView recyclerViewdish;
    private List<merchantpendingorders> merchantpendingOrdersList;
    private MerchantorderdishesAdapter adapter;
    DatabaseReference reference;
    String RandomUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchantorderdishes);
        recyclerViewdish = findViewById(R.id.Recycle_orders_dish);
        recyclerViewdish.setHasFixedSize(true);
        recyclerViewdish.setLayoutManager(new LinearLayoutManager(this));
        merchantpendingOrdersList = new ArrayList<>();
        Cheforderdishes();

    }

    private void Cheforderdishes() {

        RandomUID = getIntent().getStringExtra("RandomUID");

        reference = FirebaseDatabase.getInstance().getReference("MerchantPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUID).child("Dishes");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                merchantpendingOrdersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    merchantpendingorders chefPendingOrders = snapshot.getValue(merchantpendingorders.class);
                    merchantpendingOrdersList.add(chefPendingOrders);
                }
                adapter = new MerchantorderdishesAdapter(merchantorderdishes.this,  merchantpendingOrdersList);
                recyclerViewdish.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


