package com.example.loginauthentication.CustomerFoodPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.MerchantPanel.UpdateDishModel;
import com.example.loginauthentication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder> {

    private Context mcontext;
    private List<UpdateDishModel> updateDishModellist;

    public CustomerHomeAdapter(Context context, List<UpdateDishModel> updateDishModellist) {
        this.updateDishModellist = updateDishModellist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_menudish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final UpdateDishModel updateDishModel = updateDishModellist.get(position);

        holder.Dishname.setText(updateDishModel.getDishes());
        String MerchantId = updateDishModel.getMerchantId();
        holder.price.setText("Price: ₱ " + updateDishModel.getPrice());

        String ImageURL = updateDishModel.getImageURL(); // This should be the URL of the image in Firebase Storage

        if (ImageURL != null && !ImageURL.isEmpty()) {
            // Use Picasso to load and display the image
            Picasso.get()
                    .load(ImageURL)
                    .placeholder(R.drawable.placeholder_image) // Optional placeholder image
                    .error(R.drawable.placeholder_image) // Optional error image
                    .into(holder.imageView);
        } else {
            // If there's no valid imageURL, you can set a placeholder or default image here.
            holder.imageView.setImageResource(R.drawable.placeholder_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, OrderDish.class);
                intent.putExtra("FoodDetails", updateDishModel.getRandomUID());
                intent.putExtra("MerchantId", updateDishModel.getMerchantId());
                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView Dishname, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.menu_image);
            Dishname = itemView.findViewById(R.id.dishname);
            price = itemView.findViewById(R.id.dishprice);
        }
    }
}