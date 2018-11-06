package technology.nine.doubleslitproject.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import technology.nine.doubleslitproject.model.Item;
import technology.nine.doubleslitproject.model.Video;
import technology.nine.doubleslitproject.model.VideoObject;

public interface Service {

    @GET("/photos/")
    Observable<List<Item>> getItems(
            @Query("client_id") String apiKey,
            @Query("page") int currentPage
    );

    @GET("youtube/v3/videos")
    Observable<VideoObject> getVideo(
            @Query("part") String part,
            @Query("chart") String chart,
            @Query("regionCode") String regionCode,
            @Query("key") String key
    );
}
