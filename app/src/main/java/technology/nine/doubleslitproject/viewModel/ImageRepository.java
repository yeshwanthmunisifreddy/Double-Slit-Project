package technology.nine.doubleslitproject.viewModel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import technology.nine.doubleslitproject.dao.ImageDao;
import technology.nine.doubleslitproject.dao.VideosDao;
import technology.nine.doubleslitproject.database.ImageRoomDatabase;
import technology.nine.doubleslitproject.entity.Image;
import technology.nine.doubleslitproject.entity.Videos;

class ImageRepository {
    private ImageDao mImageDao;
    private VideosDao mVideosDao;
    private LiveData<List<Image>> mAllImages;
    private LiveData<List<Videos>> mAllVideos;

    ImageRepository(Application application) {
        ImageRoomDatabase db = ImageRoomDatabase.getDatabase(application);
        mImageDao = db.imageDao();
        mAllImages = mImageDao.getAllImages();

        mVideosDao = db.videosDao();
        mAllVideos = mVideosDao.getAllIVideos();
    }

    LiveData<List<Image>> getAllImages() {
        return mAllImages;
    }

    public LiveData<List<Videos>> getAllVideos() {
        return mAllVideos;
    }

    void insert(Image image) {
        new insertAsyncTask(mImageDao).execute(image);
    }

    void insertVideo(Videos videos) {
        new insertVideoAsyncTask(mVideosDao).execute(videos);
    }

    private static class insertAsyncTask extends AsyncTask<Image, Void, Void> {
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

    private static class insertVideoAsyncTask extends AsyncTask<Videos, Void, Void> {
        private VideosDao mAsyncVideoTaskDao;

        insertVideoAsyncTask(VideosDao dao) {
            mAsyncVideoTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Videos... videos) {
            mAsyncVideoTaskDao.insert(videos[0]);
            return null;
        }
    }
}
