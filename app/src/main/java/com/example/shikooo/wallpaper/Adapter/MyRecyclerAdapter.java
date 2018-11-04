package com.example.shikooo.wallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shikooo.wallpaper.Common.Common;
import com.example.shikooo.wallpaper.Database.Recents;
import com.example.shikooo.wallpaper.Interface.Itemclicklistener;
import com.example.shikooo.wallpaper.Model.WallpaperItem;
import com.example.shikooo.wallpaper.R;
import com.example.shikooo.wallpaper.ViewWallpaper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shikooo on 8/15/2018.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<ListWallpaperViewHolder> {

   private Context context;
   private List<Recents> recents;

    public MyRecyclerAdapter(Context context, List<Recents> recents) {
        this.context = context;
        this.recents = recents;
    }

    @NonNull
    @Override
    public ListWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_list,parent,false);
        int height = parent.getMeasuredHeight() /2;
        view.setMinimumHeight(height);
        return new  ListWallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, final int position) {



                Picasso.with(context)
                        .load(recents.get(position).getImageurl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.imageView1, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(context)
                                        .load(recents.get(position).getImageurl())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.imageView1, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Log.e("Error","Couldn't fetch image");
                                            }
                                        });
                                ;
                            }
                        });


                holder.setItemclicklistener(new Itemclicklistener() {
                    @Override
                    public void onClick(View view, int postion) {


                        Intent intent =new Intent(context, ViewWallpaper.class);
                        WallpaperItem wallpaperItem = new WallpaperItem();
                        wallpaperItem.setCategoryId(recents.get(position).getCategoryId());
                        wallpaperItem.setImageurl(recents.get(position).getImageurl());
                        Common.select_bacgrounds = wallpaperItem;
                        Common.select_background_key=recents.get(position).getKey();
                        context.startActivity(intent);

                    }
                });

    }

    @Override
    public int getItemCount() {
        return recents.size();
    }
}
