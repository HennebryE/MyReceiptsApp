package com.example.user.receipts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.receipts.databaseDetails.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReceiptDisplayListAdapter extends ArrayAdapter {

    List list = new ArrayList();

    public ReceiptDisplayListAdapter(Context context, int resource) {
        super(context, resource);
    }

    static class LayoutHandler {
        TextView receiptNum, shop, date, price;
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

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        View row = view;
        ReceiptDisplayListAdapter.LayoutHandler layoutHandler;
        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout, parent, false);
            layoutHandler = new ReceiptDisplayListAdapter.LayoutHandler();
            layoutHandler.receiptNum = (TextView) row.findViewById(R.id.textReceipt);
            layoutHandler.shop = (TextView) row.findViewById(R.id.textShop);
            layoutHandler.date = (TextView) row.findViewById(R.id.textDate);
            layoutHandler.price = (TextView) row.findViewById(R.id.textPrice);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (ReceiptDisplayListAdapter.LayoutHandler) row.getTag();
        }
        Product p = (Product) this.getItem(position);
        layoutHandler.receiptNum.setText(p.getReceiptID());
        layoutHandler.shop.setText(p.getShopName());
        layoutHandler.date.setText(p.getDateDB());
        layoutHandler.price.setText(String.valueOf(p.getPrice()));

        return row;
    }
}
