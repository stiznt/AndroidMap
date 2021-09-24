package ru.stiznt.mapinkotlin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.stiznt.mapinkotlin.Models.Cabinet;
import ru.stiznt.mapinkotlin.R;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<Cabinet> {

    private Context mContext;
    int mResourse;
    ArrayList<Cabinet> arraylist;

    public SearchAdapter(Context context, int resource, ArrayList<Cabinet> objects) {
        super(context, resource, objects);
        mContext = context;
        mResourse = resource;
        arraylist = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cabinet command = getItem(position);

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResourse, parent, false);

        TextView textView = (TextView)convertView.findViewById(R.id.tv);
        textView.setText(command.getName());

        return convertView;
    }

}
