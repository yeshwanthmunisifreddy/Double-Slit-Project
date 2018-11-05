package technology.nine.doubleslitproject.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import technology.nine.doubleslitproject.entity.Image;

@Dao
public interface ImageDao {
    @Insert
    void insert(Image image);

    @Query("SELECT * from Images_table")
    LiveData<List<Image>> getAllImages();

    @Query("SELECT * from IMAGES_TABLE where url Like :url")
    Image findByName(String url);


}
