package com.example.loginauthentication.CustomerFoodPanel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.loginauthentication.CustomerFoodPanel_BottomNavigation;
import com.example.loginauthentication.MainMenu;
import com.example.loginauthentication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SettingsFragment extends Fragment {
    LinearLayout LogOut;
    Button Clearorder;
    String ID, RandomUID;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Settings");
        View v = inflater.inflate(R.layout.fragment_settings, null);

        // Find your LogOut view by ID
        LogOut = v.findViewById(R.id.logout_layout);
        Clearorder =  v.findViewById(R.id.clearorder);

        Clearorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Delete  Order History");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseDatabase.getInstance().getReference("StudentFinalOrders")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                        FirebaseDatabase.getInstance().getReference("AlreadyOrdered")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();

                        AlertDialog.Builder food = new AlertDialog.Builder(getActivity());
                        food.setMessage("Your Order History has been Deleted");
                        food.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getActivity(), CustomerFoodPanel_BottomNavigation.class));                                        }
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



        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Logout ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), MainMenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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

        return v;
    }
}