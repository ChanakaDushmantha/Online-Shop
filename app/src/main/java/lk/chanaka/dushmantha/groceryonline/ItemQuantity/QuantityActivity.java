package lk.chanaka.dushmantha.groceryonline.ItemQuantity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class QuantityActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    EditText address, qty1, qty2;
    JSONObject item;
    TextView itemTitle, itemDec, available, availableNm, priceTV, discountTV, totalTV, unit1, unit2;
    ImageView coverImage;
    double price, discount, total;
    NumberPicker numberPicker;
    RadioButton myAddress;
    private String id = "";
    private boolean type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);
        
        radioGroup = findViewById(R.id.radioGroup);
        address = findViewById(R.id.address);
        itemTitle = findViewById(R.id.itemTitle);
        itemDec = findViewById(R.id.itemDec);
        coverImage = findViewById(R.id.coverImage);
        available = findViewById(R.id.available);
        availableNm = findViewById(R.id.availableNm);
        priceTV = findViewById(R.id.price);
        discountTV = findViewById(R.id.discount);
        totalTV = findViewById(R.id.total);
        qty1 = findViewById(R.id.quantity1);
        qty2 = findViewById(R.id.quantity2);
        unit1 = findViewById(R.id.unit1);
        unit2 = findViewById(R.id.unit2);
        myAddress = findViewById(R.id.radio2);
        numberPicker = findViewById(R.id.number_picker);

        SetToolbar();
        setItem();

        SessionManager sessionManager = new SessionManager(this);
        String ads = sessionManager.getAddress();
        myAddress.setText(ads);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checked = findViewById(group.getCheckedRadioButtonId());
            String checkedtxt = checked.getText().toString();

            if(checkedtxt.equals("Custom")){
                address.setVisibility(View.VISIBLE);
            }
            else {
                address.setVisibility(View.GONE);
            }
        });

       numberPicker.setValueChangedListener(new ValueChangedListener() {
           @Override
           public void valueChanged(int value, ActionEnum action) {
               total = (price-discount)*value;
               String t = String.valueOf(total);
               totalTV.setText(t);
           }
       });
       
    }

    private void SetToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
    }

    private void setItem(){

        String Item = this.getIntent().getStringExtra("ITEM");
        boolean cart = this.getIntent().getBooleanExtra("CART", false);
        if(cart){
            cartApply();
        }
        try {
            item = new JSONObject(Item);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String txtname = "";
        String txtdescription = "";
        String txtprice = "";
        String txtquantity = "";
        String txtdiscount = "";
        String txtquantityType = "";
        String txtquantityId = "";
        JSONObject quantityType;
        String image_url = null;
        try {
            id = item.getString("id");
            txtname = item.getString("name");
            txtdescription = item.getString("description");
            txtprice = item.getString("price");
            txtquantity = item.getString("quantity");
            quantityType = item.getJSONObject("quantity_type");
            txtquantityType = quantityType.getString("name");
            txtquantityId = quantityType.getString("id");
            txtdiscount = item.getString("discount");
            image_url = item.getString("image_url");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*if((Integer.parseInt(txtquantity))<1){
            availableNm.setText("Sold out");
            availableNm.setTextColor(getResources().getColor(R.color.highlight));
        }else{
            available.setText(txtquantity);
        }*/

        itemTitle.setText(txtname);
        itemDec.setText(txtdescription);
        available.setText(txtquantity); //invisible textview
        priceTV.setText(txtprice);
        if (!txtdiscount.equals("null")) {
            discountTV.setText(txtdiscount);
            discount = Double.parseDouble(txtdiscount);
        }
        else {
            discountTV.setText("0.00");
            discount = 0.00;
        }
        assert image_url != null;
        if(!image_url.equals("null")){
            Picasso.get().load(image_url).into(coverImage);
        }
        price = Double.parseDouble(txtprice);
        total = price-discount;
        String t = String.valueOf(total);
        totalTV.setText(t);



        if(txtquantityType.equals("piece")){
            numberPicker.setVisibility(View.VISIBLE);

            if((Integer.parseInt(txtquantity))<1){
                numberPicker.setActionEnabled(ActionEnum.INCREMENT, false);
                numberPicker.setActionEnabled(ActionEnum.DECREMENT, false);
                numberPicker.setValue(0);
            }else{
                //numberPicker.setMax(Integer.parseInt(txtquantity)); without check availability
                numberPicker.setValue(1);
                numberPicker.setMin(1);
            }
            type = true;
        }
        else {
            QuantityPresenter presenter = new QuantityPresenter( this,QuantityActivity.this);
            presenter.getUnitbyId(txtquantityId);
            qty1.setVisibility(View.VISIBLE);
            qty2.setVisibility(View.VISIBLE);
            type = false;
        }
    }

    private void cartApply() {
        findViewById(R.id.placeCart).setVisibility(View.VISIBLE);
        findViewById(R.id.placeOrder).setVisibility(View.GONE);
        findViewById(R.id.delInfo).setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void placeOrder(View view) {
        String qty01 = "";
        String qty02 = "";
        if(type){
            qty01 = String.valueOf(numberPicker.getValue());
        }
        else {
            qty01 = qty1.getText().toString();
            qty02 = qty2.getText().toString();
        }

        RadioButton checked = findViewById(radioGroup.getCheckedRadioButtonId());
        String checkedtxt = checked.getText().toString();
        String ads;
        if(checkedtxt.equals("Custom")){
            ads = address.getText().toString();
        }else{
            ads = checkedtxt;
        }
        if(!type) {
            if(!qty01.isEmpty()||!qty02.isEmpty()){
                QuantityPresenter post = new QuantityPresenter(this,QuantityActivity.this);
                post.postOrderbyId(id, qty01, qty02, ads);
            }
            else{
                qty1.setError("Please value");
            }
        }
        else{
            QuantityPresenter post = new QuantityPresenter(this,QuantityActivity.this);
            post.postOrderbyId(id, qty01, qty02, ads);
        }

    }

    public void cart(View view) {
        String qty01 = "";
        String qty02 = "";
        if(type){
            qty01 = String.valueOf(numberPicker.getValue());
        }
        else {
            qty01 = qty1.getText().toString();
            qty02 = qty2.getText().toString();
        }
        QuantityPresenter post = new QuantityPresenter(this,QuantityActivity.this);
        if(!type) {
            if(!qty01.isEmpty()||!qty02.isEmpty()){

                post.addToCart(id, qty01, qty02);
            }
            else{
                qty1.setError("Please value");
            }
        }
        else{
            post.addToCart(id, qty01, qty02);
        }

    }
}