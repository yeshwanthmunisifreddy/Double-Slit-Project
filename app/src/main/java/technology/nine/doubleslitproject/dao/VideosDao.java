package technology.nine.doubleslitproject.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import technology.nine.doubleslitproject.entity.Videos;

@Dao
public interface VideosDao {
    @Insert
    void insert(Videos videos);

    @Query("SELECT * from Videos_table")
    LiveData<List<Videos>> getAllIVideos();

    @Query(" SELECT * FROM Videos_table where url LIKe :url")
    Videos findByVideoName(String url);
}
