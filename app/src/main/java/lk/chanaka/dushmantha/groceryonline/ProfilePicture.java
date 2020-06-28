package lk.chanaka.dushmantha.groceryonline;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.androidnetworking.model.Progress;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilePicture extends AppCompatActivity {

    private CircleImageView civProfile;
    private CardView btnNext;
    private String URL;
    private ProgressDialog progressDialog;
    SessionManager sessionManager;
    private String token;
    private String host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

        host = ((MyApp) this.getApplication()).getServiceURL();
        URL = host+"/updateImage";

        sessionManager = new SessionManager(this);
        token = sessionManager.getToken();

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
                                            if(success.equals("true")){
                                                Toast.makeText(ProfilePicture.this, message  , Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProfilePicture.this, Itemlist.class);
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
        Intent intent = new Intent(ProfilePicture.this, Itemlist.class);
        startActivity(intent);
    }
}
