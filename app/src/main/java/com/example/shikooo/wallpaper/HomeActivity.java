package com.example.shikooo.wallpaper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shikooo.wallpaper.Adapter.MyFragmentAdapter;
import com.example.shikooo.wallpaper.Common.Common;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ViewPager viewPager;
    TabLayout tabLayout;

    DrawerLayout drawer;
    NavigationView navigationView;

    BottomNavigationView menu_bottom;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Common.SIGN_IN_REQUEST_CODE)
        {
             if (resultCode == RESULT_OK)
            {
                Snackbar.make(drawer,new StringBuilder("Welcome ")
                        .append(FirebaseAuth.getInstance().getCurrentUser().getEmail()
                                .toString()),Snackbar.LENGTH_LONG).show();

                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISION_REQUEST_CODE);
                    }

                }
                viewPager = (ViewPager) findViewById(R.id.viewPager);
                MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),this);
                viewPager.setAdapter(adapter);

                tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                tabLayout.setupWithViewPager(viewPager);

                LoadUserInformation();


            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case Common.PERMISION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED)
                {
                    Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_home);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Live Wallpaper");
        setSupportActionBar(toolbar);



        menu_bottom = (BottomNavigationView) findViewById(R.id.navigation);
        menu_bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.getItemId() == R.id.action_upload)
                {
                    startActivity(new Intent(HomeActivity.this,UploadWallpaper.class));
                }

                return false;
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //reguest runtime permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISION_REQUEST_CODE);
            }

        }


        //check if not sign-in then navigate sign-in page
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            FirebaseApp.initializeApp(this);

            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),Common.SIGN_IN_REQUEST_CODE);
        }

        else
        {
            Snackbar.make(drawer,new StringBuilder("Welcome ")
                    .append(FirebaseAuth.getInstance().getCurrentUser().getEmail()
                        .toString()),Snackbar.LENGTH_LONG).show();
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        LoadUserInformation();


    }

    private void LoadUserInformation() {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            View headerLayout = navigationView.getHeaderView(0);
            TextView txt_email = (TextView) headerLayout.findViewById(R.id.txt_email);
            txt_email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view_upload) {
            // Handle the camera action
        }
        if (id == R.id.nav_view_fav) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
