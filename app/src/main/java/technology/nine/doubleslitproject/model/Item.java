package technology.nine.doubleslitproject.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item {
    private String id;
    @SerializedName("created_at")
    @Expose
    private String createdDate;
    private float width;
    private float height;
    private String color;
    private Urls urls;

    public Item(String id, String createdDate, float width, float height, String color, Urls urls) {
        this.id = id;
        this.createdDate = createdDate;
        this.width = width;
        this.height = height;
        this.color = color;
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getColor() {
        return color;
    }

    public Urls getUrls() {
        return urls;
    }


    public  class Urls {
        @SerializedName("raw")
        @Expose
        private String raw;
        @SerializedName("full")
        @Expose
        private String full;
        @SerializedName("regular")
        @Expose
        private String regular;
        @SerializedName("small")
        @Expose
        private String small;
        @SerializedName("thumb")
        @Expose
        private String thumb;

        public Urls(String raw, String full, String regular, String small, String thumb) {
            this.raw = raw;
            this.full = full;
            this.regular = regular;
            this.small = small;
            this.thumb = thumb;
        }

        public String getRaw() {
            return raw;
        }

        public String getFull() {
            return full;
        }

        public String getRegular() {
            return regular;
        }

        public String getSmall() {
            return small;
        }

        public String getThumb() {
            return thumb;
        }

        public void setRaw(String raw) {
            this.raw = raw;
        }

        public void setFull(String full) {
            this.full = full;
        }

        public void setRegular(String regular) {
            this.regular = regular;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

    }
}
