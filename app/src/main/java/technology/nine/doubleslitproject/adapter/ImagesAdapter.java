package technology.nine.doubleslitproject.adapter;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import technology.nine.doubleslitproject.GlideApp;
import technology.nine.doubleslitproject.R;
import technology.nine.doubleslitproject.entity.Image;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {
    private  Context context;
    private List<Image> mImages;

    public ImagesAdapter(Context context) {
        this.context = context;
        mImages = new ArrayList<>();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.image_recyclerview_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
      if (mImages != null){
          Image current = mImages.get(i);
          int width = current.getWidth();
          int height = current.getHeight();
          String url = current.getUrl();
          DisplayMetrics metrics = context.getResources().getDisplayMetrics();
          float finalHeight = metrics.widthPixels / ((float) width / (float) height);
          GlideApp.with(context)
                  .load(url)
                  .into(holder.imageView);
          holder.imageView.setMinimumHeight((int) finalHeight);
          int colorFrom = Color.WHITE;
          int colorTo;
          String color =current.getColor();
          if (color != null) {
              colorTo = Color.parseColor(color);
          } else {
              colorTo = Color.WHITE;
          }
          ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
          colorAnimation.setDuration(0);
          colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
              @Override
              public void onAnimationUpdate(ValueAnimator animator) {
                  holder.imageView.setBackgroundColor((int) animator.getAnimatedValue());
              }

          });
          colorAnimation.start();
      }
    }
    public void setWords(List<Image> words) {
        mImages = words;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recyclerview_image_view);
        }
    }
}
