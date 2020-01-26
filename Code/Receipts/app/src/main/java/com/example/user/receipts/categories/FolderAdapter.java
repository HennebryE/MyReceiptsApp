package com.example.user.receipts.categories;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.receipts.databaseDetails.Categories;
import com.example.user.receipts.R;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder>{

    Context context;
    List<Categories> folderList;
    int rowLayout;
    ItemClickListener clickListener;

    public FolderAdapter(Context context, List<Categories> folderList, int rowLayout) {
        this.context = context;
        this.folderList = folderList;
        this.rowLayout = rowLayout;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_folder, null);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        Categories categories = folderList.get(position);

        holder.folder.setText(categories.getFolder());
        holder.itemView.setTag(categories.getFolderID());
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView folder;

        public FolderViewHolder(View view) {
            super(view);

            folder = view.findViewById(R.id.folderRow);
            view.setTag(view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

}
