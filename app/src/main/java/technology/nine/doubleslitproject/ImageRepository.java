package technology.nine.doubleslitproject;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import technology.nine.doubleslitproject.model.Image;

public class ImageRepository {
    private ImageDao mImageDao;
    private LiveData<List<Image>> mAllImages;

    public ImageRepository(Application application) {
        ImageRoomDatabase db = ImageRoomDatabase.getDatabase(application);
        mImageDao = db.imageDao();
        mAllImages = mImageDao.getAllImages();
    }

    public LiveData<List<Image>> getAllImages() {
        return mAllImages;
    }

    public void insert(Image image) {
        new insertAsyncTask(mImageDao).execute(image);
    }
    private static class insertAsyncTask extends AsyncTask<Image,Void,Void>{
        private ImageDao mAsyncTaskDao;
        public insertAsyncTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Image... images) {
            mAsyncTaskDao.insert(images[0]);
            return null;
        }
    }
}
