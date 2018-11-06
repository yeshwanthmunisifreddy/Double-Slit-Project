package technology.nine.doubleslitproject.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoObject {
    @SerializedName("items")
 private List<Video> videoList;

    public VideoObject(List<Video> videoList) {
        this.videoList = videoList;
    }
    public List<Video> getVideoList() {
        return videoList;
    }
}
