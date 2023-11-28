package com.example.loginauthentication.CustomerFoodPanel;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

public class PayableOrders extends AppCompatActivity {

    RecyclerView recyclerView;
    private List<CustomerPaymentOrders> customerPaymentOrdersList;
    private PayableOrderAdapter adapter;
    DatabaseReference databaseReference;
    private LinearLayout pay;
    Button payment;
    TextView total;
    private SwipeRefreshLayout swipeRefreshLayout;
    ImageView backButton;
    Dialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mdialog = new Dialog(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payable_orders);
        recyclerView = findViewById(R.id.recyclepayableorder);
        pay = findViewById(R.id.btn);
        total = findViewById(R.id.rs);
        payment = (Button) findViewById(R.id.paymentmethod);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(PayableOrders.this));
        customerPaymentOrdersList = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.Swipe2);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
        adapter = new PayableOrderAdapter(PayableOrders.this, customerPaymentOrdersList);
        recyclerView.setAdapter(adapter);
        backButton = findViewById(R.id.backButton);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView = findViewById(R.id.recyclepayableorder);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(PayableOrders.this));
                customerPaymentOrdersList = new ArrayList<>();
                CustomerpayableOrders();
            }
        });
        CustomerpayableOrders();

    }

    private void CustomerpayableOrders() {

        databaseReference = FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    customerPaymentOrdersList.clear();
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final String randomuid = snapshot.getKey();
                        DatabaseReference data = FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(snapshot.getKey()).child("Dishes");
                        data.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                    CustomerPaymentOrders customerPaymentOrders = snapshot1.getValue(CustomerPaymentOrders.class);
                                    customerPaymentOrdersList.add(customerPaymentOrders);
                                }
                                if (customerPaymentOrdersList.size() == 0) {
                                    pay.setVisibility(View.INVISIBLE);
                                } else {
                                    pay.setVisibility(View.VISIBLE);
                                    payment.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(PayableOrders.this, CustomerPayment.class);
                                            intent.putExtra("RandomUID", randomuid);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });


                                }
                                adapter = new PayableOrderAdapter(PayableOrders.this, customerPaymentOrdersList);
                                recyclerView.setAdapter(adapter);
                                swipeRefreshLayout.setRefreshing(false);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("StudentPaymentOrders").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(randomuid).child("OtherInformation");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    CustomerPaymentOrders1 customerPaymentOrders1 = dataSnapshot.getValue(CustomerPaymentOrders1.class);
                                    total.setText("₱ " + customerPaymentOrders1.getGrandTotalPrice());
                                    swipeRefreshLayout.setRefreshing(false);

                                } else {
                                    swipeRefreshLayout.setRefreshing(false);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayableOrders.this, CustomerFoodPanel_BottomNavigation.class);
                startActivity(intent);
                finish();
            }
        });

    }

}