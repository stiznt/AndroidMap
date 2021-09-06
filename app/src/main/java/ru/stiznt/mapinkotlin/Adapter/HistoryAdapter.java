package ru.stiznt.mapinkotlin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.stiznt.mapinkotlin.Models.Cabinet;
import ru.stiznt.mapinkotlin.R;

import java.util.ArrayList;

//TODO: Убирать часы при вводе поискового запроса
public class HistoryAdapter extends ArrayAdapter<Cabinet> {

    private Context mContext;
    int mResourse;
    ArrayList<Cabinet> arraylist;
    //ImageView watch;

    public HistoryAdapter(Context context, int resource, ArrayList<Cabinet> objects) {
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
        //watch = (ImageView) convertView.findViewById(R.id.watch);
        textView.setText(command.getName());

        return convertView;
    }

    /*public void setInvisible(){
        watch.setVisibility(View.INVISIBLE);
    }

    public void setVisible(){
        watch.setVisibility(View.VISIBLE);
    }*/
}
