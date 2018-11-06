package technology.nine.doubleslitproject.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import technology.nine.doubleslitproject.entity.Image;
import technology.nine.doubleslitproject.entity.Videos;

public class ImageViewModel extends AndroidViewModel {
    private ImageRepository mImageRepository;
    private LiveData<List<Image>> mAllImages;
    private  LiveData<List<Videos>> mAllVideos;
    public ImageViewModel(@NonNull Application application) {
        super(application);
        mImageRepository = new ImageRepository(application);
        mAllImages = mImageRepository.getAllImages();
        mAllVideos = mImageRepository.getAllVideos();
    }

    public LiveData<List<Image>> getAllImages() {
        return mAllImages;
    }

    public LiveData<List<Videos>> getAllVideos() {
        return mAllVideos;
    }

    public void insert(Image image){
        mImageRepository.insert(image);
    }
    public void insert(Videos videos){
        mImageRepository.insertVideo(videos);
    }
}
