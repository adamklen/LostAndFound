package com.example.ajklen.lostandfound;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ajklen on 2/6/15.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem> {

    Context context;
    int layoutResourceId;
    ListItem data[] = null;

    public ListItemAdapter(Context context, int layoutResourceId, ListItem[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public void resetData(ArrayList<ListItem> list){
        data = new ListItem[list.size()];
        for (int i=0; i<list.size(); i++){
            data[i] = list.get(i);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ListItemHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ListItemHolder();
            holder.txtItem = (TextView)row.findViewById(R.id.text_item);
            holder.txtLocation = (TextView)row.findViewById(R.id.text_location);
            holder.txtDescription = (TextView)row.findViewById(R.id.text_description);
            holder.txtDistance = (TextView)row.findViewById(R.id.text_distance);

            row.setTag(holder);
        }
        else
        {
            holder = (ListItemHolder)row.getTag();
        }

        ListItem listItem = data[position];
        holder.txtItem.setText(listItem.item);
        holder.txtLocation.setText(listItem.location);
        double d = listItem.distance;
        holder.txtDistance.setText(String.format("%.2f %s away",
                                        d > 1000 ? d/1000 : d,
                                        d > 1000 ? "km" : "m"));
        holder.txtDescription.setText(listItem.description);

        return row;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class ListItemHolder
    {
        TextView txtItem;
        TextView txtLocation;
        TextView txtDescription;
        TextView txtDistance;
    }
}
