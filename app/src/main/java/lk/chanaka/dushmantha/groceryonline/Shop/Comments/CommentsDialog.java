package lk.chanaka.dushmantha.groceryonline.Shop.Comments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.R;

public class CommentsDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    RecyclerView recycler_comments;
    String shopId;
    private View viewConfirm;
    private String host;
    ArrayList<Comments> commentsList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            shopId = getArguments().getString("ShopId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        host = ((MyApp) getActivity().getApplication()).getServiceURL();
        commentsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewConfirm = inflater.inflate(R.layout.bottom_sheet_comments, container, false);
        recycler_comments = viewConfirm.findViewById(R.id.recycler_comments);
        initViews();
        extract();

        getDialog().setCanceledOnTouchOutside(true);
        return viewConfirm;

    }

    private void initViews() {
        recycler_comments.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,true);
        recycler_comments.setLayoutManager(layoutManager);
        recycler_comments.addItemDecoration(new DividerItemDecoration(getContext(),layoutManager.getOrientation()));
    }
    private void extract(){
        String URL = host+"/getShopById/"+shopId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray feedback = data.getJSONArray("feedback");

                            if(success.equals("true")){
                                //Toast.makeText(ShopActivity.this, "Shop Success!", Toast.LENGTH_SHORT).show();
                                //createList(data);
                                setModel(feedback);
                                //loading.setVisibility(View.GONE);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Response type error "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg = "Error";
                        if (error instanceof NoConnectionError) {
                            errorMsg = getString(R.string.noConnectionError);
                        } else if (error instanceof TimeoutError) {
                            errorMsg = getString(R.string.timeoutError);
                        } else if (error instanceof AuthFailureError) {
                            errorMsg = getString(R.string.authFailureError);
                        } else if (error instanceof ServerError) {
                            errorMsg = getString(R.string.serverError);
                        } else if (error instanceof NetworkError) {
                            errorMsg = getString(R.string.networkError);
                        } else if (error instanceof ParseError) {
                            errorMsg = getString(R.string.parseError);
                        }
                        Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void setModel(JSONArray data) {
        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject detailsObject = data.getJSONObject(i);

                Comments comment = new Comments();
                comment.setRating(Float.parseFloat(detailsObject.getString("feedback_rate")));
                comment.setComment(detailsObject.getString("feedback_comment"));
                comment.setTime(detailsObject.getString("created_date"));

                JSONObject user = detailsObject.getJSONObject("user");
                comment.setUserName(user.getString("name"));
                comment.setImage_url(user.getString("image_url"));
                
                commentsList.add(comment);

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error 3"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        setAdapet(commentsList);
    }

    private void setAdapet(ArrayList<Comments> commentsList) {
        CommentsAdapter adapter = new CommentsAdapter(getContext(), commentsList);
        recycler_comments.setAdapter(adapter);
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
