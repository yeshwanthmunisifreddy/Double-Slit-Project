package technology.nine.doubleslitproject;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import technology.nine.doubleslitproject.model.Image;

@Dao
public interface ImageDao {
    @Insert
    void insert(Image image);

    @Query("SELECT * from Images_table")
    LiveData<List<Image>> getAllImages();
}
