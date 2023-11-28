package com.example.loginauthentication.StudentPanel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.loginauthentication.StudentPanel.FoodDetails;
import com.example.loginauthentication.R;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<FoodDetails> foodList;

    public FoodAdapter(List<FoodDetails> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodDetails food = foodList.get(position);
        holder.bind(food);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private TextView dishNameTextView;
        private TextView descriptionTextView;
        private TextView quantityTextView;
        private TextView priceTextView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            dishNameTextView = itemView.findViewById(R.id.dishNameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }

        public void bind(FoodDetails food) {
            dishNameTextView.setText(food.getDishName());
            descriptionTextView.setText(food.getDescription());
            quantityTextView.setText(food.getQuantity());
            priceTextView.setText(food.getPrice());
            // You can also load the image here if needed.
        }
    }
}
