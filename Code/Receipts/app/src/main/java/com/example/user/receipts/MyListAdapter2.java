package com.example.user.receipts;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.receipts.databaseDetails.ExtraInformation;
import com.example.user.receipts.databaseDetails.Product;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter2 extends ArrayAdapter {
    List list = new ArrayList();

    public MyListAdapter2(Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler {
        TextView shop, product, price;
    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    //@NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View row = view;
        LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout2, parent, false);
            layoutHandler = new LayoutHandler();
            layoutHandler.product = (TextView) row.findViewById(R.id.productDisplay);
            layoutHandler.price = (TextView) row.findViewById(R.id.priceProductDisplay);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) row.getTag();
        }
        ExtraInformation e = (ExtraInformation) this.getItem(position);
        layoutHandler.product.setText(e.get_extraname());
        layoutHandler.price.setText(e.get_expirydate());

        return row;
    }
}
