package com.example.shikooo.wallpaper.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shikooo.wallpaper.Adapter.CategoryViewHolder;
import com.example.shikooo.wallpaper.Common.Common;
import com.example.shikooo.wallpaper.Interface.Itemclicklistener;
import com.example.shikooo.wallpaper.ListWallpaper;
import com.example.shikooo.wallpaper.Model.CategoryItem;
import com.example.shikooo.wallpaper.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {

    public static CategoryFragment INSTANCE = null;

    FirebaseDatabase database;
    DatabaseReference CategoryBackground;

    FirebaseRecyclerOptions<CategoryItem> options;
    FirebaseRecyclerAdapter<CategoryItem,CategoryViewHolder> adapter;

    RecyclerView recyclerView;


    public CategoryFragment() {

        database = FirebaseDatabase.getInstance();
        CategoryBackground = database.getReference(Common.STR_CATEGORY_BACKGROUND);

        options = new FirebaseRecyclerOptions.Builder<CategoryItem>()
                .setQuery(CategoryBackground,CategoryItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position, @NonNull final CategoryItem model) {

                Picasso.with(getActivity())
                        .load(model.getImageurl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity())
                                        .load(model.getImageurl())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.imageView, new Callback() {
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


                holder.nametxt.setText(model.getName());
                holder.nametxt.setTextSize(14);

                holder.setItemClickListener(new Itemclicklistener() {
                    @Override
                    public void onClick(View view, int postion) {
                        Common.GATEGORY_ID_SELECTED = adapter.getRef(postion).getKey();
                        Common.GATEGORY_SELECTED = model.getName();
                        Intent intent =new Intent(getActivity(), ListWallpaper.class);
                        startActivity(intent);
                    }
                });


            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_item,parent,false);
                return new CategoryViewHolder(view);
            }
        };
    }

    public static CategoryFragment getINSTANCE ()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new CategoryFragment();
        }
        return INSTANCE;
    }

    public static CategoryFragment newInstance(String param1, String param2)
    {
        CategoryFragment fragment = new CategoryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

        setCategory();

        return view;
    }
    private void setCategory ()
    {
            adapter.startListening();
            recyclerView.setAdapter(adapter);
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
}
