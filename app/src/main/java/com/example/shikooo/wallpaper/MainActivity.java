package com.example.shikooo.wallpaper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shikooo.wallpaper.IntroApp.PrefManager;
import com.example.shikooo.wallpaper.IntroApp.SliderAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager  sliderViewPager;
    private LinearLayout mDotsLayout;
    private TextView [] mDots;

    private Button btn_back;
    private Button btn_next;
    private RelativeLayout relativeLayout;

    private PrefManager prefManager;

    private int mCurrentPage;

    SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        SqlScoutServer.create(this, getPackageName());

        setContentView(R.layout.activity_main);*/


        changeStatusBarColor();

        sliderViewPager = (ViewPager) findViewById(R.id.view_pager_intro);
        mDotsLayout = (LinearLayout) findViewById(R.id.dots_layout);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_next = (Button) findViewById(R.id.btn_next);
        relativeLayout = (RelativeLayout)  findViewById(R.id.main_background_layout);



        sliderAdapter = new SliderAdapter(this);
        sliderViewPager.setAdapter(sliderAdapter);

        addDotsIndictor(0);
        sliderViewPager.addOnPageChangeListener(viewListener);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderViewPager.setCurrentItem(mCurrentPage +1);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderViewPager.setCurrentItem(mCurrentPage -1);
            }
        });

    }

    private void launchHomeScreen()
    {
       // prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(MainActivity.this, HomeActivity.class));
        finish();
    }

    private void changeStatusBarColor()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void addDotsIndictor (int position)
    {
        mDots = new TextView[4];
        mDotsLayout.removeAllViews();

        for (int i = 0 ; i < mDots.length ; i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorWhiteTransparent));
            mDotsLayout.addView(mDots[i]);
        }

        if (mDots.length > 0)
        {
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));

        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndictor(position);
            mCurrentPage = position;

            if (position == 0)
            {
                btn_next.setEnabled(true);
                btn_back.setEnabled(false);
                btn_back.setVisibility(View.INVISIBLE);

                btn_next.setText("Next");
                btn_back.setText("");
            }
            else if (position == mDots.length-1)
            {
                btn_next.setEnabled(true);
                btn_back.setEnabled(true);
                btn_back.setVisibility(View.VISIBLE);

                btn_next.setText("Finish");
                btn_back.setText("Back");

                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        launchHomeScreen();

                    }
                });
            }
            else
            {
                btn_next.setEnabled(true);
                btn_back.setEnabled(true);
                btn_back.setVisibility(View.VISIBLE);

                btn_next.setText("Next");
                btn_back.setText("Back");
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
