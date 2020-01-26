package com.example.user.receipts.shops;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.receipts.R;
import com.example.user.receipts.databaseDetails.Product;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ShopAdapter extends BaseAdapter {

    Context context;
    private final int[] imgid;

    public ShopAdapter(Activity context, int[] imgid) {
        this.context = context;
        this.imgid = imgid;
    }

    @Override
    public int getCount() {
        return imgid.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View view, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        final View result;

        if(view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.row_shop, parent, false);
            viewHolder.icon = (ImageView) view.findViewById(R.id.shopImage);

            result = view;
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
            result = view;
        }
        viewHolder.icon.setImageResource(imgid[position]);
        return view;
    }
    private static class ViewHolder {
        ImageView icon;
    }
}