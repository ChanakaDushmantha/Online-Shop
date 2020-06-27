package lk.chanaka.dushmantha.groceryonline.Cart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import lk.chanaka.dushmantha.groceryonline.ItemDetails.Item;
import lk.chanaka.dushmantha.groceryonline.OrderItems.OrderItem;
import lk.chanaka.dushmantha.groceryonline.R;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {
    private LayoutInflater inflater;
    private List<OrderItem> orderItems;
    private String host, token;

    AdapterCart(Context ctx, List<OrderItem> orderItems, String host, String token){
        this.inflater = LayoutInflater.from(ctx);
        this.orderItems = orderItems;
        this.host = host;
        this.token = token;
    }

    @NonNull
    @Override
    public AdapterCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_ordereditem,parent,false);
        return new AdapterCart.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCart.ViewHolder holder, int position) {
        /*holder.itemTitle.setText(orderItems.get(position).getName());
        holder.itemDec.setText(orderItems.get(position).getDescription());
        holder.price.setText(orderItems.get(position).getPrice());
        String d = orderItems.get(position).getDiscount();
        if(!d.equals("null")){
            holder.discount.setText(orderItems.get(position).getDiscount());
        }
        holder.tvQuantity.setText(orderItems.get(position).getQuantity());
        Picasso.get().load(orderItems.get(position).getImage_url()).into(holder.coverImage);
        holder.itemId = orderItems.get(position).getItem_id();*/
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView itemTitle,itemDec,price,discount, tvQuantity;
        ImageView coverImage;
        String itemId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemDec = itemView.findViewById(R.id.itemDec);
            discount = itemView.findViewById(R.id.discount);
            price = itemView.findViewById(R.id.price);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            coverImage = itemView.findViewById(R.id.coverImage);

            // handle onClick

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(inflater.getContext(), Item.class);
                    intent.putExtra("ID", itemId);
                    inflater.getContext().startActivity(intent);
                }
            });
        }
    }
}