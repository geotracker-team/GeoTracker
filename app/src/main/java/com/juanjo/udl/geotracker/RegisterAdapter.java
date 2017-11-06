package com.juanjo.udl.geotracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David on 05/11/2017.
 */

public class RegisterAdapter extends ArrayAdapter {

    public RegisterAdapter(Context context, ArrayList<Register> registers) {
        super(context, 0, registers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item , parent, false);
        }

        Register currentReg = (Register) getItem(position);

        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), RecordViewActivity.class);
                getContext().startActivity(it);
            }
        });

        TextView projectTextView = (TextView) listItemView.findViewById(R.id.project);
        projectTextView.setText(currentReg.getProjectName());

        TextView userTextView = (TextView) listItemView.findViewById(R.id.user);
        userTextView.setText(currentReg.getUser());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(currentReg.getDate());

        TextView latitudeTextView = (TextView) listItemView.findViewById(R.id.latitude);
        latitudeTextView.setText(currentReg.getLatitude().toString());

        TextView longitudeTextView = (TextView) listItemView.findViewById(R.id.longitude);
        longitudeTextView.setText(currentReg.getLongitude().toString());

        TextView descripTextView = (TextView) listItemView.findViewById(R.id.description);
        descripTextView.setText(currentReg.getDescription());

        return listItemView;
    }

}
