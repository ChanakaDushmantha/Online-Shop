package lk.chanaka.dushmantha.groceryonline.Items.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import lk.chanaka.dushmantha.groceryonline.Items.Adapter;
import lk.chanaka.dushmantha.groceryonline.Items.GroceryItem;
import lk.chanaka.dushmantha.groceryonline.Items.ItemsPresenter;
import lk.chanaka.dushmantha.groceryonline.Items.ItemsView;
import lk.chanaka.dushmantha.groceryonline.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends Fragment implements ItemsView{

    private RecyclerView recyclerView;
    private java.util.List<GroceryItem> groceryItems;
    private View shimmerItem;
    private ImageView emptyCart;
    private Adapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = root.findViewById(R.id.RVitems);
        shimmerItem = root.findViewById(R.id.shimmerItem);
        emptyCart = root.findViewById(R.id.emptycart);
        groceryItems = new ArrayList<>();

        ItemsPresenter itemsPresenter = new ItemsPresenter(getContext(), this);
        itemsPresenter.extractItems();
        return root;
    }

    @Override
    public void hideShimmer() {
        shimmerItem.setVisibility(View.GONE);
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
                //Log.d("cda", detailsObject.toString());
                GroceryItem groceryItem = new GroceryItem();
                groceryItem.setId(detailsObject.getString("id"));
                groceryItem.setName(detailsObject.getString("name"));
                groceryItem.setDescription(detailsObject.getString("description"));
                groceryItem.setPrice(detailsObject.getString("price"));
                groceryItem.setDiscount(detailsObject.getString("discount"));
                groceryItem.setImage_url(detailsObject.getString("image_url"));
                groceryItems.add(groceryItem);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Log.d("adp",groceryItems.get(0).getName());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new Adapter(getApplicationContext(),groceryItems);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);//Make sure you have this line of code.
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_view);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}