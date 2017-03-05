package com.example.abhishek.localcloud;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;


import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static xdroid.toaster.Toaster.toast;

public class MainActivity extends AppCompatActivity
{
    private int mInterval = 20000;

    Handler handler;
    String SCAN_PATH;
    File[] allFiles ;
    private  Button button;

    public static Context context;
    SharedPreferences sharedpreferences;

    private final static int INTERVAL = 60000; //2 minutes
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File folder = new File(Environment.getExternalStorageDirectory().getPath()+"/DCIM/");
        allFiles = folder.listFiles();

        context = getApplicationContext();



        button = (Button)findViewById(R.id.button1);

        sharedpreferences = getSharedPreferences("my prefs", Context.MODE_PRIVATE);

//        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
//        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
//        Log.e("errorr10",ip);
//       String array[] = ip.split("\\.");
//        StringBuilder build = new StringBuilder();
//        build.append(array[0]);
//        build.append(".");
//        build.append(array[1]);
//        build.append(".");
//        build.append(array[2]);
//        build.append(".");
//        String subst = build.toString();
//        for (int i=1;i<=255;i++){
//            String macad="";
//            StringBuilder bi = new StringBuilder();
//            bi.append(subst);
//            bi.append(i);
//
//            String tempip = bi.toString();
//            try {
//                InetAddress inad = InetAddress.getByName(ip);
//                SocketAddress sockaddr = new InetSocketAddress(inad, 8000);
//                // Create an unbound socket
//                Socket sock = new Socket();
//
//                // This method will block no more than timeoutMs.
//                // If the timeout occurs, SocketTimeoutException is thrown.
//                int timeoutMs = 2000;   // 2 seconds
//                Log.e("ip_addr",tempip);
//                sock.connect(sockaddr, timeoutMs);
////                exists = true;
//                Log.e("errorr444","hj");
//                 macad = getmac(tempip);
//
//                Log.e("mac_addr",macad);
//                if(macad.equals(Constants.MACAD))
//                    Constants.Url = bi.append(":8000/app/backup").toString();
//            }catch(Exception e){
//            }
//
//        }
//
//        Log.e("errorip",ip);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;

            }
        }
        mHandler = new Handler();
        startRepeatingTask();
/////
//        File fl = new File(Constants.dir);
//        File[] files = fl.listFiles(new FileFilter()
//        {
//            public boolean accept(File file) {
//                return file.isFile();
//            }
//        });
//        for (File file : files)
//        {
//            String file_path = file.getAbsolutePath();
//            if((file_path.substring(file_path.lastIndexOf("/")+1).equals("IMG_20170301_175233796.jpg")))
//            {
//                Log.e("errorr", String.valueOf(file.lastModified()));
//            }
//            if(file.lastModified()>1488370955000l)
//            {
//                Log.e("errorr", String.valueOf(file.lastModified()));
//            }
//        }
//        final File f = files[0];
//        String content_type = getMimeType(f.getPath());
//        Log.e("errorr1",String.valueOf(f.lastModified()));
//        String file_path = f.getAbsolutePath();
//        Log.e("errorr2",file_path);
//        Log.e("errorr3",file_path.substring(file_path.lastIndexOf("/")+1));//,file_body




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }
    public String getmac(String ipad)
    {
        String str = "";
        InetAddress ip;
        try {

            ip = InetAddress.getByName(ipad);
           Log.e("Current IP address : " , ip.getHostAddress());

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();



            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
            }
//            Log.e("Current MAC address : ",);
//            System.out.println(sb.toString());

            str = sb.toString();
            Log.e("errorr11" ,str);
        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e){

            e.printStackTrace();

        }
        return str;

    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
//                updateStatus(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
               sendingtime();
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    private void enable_button(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                new MaterialFilePicker()
//                        .withActivity(MainActivity.this)
//                        .withRequestCode(10)
//                        .start();
//                sendingtime();
                mHandler = new Handler();
                startRepeatingTask();

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

                    Log.e("errorr",String.valueOf(f.lastModified()));

                    String file_path = f.getAbsolutePath();

                    Log.e("errorr",file_path);

                    OkHttpClient client = new OkHttpClient();
                    RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                    RequestBody request_body = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("type",content_type)
                            .addFormDataPart("path","yiihbj")

                            .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1),file_body)
                            .build();


                        Request request = new Request.Builder()
                                .url(Constants.Url)
                                .post(request_body)
                                .build();



                    try{
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()){
                            throw new IOException("Error :"+response);

                        }

                      //  progress.dismiss();

                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e("error",e.getMessage());
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


    private void sendingtime()
    {
        File fl = new File(Constants.dir);
        File[] files = fl.listFiles(new FileFilter()
        {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastTimeStamp= 1488605655000l;
//        String tempTimeStamp = PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", null);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tempTimeStamp = preferences.getString("key", "-1");
//        String tempTimeStamp = sharedpreferences.getString("key","-1");
        Log.e("errorr5",tempTimeStamp);
        if (!tempTimeStamp.equals("-1")){
            lastTimeStamp = Long.parseLong(tempTimeStamp);
        }




        Log.e("errorr123",String.valueOf(lastTimeStamp));
        long maxTimeStamp = lastTimeStamp;
        for (File file : files)
        {
            if (file.lastModified() > lastTimeStamp && !Constants.Url.equals(""))
            {

                final File f = file;

                if(maxTimeStamp<file.lastModified()) {
                    maxTimeStamp = file.lastModified();
                };
                Thread t = new Thread(new Runnable()
                {
                    @Override
                    public void run() {
                      //  File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                        String content_type = getMimeType(f.getPath());

                        Log.e("errorr",String.valueOf(f.lastModified()));

                        String file_path = f.getAbsolutePath();

                        Log.e("errorr",file_path);

                        OkHttpClient client = new OkHttpClient();
                        RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                        RequestBody request_body = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("type",content_type)
                                .addFormDataPart("path",file_path)
                                .addFormDataPart("uploaded_file",file_path.substring(file_path.lastIndexOf("/")+1),file_body)
                                .build();


                        Request request = new Request.Builder()
                                .url(Constants.Url)
                                .post(request_body)
                                .build();



                        try{
                            Response response = client.newCall(request).execute();
                            if (!response.isSuccessful()){
                                throw new IOException("Error :"+response);

                            }

                            //  progress.dismiss();

                        }catch (Exception e){
                            e.printStackTrace();
                            Log.e("error",e.getMessage());
                            toast("Kuch galat hain url mein");
                        }

                    }
                });
                t.start();
//                lastMod = file.lastModified();
                try {
                    t.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
//        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("MYLABEL", String.valueOf(maxTimeStamp)).commit();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("key",String.valueOf(maxTimeStamp) );
        editor.apply();

//        SharedPreferences preferences1 = getSharedPreferences("activity_main.xml", 0);
//        SharedPreferences.Editor editor = preferences1.edit();
//        editor.putString("key", String.valueOf(maxTimeStamp));
//        editor.commit();
    }
}
