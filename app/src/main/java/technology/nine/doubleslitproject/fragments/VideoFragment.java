package technology.nine.doubleslitproject.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import technology.nine.doubleslitproject.R;
import technology.nine.doubleslitproject.Util;
import technology.nine.doubleslitproject.adapter.VideoAdapter;
import technology.nine.doubleslitproject.api.Client;
import technology.nine.doubleslitproject.api.Service;
import technology.nine.doubleslitproject.dao.ImageDao;
import technology.nine.doubleslitproject.dao.VideosDao;
import technology.nine.doubleslitproject.database.ImageRoomDatabase;
import technology.nine.doubleslitproject.entity.Image;
import technology.nine.doubleslitproject.entity.Videos;
import technology.nine.doubleslitproject.model.Video;
import technology.nine.doubleslitproject.model.VideoObject;
import technology.nine.doubleslitproject.viewModel.ImageViewModel;

public class VideoFragment extends Fragment {
    private ImageViewModel mImageViewModel;
    RecyclerView recyclerView;
    VideoAdapter videoAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        videoAdapter = new VideoAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(videoAdapter);
        recyclerView.setHasFixedSize(true);
        mImageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        mImageViewModel.getAllVideos().observe(this, new android.arch.lifecycle.Observer<List<Videos>>() {
            @Override
            public void onChanged(@Nullable List<Videos> videos) {
                videoAdapter.setWords(videos);
            }
        });
        fetch();
        return view;
    }
    private void fetch() {
        Log.e("fetch", "is called");
        Service apiService = Client.getVideoRetrofit(getContext()).create(Service.class);
        apiService.getVideo("snippet", "mostPopular", "in", "AIzaSyCrXyv1A4GX-BUIxl0OMGehxm464U9vyFo")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VideoObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(VideoObject videoObject) {
                        VideosDao videosDao = ImageRoomDatabase.getDatabase(getContext()).videosDao();
                        List<Video> videos = videoObject.getVideoList();
                        final String video_base_url = "https://www.youtube.com/watch?v=";
                        for (int i = 0; i < videos.size() - 1; i++) {
                            String id = videos.get(i).getId();
                            String thumbUrl = videos.get(i).getSnippet().getThumbnails().getHigh().getUrl();
                            String title = videos.get(i).getSnippet().getTitle();
                            String videoUrl = video_base_url + id;
                            Videos videosToInsert = videosDao.findByVideoName(videoUrl);
                            if (videosToInsert == null) {
                                videosDao.insert(new Videos(videoUrl, id, thumbUrl, title));
                            }
                            new DownloadVideoTask().execute(videoUrl, id);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private  class DownloadVideoTask extends AsyncTask<String, Void, Void> {
        Bitmap bmp;

        @Override
        protected Void doInBackground(String... strings) {
            String urls = strings[0];
            String fileName = strings[1];
            try {
                Util.encodeAndSaveVideoFile(getContext(), urls, fileName);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
    }

}

