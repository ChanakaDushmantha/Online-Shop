package lk.chanaka.dushmantha.groceryonline.OrderList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;
import lk.chanaka.dushmantha.groceryonline.OrderItems.OrderItemsActivity;
import lk.chanaka.dushmantha.groceryonline.R;


public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {
    private LayoutInflater inflater;
    private java.util.List<OrderItem> orderItems;
    private Activity activity;
    private Context context;

    AdapterOrder(Context ctx, Activity activity, List<OrderItem> orderItems){
        this.inflater = LayoutInflater.from(ctx);
        this.orderItems = orderItems;
        this.context = ctx;
        this.activity = activity;
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
        holder.netTotal.setText(orderItems.get(position).getNet_total());
        holder.couponOff.setText(orderItems.get(position).getCoupon_off());
        holder.delCharge.setText(orderItems.get(position).getDelivery_charge());
        holder.status.setText(orderItems.get(position).getStatus());

        if (!orderItems.get(position).isFeedback()) {
            holder.feedback.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        TextView orderId, address, date, total, status, delCharge, couponOff, netTotal, feedback;
        ImageView coverImage, deletebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.orderId);
            address = itemView.findViewById(R.id.address);
            date = itemView.findViewById(R.id.time);
            total = itemView.findViewById(R.id.total);
            status = itemView.findViewById(R.id.status);
            delCharge = itemView.findViewById(R.id.delCharge);
            couponOff = itemView.findViewById(R.id.couponOff);
            netTotal = itemView.findViewById(R.id.netTotal);
            //coverImage = itemView.findViewById(R.id.coverImage);
            deletebtn = itemView.findViewById(R.id.deletebtn);
            feedback = itemView.findViewById(R.id.feedback);
            // handle onClick

            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    //cancelOrder(orderId.getText().toString());
                    OrderListPesenter orderListPesenter = new OrderListPesenter(inflater.getContext());
                    orderListPesenter.cancelOrder(orderId.getText().toString());
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
                    Intent intent = new Intent(inflater.getContext(), OrderItemsActivity.class);
                    intent.putExtra("ID", orderId.getText().toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    inflater.getContext().startActivity(intent);
                }
            });
            feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = ((MainActivity)activity).getSupportFragmentManager().beginTransaction();
                    Fragment prev = ((MainActivity)activity).getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {

                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);
                    BottomSheetDialogFragment dialogFragment = new FeedbackDailog();

                    Bundle args = new Bundle();
                    args.putString("OrderId", orderId.getText().toString());

                    dialogFragment.setArguments(args);
                    dialogFragment.show(ft, "dialog");
                    feedback.setVisibility(View.GONE);
                }
            });
        }
    }

}
