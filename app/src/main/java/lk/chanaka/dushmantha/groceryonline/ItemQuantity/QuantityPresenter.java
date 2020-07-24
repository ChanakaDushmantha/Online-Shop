package lk.chanaka.dushmantha.groceryonline.ItemQuantity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.OrderList.OrdersActivity;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class QuantityPresenter {
    private QuantityActivity view;
    private String token;
    private Context context;
    private String host;
    SessionManager sessionManager;


    QuantityPresenter(QuantityActivity view, Context context) {
        this.view = view;
        this.context = context;
        sessionManager = new SessionManager(context);
        token = sessionManager.getToken();
        host = ((MyApp) context.getApplicationContext()).getServiceURL();
    }

    QuantityPresenter(Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);
        token = sessionManager.getToken();
        host = ((MyApp) context.getApplicationContext()).getServiceURL();
    }
    void getUnitbyId(String typeId){
        String URL = host+"/getQuantityType/"+typeId;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            JSONObject data = jsonObject.getJSONObject("data");
                            String unit1 = data.getString("unit1");
                            String unit2 = data.getString("unit2");

                            if(success.equals("true")){
                                view.unit1.setText(unit1);
                                view.unit2.setText(unit2);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
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
                return params;
            }
        };
        queue.add(stringRequest);
    }

    void postOrderbyId(String ItemId,
                              String quantity1, String quantity2,
                              String address, String mobile, String coupon_code){


        String URL = host+"/addOneItemToOrder/"+sessionManager.getShopId();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");

                            if(success.equals("true")){
                                Toast.makeText(context, "Order Success!", Toast.LENGTH_SHORT).show();
                                sessionManager.addDetails(address, mobile);

                                Intent intent = new Intent(context, OrdersActivity.class);
                                context.startActivity(intent);
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
                params.put("item_id", ItemId);
                params.put("quantity1", quantity1);
                params.put("quantity2", quantity2);
                params.put("delivery_address", address);
                params.put("contact_no", mobile);
                params.put("coupon_code", coupon_code);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    void addToCart(String ItemId,
                   String quantity1, String quantity2){
        String URL = host+"/addToCart";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");

                            if(success.equals("true")){
                                Toast.makeText(context, "Add to Cart Successfully!", Toast.LENGTH_SHORT).show();

                                /*Intent intent = new Intent(context, OrdersActivity.class);
                                context.startActivity(intent);
                                Activity activity = (Activity) context;
                                activity.finish();*/
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
                params.put("item_id", ItemId);
                params.put("quantity1", quantity1);
                params.put("quantity2", quantity2);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }


    void calculationOneTime(String ItemId,
                       String quantity1, String quantity2,
                       String coupon, String address,String mobile){


        String URL = host+"/calculationOneTime/"+sessionManager.getShopId();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");
                            JSONObject data =  jsonObject.getJSONObject("data");

                            if(success.equals("true")){
                                //Toast.makeText(context, "Order Success!", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = view.getSupportFragmentManager().beginTransaction();
                                Fragment prev = view.getSupportFragmentManager().findFragmentByTag("dialog");
                                if (prev != null) {

                                    ft.remove(prev);
                                }
                                ft.addToBackStack(null);
                                BottomSheetDialogFragment dialogFragment = new ConfirmCharges();

                                Bundle args = new Bundle();
                                args.putString("Total", data.getString("items_total"));
                                args.putString("Delivery_Charge", data.getString("delivery_charge"));
                                args.putString("Coupon_OFF", data.getString("coupon_off"));
                                args.putString("Net_Total", data.getString("net_total"));

                                args.putString("ItemId",ItemId);
                                args.putString("Quantity1", quantity1);
                                args.putString("Quantity2", quantity2);
                                args.putString("Address", address);
                                args.putString("Mobile", mobile);
                                args.putString("Coupon", coupon);

                                dialogFragment.setArguments(args);
                                dialogFragment.show(ft, "dialog");
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
                params.put("item_id", ItemId);
                params.put("quantity1", quantity1);
                params.put("quantity2", quantity2);
                params.put("coupon_code", coupon);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    void calculationCart(String coupon, String address, String mobile){


        String URL = host+"/calculationCart/"+sessionManager.getShopId();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success =  jsonObject.getString("success");
                            JSONObject data =  jsonObject.getJSONObject("data");

                            if(success.equals("true")){
                                //Toast.makeText(context, "Order Success!", Toast.LENGTH_SHORT).show();

                                FragmentTransaction ft = view.getSupportFragmentManager().beginTransaction();
                                Fragment prev = view.getSupportFragmentManager().findFragmentByTag("dialog");
                                if (prev != null) {

                                    ft.remove(prev);
                                }
                                ft.addToBackStack(null);
                                BottomSheetDialogFragment dialogFragment = new ConfirmCharges();

                                Bundle args = new Bundle();
                                args.putString("Total", data.getString("cart_total"));
                                args.putString("Delivery_Charge", data.getString("delivery_charge"));
                                args.putString("Coupon_OFF", data.getString("coupon_off"));
                                args.putString("Net_Total", data.getString("net_total"));

                                args.putString("Coupon", coupon);
                                args.putString("Address", address);
                                args.putString("Mobile", mobile);
                                args.putString("Status", "CART_ORDER");

                                dialogFragment.setArguments(args);
                                dialogFragment.show(ft, "dialog");
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
                params.put("coupon_code", coupon);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
