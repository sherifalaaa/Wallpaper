package com.example.shikooo.wallpaper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shikooo.wallpaper.Common.Common;
import com.example.shikooo.wallpaper.Model.CategoryItem;
import com.example.shikooo.wallpaper.Model.WallpaperItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UploadWallpaper extends AppCompatActivity {

    Button btn_upload,btn_browser;
    ImageView imageView_preview;
    MaterialSpinner spinner;

    Map<String,String> spinnerData = new HashMap<>();
    private Uri filePath;

    String categoryIdSelected = "";

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_wallpaper);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imageView_preview = (ImageView) findViewById(R.id.image_perview);
        btn_browser= (Button) findViewById(R.id.btn_browser);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);

        loadCategoryToSpinner();

        btn_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner.getSelectedIndex() == 0)
                {
                    Toast.makeText(UploadWallpaper.this, "Please choose category", Toast.LENGTH_SHORT).show();
                }
                else {
                    upload();
                }
            }
        });

    }

    private void upload() {

        if(filePath != null)
        {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Uploading... ");
            dialog.show();

            StorageReference ref = storageReference.child(new StringBuilder("images/").append(UUID.randomUUID().toString()).toString());
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    dialog.dismiss();
                    saveUrlToCategory(categoryIdSelected,taskSnapshot.getDownloadUrl().toString());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(UploadWallpaper.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress  = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    dialog.setMessage("Upload : "+(int)progress+"");
                }
            });
        }
    }

    private void saveUrlToCategory(String categoryIdSelected, String imageLink) {

        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPER)
                .push()
                .setValue(new WallpaperItem(imageLink,categoryIdSelected))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadWallpaper.this, "Success! ", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
        ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Common.PICK_IMAGE_REQUEST  && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                imageView_preview.setImageBitmap(bitmap);
                btn_upload.setEnabled(true);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture: "),Common.PICK_IMAGE_REQUEST);
    }

    private void loadCategoryToSpinner() {

        FirebaseDatabase.getInstance()
                .getReference(Common.STR_CATEGORY_BACKGROUND)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                        {
                            CategoryItem item = postSnapShot.getValue(CategoryItem.class);
                            String key = postSnapShot.getKey();

                            spinnerData.put(key,item.getName());
                        }

                        //custom hint to material spinner
                        Object [] valueArray = spinnerData.values().toArray();
                        List<Object> valueList = new ArrayList<>();
                        valueList.add(0,"Category"); // we will add first item to hint
                        valueList.addAll(Arrays.asList(valueArray)); // and add all remain category name
                        spinner.setItems(valueList); //set source data for spinner
                        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                // when user choose category , we will get categoryId (key)

                                Object [] keyArray = spinnerData.keySet().toArray();
                                List<Object> keyList = new ArrayList<>();
                                keyList.add(0,"Category_key");
                                keyList.addAll(Arrays.asList(keyArray));
                                categoryIdSelected = keyList.get(position).toString(); //assign key when user choose category

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
