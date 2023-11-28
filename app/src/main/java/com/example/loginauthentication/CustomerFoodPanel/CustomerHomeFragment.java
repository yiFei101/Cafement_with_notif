package com.example.loginauthentication.CustomerFoodPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.loginauthentication.MerchantPanel.UpdateDishModel;
import com.example.loginauthentication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private List<UpdateDishModel> updateDishModelList;
    private CustomerHomeAdapter adapter;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customerhome, container, false);
        getActivity().setTitle("Cafement");
        setHasOptionsMenu(true);

        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
        recyclerView.startAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateDishModelList = new ArrayList<>();
        swipeRefreshLayout = v.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);

        // Fetch data from Firebase Realtime Database
        fetchDataFromFirebase();

        return v;
    }

    @Override
    public void onRefresh() {
        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        swipeRefreshLayout.setRefreshing(true);

        // Reference to the Firebase Realtime Database where your data is located
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails");
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateDishModelList.clear();
                for (DataSnapshot merchantSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot dishSnapshot : merchantSnapshot.getChildren()) {
                        UpdateDishModel updateDishModel = dishSnapshot.getValue(UpdateDishModel.class);
                        String imageURL = dishSnapshot.child("ImageURL").getValue(String.class);
                        if (imageURL != null) {
                            // Set the imageURL to the UpdateDishModel object
                            updateDishModel.setImageURL(imageURL);
                        }

                        updateDishModelList.add(updateDishModel);
                    }
                }
                // Initialize or update the adapter
                if (adapter == null) {
                    adapter = new CustomerHomeAdapter(getContext(), updateDishModelList);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }

                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.Searchdish);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Dish");

        // Initialize searchView query text listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });
    }

    private void search(String searchtext) {
        ArrayList<UpdateDishModel> mylist = new ArrayList<>();
        for (UpdateDishModel object : updateDishModelList) {
            if (object.getDishes().toLowerCase().contains(searchtext.toLowerCase())) {
                mylist.add(object);
            }
        }

        // Update the adapter with the filtered list
        adapter = new CustomerHomeAdapter(getContext(), mylist);
        recyclerView.setAdapter(adapter);
    }
}