package com.nearby.wardrobetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.nearby.wardrobetest.db.Topwear;

import java.util.ArrayList;
import java.util.List;

public class TopSwipeAdapter extends PagerAdapter {

    private Context mContext;
    private List<Topwear> topwears = new ArrayList<>();

    public TopSwipeAdapter(Context mContext,List<Topwear> topwears) {
        this.mContext = mContext;
        this.topwears = topwears;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.img_layout, collection, false);
        ImageView imageView = view.findViewById(R.id.imgData);

        Glide.with(mContext)
                .load(topwears.get(position).getPath())
                .into(imageView);
        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return topwears.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
