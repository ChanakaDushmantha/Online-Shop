package lk.chanaka.dushmantha.groceryonline.Cart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lk.chanaka.dushmantha.groceryonline.ItemQuantity.ConfirmCharges;
import lk.chanaka.dushmantha.groceryonline.ItemQuantity.QuantityActivity;
import lk.chanaka.dushmantha.groceryonline.Items.ItemsView;
import lk.chanaka.dushmantha.groceryonline.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CartFragment extends Fragment implements CartView {

    private RecyclerView recyclerView;
    private ImageView emptyCart;
    private LinearLayout linearLayout;
    private ArrayList<Cart> cartItems;
    private AdapterCart adapter;
    private JSONObject data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = root.findViewById(R.id.OrderList);
        emptyCart = root.findViewById(R.id.emptycart);
        linearLayout = root.findViewById(R.id.lyCart);

        cartItems = new ArrayList<>();

        CartPresenter cartPresenter = new CartPresenter(getContext(), this);
        cartPresenter.extractItems();

        root.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), QuantityActivity.class);
                i.putExtra("CART_ORDER", true);
                startActivity(i);
            }
        });
        root.findViewById(R.id.details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {

                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                BottomSheetDialogFragment dialogFragment = new ConfirmCharges();

                Bundle args = new Bundle();
                try {
                    args.putString("Total", data.getString("cart_total"));
                    args.putString("Delivery_Charge", data.getString("delivery_charge"));
                    args.putString("Net_Total", data.getString("net_total"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                args.putString("Coupon_OFF","0.00" );
                args.putString("Status", "CART");

                dialogFragment.setArguments(args);
                dialogFragment.show(ft, "dialog");
            }
        });
        return root;
    }

    @Override
    public void showEmpty() {
        emptyCart.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAdaptor(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject detailsObject = data.getJSONObject(i);
                Log.d("mxd",detailsObject.toString());
                Cart cart = new Cart();
                cart.setId(detailsObject.getString("id").toString());

                cart.setQuantity(detailsObject.getString("quantity").toString());

                JSONObject item = detailsObject.getJSONObject("item");
                cart.setItem_id(item.getString("id").toString());
                cart.setName(item.getString("name".toString()));
                cart.setDescription(item.getString("description").toString());
                cart.setImage_url(item.getString("image_url".toString()));
                cart.setPrice(item.getString("price").toString());
                cart.setDiscount(item.getString("discount".toString()));
                cart.setTotal(item.getString("total").toString());
                cartItems.add(cart);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new AdapterCart(getApplicationContext(),cartItems);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void parseData(JSONObject data) {
        this.data = data;
    }
}