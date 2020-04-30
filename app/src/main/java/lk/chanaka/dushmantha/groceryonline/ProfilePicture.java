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
    private String url = "http://10.0.2.2:8000/api/updateImage";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);

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
                                if(permissionDeniedResponse.isPermanentlyDenied()){
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
                System.out.println(resultUri);
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File imageFile = new File(resultUri.getPath());
                        progressDialog.show();
                        AndroidNetworking.upload(url)
                                .addMultipartFile("image",imageFile)
                                .addHeaders("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC8xMjcuMC4wLjE6ODAwMFwvYXBpXC9sb2dpbiIsImlhdCI6MTU4ODI1MjE3NSwiZXhwIjoxNTg4MjU1Nzc1LCJuYmYiOjE1ODgyNTIxNzUsImp0aSI6IktoRmhBaEhrelNFRU1saXUiLCJzdWIiOjMsImRhdGEiOnsiZW1haWwiOiJ2aWhhbmdhM0BnbWFpbC5jb20iLCJwYXNzd29yZCI6IjEyMzQ1NiIsInVzZXJfdHlwZSI6InVzZXIifX0.JbeXWBlk3Qvha05thxY41qvfsC5ShjXI7SzZ6F1Fido")
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

    public void registerActivity(View view) {
    }
}
