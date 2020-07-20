package lk.chanaka.dushmantha.groceryonline.ItemQuantity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import lk.chanaka.dushmantha.groceryonline.Cart.CartPresenter;
import lk.chanaka.dushmantha.groceryonline.R;

public class ConfirmCharges extends BottomSheetDialogFragment implements View.OnClickListener{

    private Button accept, cancel, ok;
    private TextView tvTotal, tvDelCharge, tvCoupenOff, tvNetTotal;
    View viewConfirm;
    private String total, delivery_charge, coupon_off, net_total;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            total = getArguments().getString("Total");
            delivery_charge = getArguments().getString("Delivery_Charge");
            coupon_off = getArguments().getString("Coupon_OFF");
            net_total = getArguments().getString("Net_Total");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewConfirm = inflater.inflate(R.layout.custom_confirm_charges, container, false);
        accept= viewConfirm.findViewById(R.id.accept);
        cancel= viewConfirm.findViewById(R.id.cancel);
        //ok= viewConfirm.findViewById(R.id.ok);
        tvTotal = viewConfirm.findViewById(R.id.tvTotal);
        tvDelCharge = viewConfirm.findViewById(R.id.tvDelCharge);
        tvCoupenOff = viewConfirm.findViewById(R.id.tvCoupenOff);
        tvNetTotal = viewConfirm.findViewById(R.id.tvNetTotal);

        tvTotal.setText(total);
        tvDelCharge.setText(delivery_charge);
        tvCoupenOff.setText(coupon_off);
        tvNetTotal.setText(net_total);

        try {
            if(getArguments().getString("Status").equals("CART")){
                accept.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                //ok.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = getArguments().getString("Status","false");
                if(status.equals("CART_ORDER")){
                    CartPresenter cartPresenter = new CartPresenter(inflater.getContext());
                    cartPresenter.OrderPost(
                            getArguments().getString("Coupon"),
                            getArguments().getString("Address"),
                            getArguments().getString("Mobile"));
                }
                else {
                    QuantityPresenter post = new QuantityPresenter(inflater.getContext());
                    post.postOrderbyId(
                            getArguments().getString("ItemId"),
                            getArguments().getString("Quantity1"),
                            getArguments().getString("Quantity2"),
                            getArguments().getString("Address"),
                            getArguments().getString("Mobile"));
                }

                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        /*ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });*/
        getDialog().setCanceledOnTouchOutside(true);
        return viewConfirm;

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismiss();
    }

    @Override
    public void onClick(View v) {

    }

}
