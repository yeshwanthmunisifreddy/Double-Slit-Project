package technology.nine.doubleslitproject.viewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import technology.nine.doubleslitproject.dao.ImageDao;
import technology.nine.doubleslitproject.database.ImageRoomDatabase;
import technology.nine.doubleslitproject.entity.Image;

class ImageRepository {
    private ImageDao mImageDao;
    private LiveData<List<Image>> mAllImages;

    ImageRepository(Application application) {
        ImageRoomDatabase db = ImageRoomDatabase.getDatabase(application);
        mImageDao = db.imageDao();
        mAllImages = mImageDao.getAllImages();
    }

    LiveData<List<Image>> getAllImages() {
        return mAllImages;
    }

    void insert(Image image) {
        new insertAsyncTask(mImageDao).execute(image);
    }
    private static class insertAsyncTask extends AsyncTask<Image,Void,Void>{
        private ImageDao mAsyncTaskDao;
        insertAsyncTask(ImageDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Image... images) {
            mAsyncTaskDao.insert(images[0]);
            return null;
        }
    }
}
