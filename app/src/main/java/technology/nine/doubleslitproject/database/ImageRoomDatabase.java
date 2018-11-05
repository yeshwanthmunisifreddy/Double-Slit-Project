package technology.nine.doubleslitproject.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import technology.nine.doubleslitproject.dao.ImageDao;
import technology.nine.doubleslitproject.entity.Image;

@Database(entities = {Image.class}, version = 1)
public abstract class ImageRoomDatabase extends RoomDatabase {
    public abstract ImageDao imageDao();

    private static volatile ImageRoomDatabase INSTANCE;

     public  static ImageRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (ImageRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ImageRoomDatabase.class, "image_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
