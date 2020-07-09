package lk.chanaka.dushmantha.groceryonline.OrderList;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import lk.chanaka.dushmantha.groceryonline.R;

public class FeedbackDailog  extends BottomSheetDialogFragment implements View.OnClickListener {


    private Button accept;
    private View viewConfirm;
    private String orderId;
    private RatingBar ratingBar;
    private TextInputEditText comment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            orderId = getArguments().getString("OrderId");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewConfirm = inflater.inflate(R.layout.dailog_feedback, container, false);
        accept= viewConfirm.findViewById(R.id.accept);
        ratingBar= viewConfirm.findViewById(R.id.rating);
        comment= viewConfirm.findViewById(R.id.comment);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    OrderListPesenter post = new OrderListPesenter(inflater.getContext());
                    post.addFeedback(
                            ratingBar.getRating(),
                            comment.getText().toString(),
                            orderId);

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
