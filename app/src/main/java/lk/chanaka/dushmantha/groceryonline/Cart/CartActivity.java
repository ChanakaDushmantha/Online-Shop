package lk.chanaka.dushmantha.groceryonline.Cart;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import lk.chanaka.dushmantha.groceryonline.Items.List;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.OrderItems.OrderItem;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;


public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private String host, token, URL;
    SessionManager sessionManager;
    ArrayList<OrderItem> orderItems;
    private ImageView emptycart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        SetToolbar();
        recyclerView = findViewById(R.id.OrderList);
        emptycart = findViewById(R.id.emptycart);

        host = ((MyApp) this.getApplication()).getServiceURL();

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        token = sessionManager.getToken();
        //String shopid = sessionManager.getShopId();

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("ID");

        URL = host+"/getOrderById/"+orderId;
        orderItems = new ArrayList<>();
        //extractItems();
    }
    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //onBackPressed();
                Intent intent = new Intent(this, List.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}