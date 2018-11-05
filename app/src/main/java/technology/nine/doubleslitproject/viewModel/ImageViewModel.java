package technology.nine.doubleslitproject.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import technology.nine.doubleslitproject.entity.Image;

public class ImageViewModel extends AndroidViewModel {
    private ImageRepository mImageRepository;
    private LiveData<List<Image>> mAllImages;
    public ImageViewModel(@NonNull Application application) {
        super(application);
        mImageRepository = new ImageRepository(application);
        mAllImages = mImageRepository.getAllImages();
    }

    public LiveData<List<Image>> getAllImages() {
        return mAllImages;
    }
    public void insert(Image image){
        mImageRepository.insert(image);
    }
}
