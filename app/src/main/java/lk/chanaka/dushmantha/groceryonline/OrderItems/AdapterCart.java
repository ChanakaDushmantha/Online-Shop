package lk.chanaka.dushmantha.groceryonline.OrderItems;

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
import lk.chanaka.dushmantha.groceryonline.OrderList.OrderItem;
import lk.chanaka.dushmantha.groceryonline.R;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {
    private LayoutInflater inflater;
    private List<CartItem> cartItems;
    private String host, token;

    AdapterCart(Context ctx, List<CartItem> cartItems, String host, String token){
        this.inflater = LayoutInflater.from(ctx);
        this.cartItems = cartItems;
        this.host = host;
        this.token = token;
    }

    @NonNull
    @Override
    public AdapterCart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_ordereditem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCart.ViewHolder holder, int position) {
        holder.itemTitle.setText(cartItems.get(position).getName());
        holder.itemDec.setText(cartItems.get(position).getDescription());
        holder.price.setText(cartItems.get(position).getPrice());
        String d = cartItems.get(position).getDiscount();
        if(!d.equals("null")){
            holder.discount.setText(cartItems.get(position).getDiscount());
        }
        holder.tvQuantity.setText(cartItems.get(position).getQuantity());
        Picasso.get().load(cartItems.get(position).getImage_url()).into(holder.coverImage);
        holder.itemId = cartItems.get(position).getItem_id();
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
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
