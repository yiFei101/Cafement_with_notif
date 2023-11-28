package com.example.loginauthentication.CustomerFoodPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.CustomerFoodPanel_BottomNavigation;
import com.example.loginauthentication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingOrders extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<CustomerPendingOrders> customerPendingOrdersList;
    private PendingOrdersAdapter adapter;
    DatabaseReference databaseReference;
    ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);
        recyclerView = findViewById(R.id.Recycleorders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PendingOrders.this));
        customerPendingOrdersList = new ArrayList<>();
        backButton = findViewById(R.id.backButton);
        CustomerpendingOrders();
    }

    private void CustomerpendingOrders() {

        databaseReference = FirebaseDatabase.getInstance().getReference("StudentPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerPendingOrdersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DatabaseReference data = FirebaseDatabase.getInstance().getReference("StudentPendingOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Dishes");
                    data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                CustomerPendingOrders customerPendingOrders = snapshot1.getValue(CustomerPendingOrders.class);
                                customerPendingOrdersList.add(customerPendingOrders);
                            }
                            adapter = new PendingOrdersAdapter(PendingOrders.this, customerPendingOrdersList);
                            recyclerView.setAdapter(adapter);
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PendingOrders.this, CustomerFoodPanel_BottomNavigation.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
