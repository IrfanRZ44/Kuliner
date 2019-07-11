package com.exomatik.kuliner.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.exomatik.kuliner.R;

/**
 * Created by IrfanRZ on 6/1/2018.
 */

public class SwipeAdapter extends PagerAdapter {
    public static int[] image_resources = {R.drawable.bg1,
            R.drawable.bg2,
            R.drawable.bg3};
    public static String[] text = {"Coto Makassar",
            "Pallubasa",
            "Es Pisang Ijo"
    };

    private Context ctx;
    private LayoutInflater layoutInflater;

    public SwipeAdapter(Context ctx){
        this.ctx=ctx;
    }


    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout, container, false);
        android.widget.ImageView imageView = (ImageView) item_view.findViewById(R.id.image_view);
        android.widget.ImageView imageStart = (ImageView) item_view.findViewById(R.id.start);
        RelativeLayout rl = (RelativeLayout) item_view.findViewById(R.id.rl_swipe);
        TextView textView = (TextView) item_view.findViewById(R.id.image_count);

        imageView.setImageResource(image_resources[position]);
        textView.setText(text[position]);

        rl.removeAllViews();
        rl.addView(imageView);
        rl.addView(textView);
        rl.addView(imageStart);

        container.addView(rl);

        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }

}
