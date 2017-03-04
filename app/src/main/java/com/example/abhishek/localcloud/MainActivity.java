package com.example.abhishek.localcloud;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;


import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.v4.app.ActivityCompat.startActivity;
import static xdroid.toaster.Toaster.toast;

public class MainActivity extends AppCompatActivity {

    String SCAN_PATH;
    File[] allFiles ;
    private  Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File folder = new File(Environment.getExternalStorageDirectory().getPath()+"/DCIM/");
        allFiles = folder.listFiles();

        button = (Button)findViewById(R.id.button1);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;

            }
        }
        enable_button();


    }

    private void enable_button(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialFilePicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(10)
                        .start();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100 && (grantResults[0]==PackageManager.PERMISSION_GRANTED)){
            enable_button();
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
            }
        }
    }

    ProgressDialog progress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10 && resultCode == RESULT_OK){

//            progress = new ProgressDialog(MainActivity.this);
//            progress.setTitle("Uploading");
//            progress.setMessage("Please Wait......");
//            progress.show();

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    String content_type = getMimeType(f.getPath());

                    String file_path = f.getAbsolutePath();

                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1),file_body)
                            .build();

                    try{
                        Request request = new Request.Builder()
                                .url(Constants.Url)
                                .post(request_body)
                                .build();




                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()){
                            throw new IOException("Error :"+response);

                        }

                      //  progress.dismiss();

                    }catch (Exception e){
                        toast("Kuch galat hain url mein");
                    }

                }
            });
            t.start();
        }
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

//    public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {
//
//        private MediaScannerConnection mMs;
//        private File mFile;
//
//        public SingleMediaScanner(Context context, File f) {
//            mFile = f;
//            mMs = new MediaScannerConnection(context, this);
//            mMs.connect();
//        }
//
//        public void onMediaScannerConnected() {
//            mMs.scanFile(mFile.getAbsolutePath(), null);
//        }
//
//        public void onScanCompleted(String path, Uri uri) {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(uri);
//            startActivity(intent);
//            mMs.disconnect();
//        }
//
//    }
}
