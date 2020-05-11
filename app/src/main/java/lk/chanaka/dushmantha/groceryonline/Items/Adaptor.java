package lk.chanaka.dushmantha.groceryonline.Items;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lk.chanaka.dushmantha.groceryonline.ItemDetails.Item;
import lk.chanaka.dushmantha.groceryonline.R;

class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {
    LayoutInflater inflater;
    java.util.List<GroceryItem> groceryItems;
    private List<GroceryItem> getGroceryItemListFiltered;

    public Adapter(Context ctx, List<GroceryItem> groceryItems){
        this.inflater = LayoutInflater.from(ctx);
        this.groceryItems = groceryItems;
        this.getGroceryItemListFiltered = groceryItems;
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

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = getGroceryItemListFiltered.size();
                    filterResults.values = getGroceryItemListFiltered;

                }else{
                    String searchChr = charSequence.toString().toLowerCase();

                    List<GroceryItem> resultData = new ArrayList<>();

                    for(GroceryItem Grocery_Item: getGroceryItemListFiltered){
                        if(Grocery_Item.getName().toLowerCase().contains(searchChr)){
                            resultData.add(Grocery_Item);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                groceryItems = (List<GroceryItem>) filterResults.values;
                notifyDataSetChanged();

            }
        };
        return filter;
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
                    int position = getAdapterPosition();
                    String id = groceryItems.get(position).getId();

                    Toast.makeText(v.getContext(), "Do Something With this Click" + id, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(inflater.getContext(), Item.class);
                    intent.putExtra("ID", id);
                    inflater.getContext().startActivity(intent);
                }
            });
        }
    }
}
