package com.nearby.wardrobetest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.nearby.wardrobetest.db.Bottomwear;
import com.nearby.wardrobetest.db.Topwear;

import java.util.ArrayList;
import java.util.List;

public class BottomSwipeAdapter extends PagerAdapter {

    private Context mContext;
    private List<Bottomwear> bottomwears = new ArrayList<>();

    public BottomSwipeAdapter(Context mContext,List<Bottomwear> bottomwears) {
        this.mContext = mContext;
        this.bottomwears = bottomwears;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.img_layout, collection, false);
        ImageView imageView = view.findViewById(R.id.imgData);

        Glide.with(mContext)
                .load(bottomwears.get(position).getPath())
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
        return bottomwears.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
