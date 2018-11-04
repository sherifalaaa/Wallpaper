package com.example.shikooo.wallpaper;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shikooo.wallpaper.Common.Common;
import com.example.shikooo.wallpaper.Database.DataSource.RecentRepository;
import com.example.shikooo.wallpaper.Database.LocalDatabase.LocalDatabase;
import com.example.shikooo.wallpaper.Database.Recents;
import com.example.shikooo.wallpaper.Model.WallpaperItem;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

;

public class ViewWallpaper extends AppCompatActivity {

    ImageView imageView;
    Button btn_wallpaper;
    Button btn_download;

    FloatingActionMenu mainFolating;
    FloatingActionButton fbShare;


    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;

    CallbackManager callbackManager;
    ShareDialog shareDialog;


    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
            try {
                wallpaperManager.setBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Toast.makeText(ViewWallpaper.this, "Failed! ", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    Target facebookConvertBitmap = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (shareDialog.canShow(SharePhotoContent.class)) {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                shareDialog.show(content);
            }

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper);

        imageView = (ImageView) findViewById(R.id.view_wallpaper);
        btn_wallpaper = (Button) findViewById(R.id.btn_set_wallpaper);
        btn_download = (Button) findViewById(R.id.btn_download);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        mainFolating = (FloatingActionMenu) findViewById(R.id.menu);
        fbShare = (FloatingActionButton) findViewById(R.id.fb_share);
        fbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(ViewWallpaper.this, "Share Successfull!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(ViewWallpaper.this, "share Cancelled!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(ViewWallpaper.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                Picasso.with(getBaseContext())
                        .load(Common.select_bacgrounds.getImageurl())
                        .into(facebookConvertBitmap);

            }
        });


        compositeDisposable = new CompositeDisposable();
        LocalDatabase database = LocalDatabase.getInstance(this);
        recentRepository = RecentRepository.getInstance(com.example.shikooo.wallpaper.Database.LocalDatabase.RecentDataSource.getInstance(database.recentsDAO()));




        Picasso.with(this)
                .load(Common.select_bacgrounds.getImageurl())
                .into(imageView);

        btn_wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getBaseContext())
                        .load(Common.select_bacgrounds.getImageurl())
                        .into(target);
            }
        });

        addToRecents();
        Toast.makeText(ViewWallpaper.this, "added", Toast.LENGTH_SHORT).show();


        //viewCount
        increaseviewCount();

    }

    private void increaseviewCount() {

        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .child(Common.select_background_key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("viewCount")) {
                            WallpaperItem wallpaperItem = dataSnapshot.getValue(WallpaperItem.class);
                            long count = wallpaperItem.getViewCount() + 1;
                            Map<String, Object> update_view = new HashMap<>();
                            update_view.put("viewCount", count);

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(Common.select_background_key)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(ViewWallpaper.this, "cannot update view Count ", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                        } else {

                            Map<String, Object> update_view = new HashMap<>();
                            update_view.put("viewCount", Long.valueOf(1));

                            FirebaseDatabase.getInstance()
                                    .getReference(Common.STR_WALLPAPER)
                                    .child(Common.select_background_key)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(ViewWallpaper.this, "cannot set default view Count ", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private  void addToRecents() {

        Disposable disposable = Observable.create(new ObservableOnSubscribe<Object>() {


            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {

                Recents recents = new Recents(
                        Common.select_bacgrounds.getImageurl(),
                        Common.select_bacgrounds.getCategoryId(),
                        String.valueOf(System.currentTimeMillis()));

                recentRepository.insertRecents(recents);

                e.onComplete();

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        Log.e("Error",throwable.getMessage());

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {


                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        Picasso.with(this).cancelRequest(target);
        super.onDestroy();
    }
}


