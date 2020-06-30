package lk.chanaka.dushmantha.groceryonline.ItemDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;
import lk.chanaka.dushmantha.groceryonline.ItemQuantity.QuantityActivity;


public class Item extends AppCompatActivity implements ItemView {

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    CardView cart, buy;
    ImageView itemThumb;
    TextView quantity, price, description, tag, value;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ProgressBar progressBar;
    JSONObject object;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        Intent intent = getIntent();
        String ItemId = intent.getStringExtra("ID");

        toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");

        appBarLayout = findViewById(R.id.appbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        progressBar = findViewById(R.id.progressBar);
        cart = findViewById(R.id.cart);
        buy = findViewById(R.id.buy);
        itemThumb = findViewById(R.id.itemThumb);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        tag = findViewById(R.id.tag);
        value = findViewById(R.id.value);


        setupActionBar();
        ItemPresenter presenter = new ItemPresenter(this, Item.this);
        presenter.getItemById(ItemId);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorWhite));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorWhite));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    void setupColorActionBarIcon(Drawable favoriteItemColor) {
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                if (toolbar.getNavigationIcon() != null)
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                favoriteItemColor.mutate().setColorFilter(getResources().getColor(R.color.colorPrimary),
                        PorterDuff.Mode.SRC_ATOP);

            } else {
                if (toolbar.getNavigationIcon() != null)
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                favoriteItemColor.mutate().setColorFilter(getResources().getColor(R.color.colorWhite),
                        PorterDuff.Mode.SRC_ATOP);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem favoriteItem = menu.findItem(R.id.favorite);
        Drawable favoriteItemColor = favoriteItem.getIcon();
        setupColorActionBarIcon(favoriteItemColor);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onErrorLoading(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setItem(JSONObject item) {
        //Log.d("TAG",item.toString());
        String txtname = "";
        String txtdescription = "";
        String txtprice = "";
        String txtquantity = "";
        String txtquantityType = "";
        JSONObject quantityType;
        String txtdiscount = "";
        String image_url = null;
        JSONObject item_category;
        String txtcatName = "";
        try {
            object = item;
            txtname = item.getString("name");
            txtdescription = item.getString("description");
            txtprice = item.getString("price");
            txtquantity = item.getString("quantity");
            quantityType = item.getJSONObject("quantity_type");
            txtquantityType = quantityType.getString("name");
            txtdiscount = item.getString("discount");
            image_url = item.getString("image_url");
            item_category = item.getJSONObject("item_category");
            txtcatName = item_category.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Picasso.get().load(image_url).into(itemThumb);
        collapsingToolbarLayout.setTitle(txtname);
        /*if((Integer.parseInt(txtquantity))<1){
            quantity.setText("Sold out");
            quantity.setTextColor(getResources().getColor(R.color.highlight));
        }else{
            quantity.setText(txtquantity);
        }*/
        //quantity.setText(txtquantity);
        price.setText("RS. "+txtprice);
        description.setText(txtdescription);

        tag.append("\n \u2022 Category -");
        value.append("\n " + txtcatName);
        tag.append("\n \u2022 Selling Type -");
        value.append("\n " + txtquantityType);

        if (!txtdiscount.equals("null")) {
            tag.append("\n \u2022 Discount -");
            value.append("\n RS." + txtdiscount);
        }
        setupActionBar();
    }

    public void buy(View view) {
        Intent i = new Intent(Item.this, QuantityActivity.class);
        i.putExtra("ITEM",object.toString());
        startActivity(i);
    }

    public void cart(View view) {
        Intent i = new Intent(Item.this, QuantityActivity.class);
        i.putExtra("ITEM",object.toString());
        i.putExtra("CART", true);
        startActivity(i);
    }
}
/*{
    "success": true,
    "message": "Item Found",
    "data": {
        "id": 1,
        "name": "sugar",
        "description": "white sugar",
        "price": "100.00",
        "quantity": "100750",
        "discount": null,
        "category_id": 1,
        "quantity_type_id": 2,
        "shop_id": 1,
        "image_url": "http://10.0.2.2:8000ADGGHGHF456asdfre",
        "created_at": "2020-03-05 00:00:00",
        "updated_at": "2020-03-05 00:00:00",
        "item_category": {
            "id": 1,
            "name": "Sugar"
        },
        "quantity_type": {
            "id": 2,
            "name": "loose",
            "unit1": "Kg",
            "unit2": "g"
        }
    }
}*/
