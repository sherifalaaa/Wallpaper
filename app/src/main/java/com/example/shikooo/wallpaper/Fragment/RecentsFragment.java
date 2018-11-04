package com.example.shikooo.wallpaper.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shikooo.wallpaper.Adapter.MyRecyclerAdapter;
import com.example.shikooo.wallpaper.Database.DataSource.RecentRepository;
import com.example.shikooo.wallpaper.Database.LocalDatabase.LocalDatabase;
import com.example.shikooo.wallpaper.Database.LocalDatabase.RecentDataSource;
import com.example.shikooo.wallpaper.Database.Recents;
import com.example.shikooo.wallpaper.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class RecentsFragment extends Fragment {

    RecyclerView recyclerView;
    List<Recents> recentsList;
    MyRecyclerAdapter adapter;

    Context context;

    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;

    public static RecentsFragment INSTANCE = null;

    public RecentsFragment() {
    }

    @SuppressLint("ValidFragment")
    public RecentsFragment(Context context) {
        // Required empty public constructor

        this.context = context;
        compositeDisposable = new CompositeDisposable();
        LocalDatabase database = LocalDatabase.getInstance(context);
        recentRepository = RecentRepository.getInstance(RecentDataSource.getInstance(database.recentsDAO()));

    }

    public static RecentsFragment getINSTANCE (Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = new RecentsFragment(context);
        }
        return INSTANCE;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recents, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recent_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recentsList = new ArrayList<>();
        adapter = new MyRecyclerAdapter(context,recentsList);
        recyclerView.setAdapter(adapter);
         
        loadRecents();

        return view;


    }

    private void loadRecents() {

        Disposable disposable = recentRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Recents>>() {
                    @Override
                    public void accept(List<Recents> recents) throws Exception {

                        onGetAllRecentsSuccess(recents);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        Log.d("ERROR" ,throwable.getMessage());

                    }
                });

        compositeDisposable.add(disposable);
    }

    private void onGetAllRecentsSuccess(List<Recents> recents) {

        recentsList.clear();
        recentsList.addAll(recents);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {

        compositeDisposable.clear();

        super.onDestroy();
    }
}
