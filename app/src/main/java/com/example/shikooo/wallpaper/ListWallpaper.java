package com.example.shikooo.wallpaper;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shikooo.wallpaper.Adapter.ListWallpaperViewHolder;
import com.example.shikooo.wallpaper.Common.Common;
import com.example.shikooo.wallpaper.Helper.SaveImageHelper;
import com.example.shikooo.wallpaper.Interface.Itemclicklistener;
import com.example.shikooo.wallpaper.Model.WallpaperItem;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class ListWallpaper extends AppCompatActivity {

    Query query;

    FirebaseRecyclerOptions<WallpaperItem> options;
    FirebaseRecyclerAdapter<WallpaperItem,ListWallpaperViewHolder> adapter;

    RecyclerView recyclerView;
    RelativeLayout relativeLayout;


    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                wallpaperManager.setBitmap(bitmap);

                Snackbar.make(relativeLayout,"Wallpaper was set",Snackbar.LENGTH_LONG).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Toast.makeText(ListWallpaper.this,"Failed! ",Toast.LENGTH_LONG).show();

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Snackbar.make(relativeLayout,"Wallpaper was not set",Snackbar.LENGTH_LONG).show();

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case Common.PERMISION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED)
                {

                    AlertDialog dialog = new  SpotsDialog(ListWallpaper.this);
                    dialog.show();
                    dialog.setMessage("Please Waiting.....");
                    String fileName = UUID.randomUUID().toString()+" .png";
                    Picasso.with(getBaseContext())
                            .load(Common.select_bacgrounds.getImageurl())
                            .into(new SaveImageHelper(getBaseContext(),dialog,getApplicationContext().getContentResolver(),fileName,"Live Wallpaper Image"));
                }
                else
                {
                    Toast.makeText(this,"You need to accept this permission to dowmload image",Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wallpaper);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Common.GATEGORY_SELECTED);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.list_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        relativeLayout = (RelativeLayout) findViewById(R.id.rootlayout);



        loadBackgroundList();

    }


    private void loadBackgroundList()
    {

        query = FirebaseDatabase.getInstance().getReference(Common.STR_WALLPAPER)
                .orderByChild("categoryId").equalTo(Common.GATEGORY_ID_SELECTED);

        options = new FirebaseRecyclerOptions.Builder<WallpaperItem>()
                .setQuery(query,WallpaperItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, final int position, @NonNull final WallpaperItem model) {

                Picasso.with(getBaseContext())
                        .load(model.getImageurl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.imageView1, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getBaseContext())
                                        .load(model.getImageurl())
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

                        Common.GATEGORY_ID_SELECTED = adapter.getRef(postion).getKey();
                        Common.select_bacgrounds = model;
                        Common.select_background_key = adapter.getRef(position).getKey();
                        Intent intent =new Intent(ListWallpaper.this, ViewWallpaper.class);
                        startActivity(intent);

                    }
                });

                holder.btn_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // check the permission
                        if (ActivityCompat.checkSelfPermission(ListWallpaper.this,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                            {
                                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISION_REQUEST_CODE);
                            }
                        }
                        else
                        {
                            Common.select_bacgrounds = model;
                            AlertDialog dialog = new  SpotsDialog(ListWallpaper.this);
                            dialog.show();
                            dialog.setMessage("Please Waiting.....");
                            String fileName = UUID.randomUUID().toString()+" .png";
                            Picasso.with(getBaseContext())
                                    .load(Common.select_bacgrounds.getImageurl())
                                    .into(new SaveImageHelper(getBaseContext(),dialog,getApplicationContext().getContentResolver(),fileName,"Live Wallpaper Image"));
                        }


                    }
                });

                holder.btn_set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.select_bacgrounds = model;
                        Picasso.with(getBaseContext())
                                .load(Common.select_bacgrounds.getImageurl())
                                .into(target);

                        Toast.makeText(ListWallpaper.this,Common.select_bacgrounds.getImageurl(),Toast.LENGTH_LONG).show();

                    }
                });
            }




            @NonNull
            @Override
            public ListWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wallpaper_list,parent,false);
                return new  ListWallpaperViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void onStart()
    {
        super.onStart();
        if(adapter != null)
        {
            adapter.startListening();
        }
    }

    @Override
    public void onStop()
    {
        if (adapter != null)
        {
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (adapter != null)
        {
            adapter.startListening();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {



        super.onDestroy();
    }
}
