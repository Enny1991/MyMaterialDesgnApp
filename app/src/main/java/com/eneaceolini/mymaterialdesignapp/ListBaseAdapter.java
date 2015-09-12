package com.eneaceolini.mymaterialdesignapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Vector;

/**
 * Created by Enea on 14/05/15.
 */
public class ListBaseAdapter extends BaseAdapter {

    private Context context;
    private Vector<ListObject> values;

    public ListBaseAdapter(Context context,Vector<ListObject> values){
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return values.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.list_item,parent,false);
        TextView mainText = (TextView)row.findViewById(R.id.firstLine);
        TextView secText = (TextView)row.findViewById(R.id.secondLine);
        //ImageView icon = (ImageView)row.findViewById(R.id.icon);
        mainText.setText(values.get(position).getText());
        return row;
    }
}
