package com.example.shikooo.wallpaper.IntroApp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shikooo.wallpaper.R;


/**
 * Created by shikooo on 9/3/2018.
 */

public class SliderAdapter extends PagerAdapter {

    public Context context;
    public LayoutInflater layoutInflater;



    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int [] sildes_images = {
            R.drawable.credit1,
            R.drawable.card,
            R.drawable.dollars,
            R.drawable.cart
    };

    public String [] slides_heading = {
            "Credit",
            "Card",
            "Dollars",
            "Cart"
    };

    public String [] slides_desc = {
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's"
    };

    public int [] slides_background = {

            R.color.layout_background_1,
            R.color.layout_background_2,
            R.color.layout_background_3,
            R.color.layout_background_4
    };



    @Override
    public int getCount() {
        return slides_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container,false);


        ImageView imageView = (ImageView) view.findViewById(R.id.image_icon);
        TextView txt_headig = (TextView) view.findViewById(R.id.txt_heading);
        TextView txt_desc = (TextView) view.findViewById(R.id.txt_desc);
        RelativeLayout layout_background = (RelativeLayout) view.findViewById(R.id.layout_background);

        imageView.setImageResource(sildes_images[position]);
        txt_headig.setText(slides_heading[position]);
        txt_desc.setText(slides_desc[position]);
        layout_background.setBackgroundResource(slides_background[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((RelativeLayout)object);
    }
}
