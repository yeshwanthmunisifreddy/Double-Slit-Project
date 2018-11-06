package technology.nine.doubleslitproject.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Videos_table")
public class Videos {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "url")
    private String url;
    private String id ;
    private  String thumbnail;
    private String  title;

    public Videos(@NonNull String url, String id, String thumbnail, String title) {
        this.url = url;
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
