package lk.chanaka.dushmantha.groceryonline.OrderList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lk.chanaka.dushmantha.groceryonline.R;

public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {
    LayoutInflater inflater;
    java.util.List<OrderItem> orderItems;

    public AdapterOrder(Context ctx, List<OrderItem> orderItems){
        this.inflater = LayoutInflater.from(ctx);
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public AdapterOrder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_orderitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterOrder.ViewHolder holder, int position) {
        holder.orderId.setText(orderItems.get(position).getId());
        holder.address.setText(orderItems.get(position).getDelivery_address());
        holder.date.setText(orderItems.get(position).getAt());
        holder.total.setText(orderItems.get(position).getTotal_amount());

        String status = orderItems.get(position).getStatus();
        holder.status.setText(status);
        switch (status){
            case "pending":{
                holder.coverImage.setImageResource(R.drawable.ic_action_pending);
                holder.status.setTextColor(inflater.getContext().getResources().getColor(R.color.pending));
                break;}
            case "received":{
                holder.coverImage.setImageResource(R.drawable.ic_action_received);
                holder.status.setTextColor(inflater.getContext().getResources().getColor(R.color.received));
                break;}
            case "completed":{
                holder.coverImage.setImageResource(R.drawable.ic_action_completed);
                holder.status.setTextColor(inflater.getContext().getResources().getColor(R.color.completed));
                break;}
            default: {
                holder.coverImage.setImageResource(R.drawable.ic_action_image);
                break;}
        }
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }
    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView orderId, address, date, total, status;
        ImageView coverImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.orderId);
            address = itemView.findViewById(R.id.address);
            date = itemView.findViewById(R.id.date);
            total = itemView.findViewById(R.id.total);
            status = itemView.findViewById(R.id.status);
            coverImage = itemView.findViewById(R.id.coverImage);

            // handle onClick

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*int position = getAdapterPosition();
                    String id = groceryItems.get(position).getId();

                    //Toast.makeText(v.getContext(), "Do Something With this Click" + id, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(inflater.getContext(), Item.class);
                    intent.putExtra("ID", id);
                    inflater.getContext().startActivity(intent);*/
                }
            });
        }
    }
}
