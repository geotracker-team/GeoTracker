package com.juanjo.udl.geotracker.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juanjo.udl.geotracker.R;

public class JSONProjectCardAdapter extends RecyclerView.Adapter<JSONProjectCardAdapter.ViewHolder>{
    private String[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProject;
        public ViewHolder(View v) {
            super(v);
            txtProject = v.findViewById(R.id.info_text);
        }
    }

    public JSONProjectCardAdapter (String[] mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public JSONProjectCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_project, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtProject.setText(mDataset[position]);

    }
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
