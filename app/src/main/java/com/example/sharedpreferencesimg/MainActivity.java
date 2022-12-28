package com.example.sharedpreferencesimg;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgbtn;
    Button button,takepicture;
    PreferenceManager preferenceManager;
    Intent camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        imgbtn=findViewById(R.id.imageview);
        button=findViewById(R.id.button3);
        takepicture=findViewById(R.id.clickpicture);
        preferenceManager=PreferenceManager.getInstance(this);
        button.setOnClickListener(this);
        takepicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.clickpicture:
                imgCamera();
                break;

            case R.id.button3:
                Intent intent=new Intent(this,DashboardActivity.class);
                startActivity(intent);
                finish();
        }
    }

    private void imgCamera() {
        if((ActivityCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.CAMERA,
                },123);
            }
        }
        else{
            camera=new Intent();
            camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera,118);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==118&& resultCode==RESULT_OK){

            Bitmap photo= (Bitmap) data.getExtras().get("data");
            imgbtn.setImageBitmap(photo);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] b = bytes.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            preferenceManager.setString("image_data",encodedImage);

            Toast.makeText(this, "Image saved in SharedPreferences", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "could not captured", Toast.LENGTH_SHORT).show();
        }
    }

}