package lk.chanaka.dushmantha.groceryonline.Items;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import lk.chanaka.dushmantha.groceryonline.R;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    java.util.List<GroceryItem> groceryItems;

    public Adapter(Context ctx, List<GroceryItem> groceryItems){
        this.inflater = LayoutInflater.from(ctx);
        this.groceryItems = groceryItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // bind the data
        holder.itemTitle.setText(groceryItems.get(position).getName());
        holder.itemDec.setText(groceryItems.get(position).getDescription());
        String prc =  groceryItems.get(position).getPrice();
        holder.price.setText("Rs. "+prc);
        holder.discount.setText(getDiscount(position));
        Picasso.get().load(groceryItems.get(position).getImage_url()).into(holder.coverImage);
    }

    private String getDiscount(int position) {
        String discount = groceryItems.get(position).getDiscount();
        if(!discount.equals("null")){
            return "Discount Rs."+discount;
        }
        else {
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView itemTitle,itemDec,price,discount;
        ImageView coverImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.itemTitle1);
            itemDec = itemView.findViewById(R.id.itemDec);
            discount = itemView.findViewById(R.id.discount);
            price = itemView.findViewById(R.id.price);
            coverImage = itemView.findViewById(R.id.coverImage);

            // handle onClick

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
