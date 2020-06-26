package lk.chanaka.dushmantha.groceryonline.OrderList;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.OrderItems.OrderItemsActivity;
import lk.chanaka.dushmantha.groceryonline.R;


public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {
    private LayoutInflater inflater;
    private java.util.List<OrderItem> orderItems;
    private String host, token;

    AdapterOrder(Context ctx, List<OrderItem> orderItems, String host, String token){
        this.inflater = LayoutInflater.from(ctx);
        this.orderItems = orderItems;
        this.host = host;
        this.token = token;
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
        ImageView coverImage, deletebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderId = itemView.findViewById(R.id.orderId);
            address = itemView.findViewById(R.id.address);
            date = itemView.findViewById(R.id.date);
            total = itemView.findViewById(R.id.total);
            status = itemView.findViewById(R.id.status);
            coverImage = itemView.findViewById(R.id.coverImage);
            deletebtn = itemView.findViewById(R.id.deletebtn);

            // handle onClick

            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderItems.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    cancelOrder(orderId.getText().toString());
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
                    inflater.getContext().startActivity(intent);
                }
            });
        }
    }

    private void cancelOrder(String orderId) {
        RequestQueue queue = Volley.newRequestQueue(inflater.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, host+"/cancelOrder/"+orderId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("true")){
                                Toast.makeText(inflater.getContext(), "Order cancel successfully", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(inflater.getContext(), "Register Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMsg = "Error";
                if (error instanceof NoConnectionError) {
                    errorMsg = inflater.getContext().getString(R.string.noConnectionError);
                } else if (error instanceof TimeoutError) {
                    errorMsg = inflater.getContext().getString(R.string.timeoutError);
                } else if (error instanceof AuthFailureError) {
                    errorMsg = inflater.getContext().getString(R.string.authFailureError);
                } else if (error instanceof ServerError) {
                    errorMsg = inflater.getContext().getString(R.string.serverError);
                } else if (error instanceof NetworkError) {
                    errorMsg = inflater.getContext().getString(R.string.networkError);
                } else if (error instanceof ParseError) {
                    errorMsg = inflater.getContext().getString(R.string.parseError);
                }
                Toast.makeText(inflater.getContext(), errorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                //System.out.println(token);
                return params;
            }
        };

        queue.add(stringRequest);
    }

}
