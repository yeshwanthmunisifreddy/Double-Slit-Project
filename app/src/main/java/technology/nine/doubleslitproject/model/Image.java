package technology.nine.doubleslitproject.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Images_table")
public class Image {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "image")
    private String url;
    private int width;
    private int height;
    private String color;

    public Image(@NonNull String url, int width, int height, String color) {
        this.url = url;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}