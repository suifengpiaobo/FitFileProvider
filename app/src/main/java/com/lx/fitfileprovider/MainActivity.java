package com.lx.fitfileprovider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.lx.fit7.Fit7Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageView mImgView;
    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(this);
        findViewById(R.id.btn_main_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //拍照
                String fileName = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA).format(new Date());
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                mFilePath = file.getAbsolutePath();
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Fit7Utils.getUriForFile(MainActivity.this, file));
                startActivityForResult(cameraIntent, 101);

            }
        });
        mImgView = findViewById(R.id.img_main_photo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 101:
                Bitmap temp = BitmapFactory.decodeFile(mFilePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                temp.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] bytes = baos.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                mImgView.setImageBitmap(bitmap);
                break;
        }
    }

    public void checkPermission(AppCompatActivity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission =
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE)
                            + ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            + ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                //动态申请
                ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                return;
            } else {
                return;
            }
        }
        return;
    }
}
