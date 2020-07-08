package lk.chanaka.dushmantha.groceryonline.Cart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.ItemDetails.Item;
import lk.chanaka.dushmantha.groceryonline.OrderItems.OrderItem;
import lk.chanaka.dushmantha.groceryonline.R;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {
    private LayoutInflater inflater;
    private List<Cart> cartItems;
    private String host, token;

    AdapterCart(Context ctx, List<Cart> cartItems, String host, String token){
        this.inflater = LayoutInflater.from(ctx);
        this.cartItems = cartItems;
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
        holder.itemTitle.setText(cartItems.get(position).getName());
        holder.itemDec.setText(cartItems.get(position).getDescription());
        holder.price.setText(cartItems.get(position).getTotal());
        String d = cartItems.get(position).getDiscount();
        if(!d.equals("null")){
            holder.discount.setText(cartItems.get(position).getDiscount());
        }
        holder.tvQuantity.setText(cartItems.get(position).getQuantity());
        String i = cartItems.get(position).getImage_url();
        //System.out.println(i);
        if(!i.equals("null")){
            Picasso.get().load(i).into(holder.coverImage);
        }
        holder.cartId = cartItems.get(position).getId();
        holder.itemId = cartItems.get(position).getItem_id();
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView itemTitle,itemDec,price,discount, tvQuantity;
        ImageView coverImage, deletebtn;
        String cartId, itemId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemDec = itemView.findViewById(R.id.itemDec);
            discount = itemView.findViewById(R.id.discount);
            price = itemView.findViewById(R.id.price);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            coverImage = itemView.findViewById(R.id.coverImage);
            deletebtn = itemView.findViewById(R.id.deletebtn);

            // handle onClick
            deletebtn.setVisibility(View.VISIBLE);
            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    cancelOrder(cartId);
                    /*int position = getAdapterPosition();
                    String id = groceryItems.get(position).getId();

                    Toast.makeText(v.getContext(), "Do Something With this Click" + id, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(inflater.getContext(), Item.class);
                    intent.putExtra("ID", id);
                    inflater.getContext().startActivity(intent);*/
                }

            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(inflater.getContext(), Item.class);
                    intent.putExtra("ID", itemId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    inflater.getContext().startActivity(intent);
                }
            });
        }
    }

    private void cancelOrder(String cartId) {
        CartPreseter cartPreseter = new CartPreseter(inflater.getContext());
        cartPreseter.cancelOrder(cartId);
    }
}