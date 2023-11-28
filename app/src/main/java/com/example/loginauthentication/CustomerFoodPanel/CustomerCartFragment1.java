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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.R;
import com.example.loginauthentication.ReusableCode.ReusableCodeForAll;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerCartFragment1 extends Fragment {

    RecyclerView recyclecart;
    private List<Cart> cartModelList;
    private CustomerCartAdapter adapter;
    private LinearLayout TotalBtns;
    DatabaseReference databaseReference, data, reference, ref, getRef, dataa;
    public static TextView grandt;
    Button remove, placeorder;
    String DishId, RandomUId, MerchantId;
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
        customercart();
        return v;
    }

    private void customercart() {
        // Load cart items from Firebase
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                }
                adapter = new CustomerCartAdapter(getContext(), cartModelList);
                recyclecart.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });

        // Set click listeners
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveConfirmationDialog();
            }
        });
        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });
    }

    // Function to show remove confirmation dialog
    private void showRemoveConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to remove the items from the cart?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Remove items from the cart in Firebase
                FirebaseDatabase.getInstance().getReference("Cart")
                        .child("CartItems")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .removeValue();
                FirebaseDatabase.getInstance().getReference("Cart")
                        .child("GrandTotal")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .removeValue();
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

    // Function to place an order
    private void placeOrder() {
        // Check if the user has already placed an order
        FirebaseDatabase.getInstance().getReference("AlreadyOrdered")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("isOrdered")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String ss = dataSnapshot.exists() ? dataSnapshot.getValue(String.class) : "";

                        if (ss.trim().equalsIgnoreCase("false") || ss.trim().isEmpty()) {
                            showAddressDialog();
                        } else {
                            ReusableCodeForAll.ShowAlert(getContext(), "Error", "It seems you have already placed an order. ");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                    }
                });
    }

    // Function to show the address dialog
    private void showAddressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Address");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.enter_address, null);
        final EditText localaddress = view.findViewById(R.id.LA);
        RadioGroup group = view.findViewById(R.id.grp);
        final RadioButton home = view.findViewById(R.id.HA);
        final RadioButton other = view.findViewById(R.id.OA);
        builder.setView(view);

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Place the order logic
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                // ... (Add order details to Firebase)
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
