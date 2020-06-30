package lk.chanaka.dushmantha.groceryonline.Cart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import lk.chanaka.dushmantha.groceryonline.R;


public class ConfirmAddress extends DialogFragment implements
        View.OnClickListener {

    private String Address;
    private Button SelectBtn;
    RadioButton myAddress;
    private Button ChangeBtn;
    RadioGroup radioGroup;
    EditText address;
    View viewConfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Address = getArguments().getString("address");

    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewConfirm = inflater.inflate(R.layout.custom_confirm_address, container, false);
        SelectBtn= viewConfirm.findViewById(R.id.Select);
        ChangeBtn= viewConfirm.findViewById(R.id.Change);
        myAddress = viewConfirm.findViewById(R.id.radio2);
        radioGroup = viewConfirm.findViewById(R.id.radioGroup);
        address = viewConfirm.findViewById(R.id.address);
        myAddress.setText(Address);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checked = viewConfirm.findViewById(group.getCheckedRadioButtonId());
            String checkedtxt = checked.getText().toString();

            if(checkedtxt.equals("Custom")){
                address.setVisibility(View.VISIBLE);
            }
            else {
                address.setVisibility(View.GONE);
            }
        });


        SelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton checked = viewConfirm.findViewById(radioGroup.getCheckedRadioButtonId());
                String checkedtxt = checked.getText().toString();
                String ads;
                if(checkedtxt.equals("Custom")){
                    ads = address.getText().toString();
                }else{
                    ads = checkedtxt;
                }

                /*Intent intent = new Intent(inflater.getContext(), OrdersActivity.class);
                intent.putExtra("Latitude", ads);
                Activity activity = getActivity();
                activity.setResult(RESULT_OK, intent);
                activity.finish();*/
                CartPreseter cartPreseter = new CartPreseter(inflater.getContext());
                cartPreseter.OrderPost(ads);
                dismiss();
            }
        });
        ChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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
