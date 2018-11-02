package technology.nine.doubleslitproject.api;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import technology.nine.doubleslitproject.model.Item;

public interface Service {

    @GET("/photos/")
    Observable<List<Item>> getItems(
            @Query("client_id") String apiKey,
            @Query("page") int currentPage
    );
}
