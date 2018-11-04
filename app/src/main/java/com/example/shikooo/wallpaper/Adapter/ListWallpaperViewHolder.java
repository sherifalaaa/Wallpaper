package com.example.shikooo.wallpaper.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.shikooo.wallpaper.Interface.Itemclicklistener;
import com.example.shikooo.wallpaper.R;

/**
 * Created by shikooo on 8/12/2018.
 */

public class ListWallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    Itemclicklistener itemclicklistener;

    public ImageView imageView1;
    public Button btn_set;
    public Button btn_download;

    public ListWallpaperViewHolder(View itemView) {
        super(itemView);

        imageView1 = (ImageView) itemView.findViewById(R.id.imageView1);
        btn_set = (Button) itemView.findViewById(R.id.btn_set_wallpaper);
        btn_download = (Button) itemView.findViewById(R.id.btn_download);

        itemView.setOnClickListener(this);

    }

    public void setItemclicklistener(Itemclicklistener itemclicklistener) {
        this.itemclicklistener = itemclicklistener;
    }

    @Override
    public void onClick(View v) {

        itemclicklistener.onClick(v,getAdapterPosition());

    }

}
