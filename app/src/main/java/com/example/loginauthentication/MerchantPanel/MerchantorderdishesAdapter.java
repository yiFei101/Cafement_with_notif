package com.example.loginauthentication.MerchantPanel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginauthentication.R;

import java.util.List;

public class MerchantorderdishesAdapter extends RecyclerView.Adapter<MerchantorderdishesAdapter.ViewHolder>{
    private Context mcontext;
    private List<merchantpendingorders> merchantPendingOrderslist;

    public MerchantorderdishesAdapter(Context context, List<merchantpendingorders> merchantPendingOrderslist) {
        this.merchantPendingOrderslist = merchantPendingOrderslist;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.merchantorderdishes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final merchantpendingorders chefPendingOrders = merchantPendingOrderslist.get(position);
        holder.dishname.setText(chefPendingOrders.getDishName());
        holder.price.setText("Price: ₹ " + chefPendingOrders.getPrice());
        holder.quantity.setText("× " + chefPendingOrders.getDishQuantity());
        holder.totalprice.setText("Total: ₹ " + chefPendingOrders.getTotalPrice());


    }

    @Override
    public int getItemCount() {
        return merchantPendingOrderslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dishname, price, totalprice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dishname = itemView.findViewById(R.id.DN);
            price = itemView.findViewById(R.id.PR);
            totalprice = itemView.findViewById(R.id.TR);
            quantity = itemView.findViewById(R.id.QY);
        }
    }
}

