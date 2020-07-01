package lk.chanaka.dushmantha.groceryonline.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import lk.chanaka.dushmantha.groceryonline.Items.MainActivity;
import lk.chanaka.dushmantha.groceryonline.MyApp;
import lk.chanaka.dushmantha.groceryonline.NetworkConnection;
import lk.chanaka.dushmantha.groceryonline.R;
import lk.chanaka.dushmantha.groceryonline.SessionManager;

public class ProfilePicture extends AppCompatActivity {

    private CircleImageView civProfile;
    private CardView btnNext;
    private String URL;
    private ProgressDialog progressDialog;
    SessionManager sessionManager;
    private String token;
    private String host;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        host = ((MyApp) this.getApplication()).getServiceURL();
        URL = host+"/updateImage";

        sessionManager = new SessionManager(this);
        token = sessionManager.getToken();

        Intent i = getIntent();
        update = i.getBooleanExtra("UPDATE", false);
        if(update){
            extractData();
        }


        ImageButton ibPick = findViewById(R.id.btn_pick);
        civProfile = findViewById(R.id.profile_image);
        btnNext = findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(ProfilePicture.this);
        progressDialog.setMessage("Uploading Image Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        ibPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(new NetworkConnection(ProfilePicture.this).isNetworkConnected())) {
                    Toast.makeText(ProfilePicture.this, "Internet Connection Error", Toast.LENGTH_LONG).show();
                }
                else{
                    Dexter.withActivity(ProfilePicture.this)
                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    // start picker to get image for cropping and then use the image in cropping activity
                                    CropImage.activity()
                                            .setGuidelines(CropImageView.Guidelines.ON)
                                            .start(ProfilePicture.this);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                    if (permissionDeniedResponse.isPermanentlyDenied()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePicture.this);
                                        builder.setTitle("Permission Required")
                                                .setMessage("Permission to access your device storage is required to pick profile image. Please go to settings to enable permission to access storage")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent();
                                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                        startActivityForResult(intent, 51);
                                                    }
                                                })
                                                .setNegativeButton("Cancel", null)
                                                .show();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            })
                            .check();
                }
            }
        });
    }

    private void extractData() {
        String URLuserProofile = host + "/userProfile";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLuserProofile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONObject data = jsonObject.getJSONObject("data");

                            if(success.equals("true")){
                                setData(data);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfilePicture.this, "Register Error 1 ! "+e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
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
                Toast.makeText(ProfilePicture.this, errorMsg, Toast.LENGTH_LONG).show();
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

    private void setData(JSONObject data) {
        try {
            String image = data.getString("image_url");
            if(!image.equals("null")){
                Picasso.get().load(image).into(civProfile);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                civProfile.setImageURI(resultUri);
                btnNext.setCardBackgroundColor(0xFFD81B60);
                //System.out.println(resultUri);
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File imageFile = new File(resultUri.getPath());
                        progressDialog.show();
                        AndroidNetworking.upload(URL)
                                .addMultipartFile("image",imageFile)
                                .addHeaders("Authorization", "Bearer "+token)
                                //.addMultipartParameter("key","value")
                                .setTag("profile pic")
                                .setPriority(Priority.HIGH)
                                .build()
                                .setUploadProgressListener(new UploadProgressListener() {
                                    @Override
                                    public void onProgress(long bytesUploaded, long totalBytes) {
                                        // do anything with progress
                                        float progress = (float) bytesUploaded / totalBytes * 100;
                                        progressDialog.setProgress((int)progress);
                                    }
                                })
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        // do anything with response
                                        try {
                                            progressDialog.dismiss();
                                            JSONObject jsonObject = new JSONObject(response);
                                            String success = jsonObject.getString("success");
                                            String message = jsonObject.getString("message");
                                            String image_url = jsonObject.getString("data");
                                            if(success.equals("true")){
                                                sessionManager.addImage(image_url);
                                                Toast.makeText(ProfilePicture.this, message  , Toast.LENGTH_SHORT).show();
                                                Intent intent;
                                                if(update){
                                                    intent = new Intent(ProfilePicture.this, MainActivity.class);
                                                }else{
                                                    intent = new Intent(ProfilePicture.this, Shops.class);
                                                }

                                                startActivity(intent);
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(ProfilePicture.this, "Unable to upload image: "+message, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(ProfilePicture.this, "Pasring Error", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    @Override
                                    public void onError(ANError error) {
                                        // handle error
                                        progressDialog.dismiss();
                                        error.printStackTrace();
                                        Toast.makeText(ProfilePicture.this, "Error Uploading"+error, Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void skip(View view) {
        Intent intent ;
        if(update){
            intent = new Intent(ProfilePicture.this, MainActivity.class);
        }else{
            intent = new Intent(ProfilePicture.this, Shops.class);
        }

        startActivity(intent);
        finish();
    }
}
