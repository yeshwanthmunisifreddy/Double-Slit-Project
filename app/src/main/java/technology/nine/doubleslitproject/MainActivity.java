package technology.nine.doubleslitproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.soloader.SoLoader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import technology.nine.doubleslitproject.api.Client;
import technology.nine.doubleslitproject.api.Service;
import technology.nine.doubleslitproject.model.Image;
import technology.nine.doubleslitproject.model.Item;

public class MainActivity extends AppCompatActivity {

    AlertDialog alert;
    String filename;
    AlertDialog.Builder builder;
    private SharedPreferences permissionStatus;
    static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    boolean sentToSettings = false;
    Bitmap bmp = null;
    String id;

    RecyclerView recyclerView;
    ImagesAdapter imagesAdapter;

    private ImageViewModel mImageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_image);

        //initializing the conceal library
        SoLoader.init(this, false);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        permissions();
        recyclerView = findViewById(R.id.recycler_view);
        imagesAdapter = new ImagesAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(imagesAdapter);

        mImageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        mImageViewModel.getAllImages().observe(this, new android.arch.lifecycle.Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> images) {
                imagesAdapter.setWords(images);
            }
        });

    }

    private void fetch() {
        Service apiService = Client.getRetrofit(getApplicationContext()).create(Service.class);
        apiService.getItems("9537c083326dcc052deccb63fdd7ba197a7f249b96f3eed6a6fd7d24a83b9b6b", 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Item>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<Item> value) {
                        if (value != null) {
                            for (int i = 0; i < value.size() - 1; i++) {
                                String url = value.get(i).getUrls().getFull();
                                filename = value.get(i).getId();
                                int width = (int) value.get(i).getWidth();
                                int height = (int) value.get(i).getHeight();
                                String color = value.get(i).getColor();
                                MyTaskParams params = new MyTaskParams(url, filename, width, height, color);
                                MyTask myTask = new MyTask();
                                myTask.execute(params);
                            }

                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void permissions() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                Button nButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nButton.setTextColor(Color.GRAY);
                Button pButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pButton.setTextColor(Color.GRAY);
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                Button nButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nButton.setTextColor(Color.GRAY);
                Button pButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pButton.setTextColor(Color.GRAY);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.apply();
        } else {
            fetch();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                fetch();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                fetch();
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                fetch();
            }
        }
    }

    private class MyTaskParams {
        String url;
        String filename;
        int width;
        int height;
        String color;

        MyTaskParams(String url, String filename, int width, int height, String color) {
            this.url = url;
            this.filename = filename;
            this.width = width;
            this.height = height;
            this.color = color;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private  class MyTask extends AsyncTask<MyTaskParams, Void, Void> {

        @Override
        protected Void doInBackground(MyTaskParams... myTaskParams) {
            String urldisplay = myTaskParams[0].url;
            String filename = myTaskParams[0].filename;
            int width = myTaskParams[0].width;
            int height = myTaskParams[0].height;
            String color = myTaskParams[0].color;
            try {
                URL url = new URL(urldisplay);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Util.encodeAndSaveFile(getApplicationContext(), bmp, filename);
            mImageViewModel.insert(new Image(urldisplay, width, height, color));
            return null;
        }
    }
}
