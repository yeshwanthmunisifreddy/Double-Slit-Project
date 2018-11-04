package technology.nine.doubleslitproject;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import technology.nine.doubleslitproject.model.Image;

@Database(entities = {Image.class}, version = 1)
public abstract class ImageRoomDatabase extends RoomDatabase {
    public abstract ImageDao imageDao();

    private static volatile ImageRoomDatabase INSTANCE;

    static ImageRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null){
            synchronized (ImageRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ImageRoomDatabase.class, "image_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
