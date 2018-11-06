package technology.nine.doubleslitproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import technology.nine.doubleslitproject.GlideApp;
import technology.nine.doubleslitproject.R;
import technology.nine.doubleslitproject.entity.Image;
import technology.nine.doubleslitproject.entity.Videos;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    private Context context;
    private List<Videos> videos;

    public VideoAdapter(Context context) {
        this.context = context;
        videos = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_recycler_item,viewGroup,false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        if (videos != null){
            String thumbnail = videos.get(i).getThumbnail();
            String id  = videos.get(i).getId();
            String url = videos.get(i).getUrl();
            String title = videos.get(i).getTitle();
            GlideApp.with(context)
                    .load(thumbnail)
                    .into(holder.imageView);
            holder.textView.setText(title);
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        ImageView imageView;
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.video_relative_layout);
            imageView = itemView.findViewById(R.id.youtube_thumbnail);
            textView = itemView.findViewById(R.id.title);
        }
    }
    public void setWords(List<Videos> video) {
        videos= video;
        notifyDataSetChanged();
    }
}
