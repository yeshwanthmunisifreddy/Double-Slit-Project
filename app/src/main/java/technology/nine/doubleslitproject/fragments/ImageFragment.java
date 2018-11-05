package technology.nine.doubleslitproject.fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import technology.nine.doubleslitproject.MainActivity;
import technology.nine.doubleslitproject.R;
import technology.nine.doubleslitproject.Util;
import technology.nine.doubleslitproject.adapter.ImagesAdapter;
import technology.nine.doubleslitproject.api.Client;
import technology.nine.doubleslitproject.api.Service;
import technology.nine.doubleslitproject.dao.ImageDao;
import technology.nine.doubleslitproject.database.ImageRoomDatabase;
import technology.nine.doubleslitproject.entity.Image;
import technology.nine.doubleslitproject.model.Item;
import technology.nine.doubleslitproject.viewModel.ImageViewModel;

public class ImageFragment extends Fragment {
    Bitmap bmp = null;
    String id;

    RecyclerView recyclerView;
    ImagesAdapter imagesAdapter;
    String filename;

    private ImageViewModel mImageViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image,container,false);
        recyclerView = view.findViewById(R.id.recycler_view);
        imagesAdapter = new ImagesAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(imagesAdapter);
        mImageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        mImageViewModel.getAllImages().observe(this, new android.arch.lifecycle.Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> images) {
                imagesAdapter.setWords(images);
            }
        });
        fetch();
        return view;

    }
    private void fetch() {
        Service apiService = Client.getRetrofit(getContext()).create(Service.class);
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
        ImageDao imageDao =ImageRoomDatabase.getDatabase(getContext()).imageDao();
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
            Util.encodeAndSaveFile(getContext(), bmp, filename);
            Image imageToUpdate =imageDao.findByName(urldisplay);
            if (imageToUpdate == null){
                Log.e("url","is not exist");
                mImageViewModel.insert(new Image(urldisplay, width, height, color));
            }
            else {
                Log.e("url","is exist");
            }

            return null;
        }
    }
}
