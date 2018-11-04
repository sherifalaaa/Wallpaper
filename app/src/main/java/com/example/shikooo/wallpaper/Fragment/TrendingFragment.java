package com.example.shikooo.wallpaper.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shikooo.wallpaper.Adapter.ListWallpaperViewHolder;
import com.example.shikooo.wallpaper.Common.Common;
import com.example.shikooo.wallpaper.Interface.Itemclicklistener;
import com.example.shikooo.wallpaper.Model.WallpaperItem;
import com.example.shikooo.wallpaper.R;
import com.example.shikooo.wallpaper.ViewWallpaper;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class TrendingFragment extends Fragment {

    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference categoryBackground;
    FirebaseRecyclerOptions<WallpaperItem> options;
    FirebaseRecyclerAdapter<WallpaperItem,ListWallpaperViewHolder> adapter;

    public static TrendingFragment INSTANCE = null;


    public TrendingFragment() {

        database = FirebaseDatabase.getInstance();
        categoryBackground = database.getReference(Common.STR_WALLPAPER);

        Query query = categoryBackground.orderByChild("viewCount")
                .limitToLast(10);
        options = new FirebaseRecyclerOptions.Builder<WallpaperItem>()
                .setQuery(query,WallpaperItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, int position, @NonNull final WallpaperItem model) {

                Picasso.with(getActivity())
                        .load(model.getImageurl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.imageView1, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity())
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
                        Common.select_background_key = adapter.getRef(postion).getKey();
                        Intent intent =new Intent(getActivity(), ViewWallpaper.class);
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public ListWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wallpaper_list,parent,false);
                int height = parent.getMeasuredHeight() / 2;
                view.setMinimumHeight(height);
                return new  ListWallpaperViewHolder(view);
            }
        };
    }

    public static TrendingFragment getINSTANCE ()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new TrendingFragment();
        }
        return INSTANCE;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_popular, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_trending);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        // because firebase return ascending sort list so we need reverse recyclerview to show largest item first 
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadTrendingList();

        return view;
    }

    private void loadTrendingList() {

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter !=null)
        {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        if (adapter !=null) {
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter !=null)
        {
            adapter.startListening();
        }
    }
}
