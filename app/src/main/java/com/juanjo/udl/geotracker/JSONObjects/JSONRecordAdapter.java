package com.juanjo.udl.geotracker.JSONObjects;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.juanjo.udl.geotracker.R;
import com.juanjo.udl.geotracker.RecordViewActivity;
import com.juanjo.udl.geotracker.Register;

import java.util.ArrayList;

/**
 * Created by David on 05/11/2017.
 */

public class JSONRecordAdapter extends ArrayAdapter {

    public JSONRecordAdapter(Context context, ArrayList<JSONRecord> registers) {
        super(context, 0, registers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item , parent, false);
        }

        final JSONRecord currentRec = (JSONRecord) getItem(position);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), RecordViewActivity.class);
                it.putExtra("record", currentRec);
                getContext().startActivity(it);
            }
        });

        TextView projectTextView = (TextView) listItemView.findViewById(R.id.project);
        projectTextView.setText(currentRec.getProjectName());

        TextView userTextView = (TextView) listItemView.findViewById(R.id.user);
        userTextView.setText(currentRec.getUserName());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(currentRec.getDate());

        TextView latitudeTextView = (TextView) listItemView.findViewById(R.id.latitude);
        latitudeTextView.setText(currentRec.getLatitude().toString());

        TextView longitudeTextView = (TextView) listItemView.findViewById(R.id.longitude);
        longitudeTextView.setText(currentRec.getLongitude().toString());

        TextView descripTextView = (TextView) listItemView.findViewById(R.id.description);
        descripTextView.setText(currentRec.getDescription());

        return listItemView;
    }

}
