package com.example.loginauthentication.StudentPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView; // Import RecyclerView
import com.example.loginauthentication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeFragment  extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private List<FoodDetails> foodList;
    private FoodAdapter adapter;

    public StudentHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        foodList = new ArrayList<>();
        adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add a ValueEventListener to retrieve data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    FoodDetails foodDetails = postSnapshot.getValue(FoodDetails.class);
                    foodList.add(foodDetails);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}