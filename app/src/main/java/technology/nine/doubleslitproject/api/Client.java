package technology.nine.doubleslitproject.api;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    public static final String IMAGE_BASE_URL = "https://api.unsplash.com";
    public static Retrofit imageRetrofit = null;
    public  static final  String VIDEO_BASE_URL ="https://www.googleapis.com";
    public  static Retrofit videoRetrofit = null;
    public static Retrofit getRetrofit(Context context) {
        if (imageRetrofit == null){
            imageRetrofit=new Retrofit.Builder()
                    .baseUrl(IMAGE_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return imageRetrofit;
    }
    public  static  Retrofit getVideoRetrofit (Context context){
        if (videoRetrofit == null){
            videoRetrofit = new Retrofit.Builder()
                    .baseUrl(VIDEO_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  videoRetrofit;
    }
}
