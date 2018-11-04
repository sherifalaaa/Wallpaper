package com.example.shikooo.wallpaper.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shikooo.wallpaper.Interface.Itemclicklistener;
import com.example.shikooo.wallpaper.R;

/**
 * Created by shikooo on 8/9/2018.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

  Itemclicklistener itemClickListener;

   public   TextView nametxt;
   public   ImageView imageView;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        nametxt = (TextView) itemView.findViewById(R.id.name);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(Itemclicklistener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition());

    }
}

