package lk.chanaka.dushmantha.groceryonline.OrderList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lk.chanaka.dushmantha.groceryonline.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OrdersFragment extends Fragment implements OrderListView{

    private RecyclerView recyclerView;
    private ImageView emptyCart;
    private LinearLayout linearLayout;
    private ArrayList<OrderItem> orderItems;
    private AdapterOrder adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

        recyclerView = root.findViewById(R.id.OrderList);
        emptyCart = root.findViewById(R.id.emptycart);
        linearLayout = root.findViewById(R.id.lyCart);

        orderItems = new ArrayList<>();
        OrderListPesenter orderListPesenter = new OrderListPesenter(getContext(), this);
        orderListPesenter.extractItems();

        return root;
    }

    @Override
    public void showEmpty() {
        emptyCart.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAdaptor(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject detailsObject = data.getJSONObject(i);

                OrderItem orderItem = new OrderItem();
                orderItem.setId(detailsObject.getString("id").toString());
                orderItem.setStatus(detailsObject.getString("status").toString());
                orderItem.setDelivery_address(detailsObject.getString("delivery_address".toString()));
                orderItem.setTotal_amount(detailsObject.getString("gross_amount").toString());
                orderItem.setAt(detailsObject.getString("created_at".toString()));
                orderItem.setDelivery_charge(detailsObject.getString("delivery_charge"));
                orderItem.setCoupon_off(detailsObject.getString("coupon_off"));
                orderItem.setNet_total(detailsObject.getString("total_amount"));
                orderItem.setFeedback(detailsObject.getBoolean("feedback"));
                orderItems.add(orderItem);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterOrder(getApplicationContext(), getActivity(),orderItems);
        recyclerView.setAdapter(adapter);
    }
}