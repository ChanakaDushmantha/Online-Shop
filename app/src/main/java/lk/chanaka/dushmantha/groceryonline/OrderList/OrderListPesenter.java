package lk.chanaka.dushmantha.groceryonline.OrderList;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class OrderListPesenter {
    private String token;
    private Context context;
    private String host;
    SessionManager sessionManager;
    private OrderListView view;

    OrderListPesenter(Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);
        sessionManager.checkLogin();
        token = sessionManager.getToken();
        host = ((MyApp) context.getApplicationContext()).getServiceURL();
    }

    public OrderListPesenter(Context context, OrderListView view) {
        this.context = context;
        this.view = view;
        sessionManager = new SessionManager(context);
        sessionManager.checkLogin();
        token = sessionManager.getToken();
        host = ((MyApp) context.getApplicationContext()).getServiceURL();
    }

    public void cancelOrder(String orderId) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, host+"/cancelOrder/"+orderId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("true")){
                                Toast.makeText(context, "Order cancel successfully", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "RegisterActivity Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMsg = "Error";
                if (error instanceof NoConnectionError) {
                    errorMsg = context.getString(R.string.noConnectionError);
                } else if (error instanceof TimeoutError) {
                    errorMsg = context.getString(R.string.timeoutError);
                } else if (error instanceof AuthFailureError) {
                    errorMsg = context.getString(R.string.authFailureError);
                } else if (error instanceof ServerError) {
                    errorMsg = context.getString(R.string.serverError);
                } else if (error instanceof NetworkError) {
                    errorMsg = context.getString(R.string.networkError);
                } else if (error instanceof ParseError) {
                    errorMsg = context.getString(R.string.parseError);
                }
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                //System.out.println(token);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    public void extractItems() {
        if (sessionManager.isLogin()) {
            String shopid = sessionManager.getShopId();
            String URL = host+"/getAllOrders/"+shopid;
            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                JSONArray data = jsonObject.getJSONArray("data");

                                if(success.equals("true")){
                                    if(data.length()==0){
                                        view.showEmpty();
                                        Toast.makeText(context, "Order list Empty!", Toast.LENGTH_LONG).show();
                                    }else{
                                        view.setAdaptor(data);
                                    }
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                            /*recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            adapter = new AdapterOrder(OrdersActivity.this,orderItems, host, token);
                            recyclerView.setAdapter(adapter);*/

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMsg = "Error";
                    if (error instanceof NoConnectionError) {
                        errorMsg = context.getString(R.string.noConnectionError);
                    } else if (error instanceof TimeoutError) {
                        errorMsg = context.getString(R.string.timeoutError);
                    } else if (error instanceof AuthFailureError) {
                        errorMsg = context.getString(R.string.authFailureError);
                    } else if (error instanceof ServerError) {
                        errorMsg = context.getString(R.string.serverError);
                    } else if (error instanceof NetworkError) {
                        errorMsg = context.getString(R.string.networkError);
                    } else if (error instanceof ParseError) {
                        errorMsg = context.getString(R.string.parseError);
                    }
                    Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", "Bearer " + token);
                    //System.out.println(token);
                    return params;
                }
            };

            queue.add(stringRequest);
        }

    }

    void addFeedback(double feedback_rate, String feedback_comment, String orderId){
        String URL = host+"/addFeedback/"+orderId;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");

                            if(success.equals("true")){
                                Toast.makeText(context, "Thank you for feedback!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg = "Error";
                        if (error instanceof NoConnectionError) {
                            errorMsg = context.getString(R.string.noConnectionError);
                        } else if (error instanceof TimeoutError) {
                            errorMsg = context.getString(R.string.timeoutError);
                        } else if (error instanceof AuthFailureError) {
                            errorMsg = context.getString(R.string.authFailureError);
                        } else if (error instanceof ServerError) {
                            errorMsg = context.getString(R.string.serverError);
                        } else if (error instanceof NetworkError) {
                            errorMsg = context.getString(R.string.networkError);
                        } else if (error instanceof ParseError) {
                            errorMsg = context.getString(R.string.parseError);
                        }
                        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + token);
                //System.out.println(token);
                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("feedback_rate", String.valueOf(feedback_rate));
                params.put("feedback_comment", feedback_comment);
/*                System.out.println(String.valueOf(feedback_rate));
                System.out.println(feedback_comment);*/
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
