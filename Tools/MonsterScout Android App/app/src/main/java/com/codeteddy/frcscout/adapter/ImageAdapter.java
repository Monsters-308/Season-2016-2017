package com.codeteddy.frcscout.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codeteddy.frcscout.R;
import com.codeteddy.frcscout.utils.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 12.03.2017.
 */

public class ImageAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> items;
    private Context context;

    public ImageAdapter(@NonNull Context context, @NonNull ArrayList<Item> objects) {
        super(context, 0, objects);
        this.items = objects;
        this.context = context;
    }

    @Nullable
    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_imagelist, parent, false);
        }

        TextView textView = (TextView)convertView.findViewById(R.id.textView);
        textView.setText(items.get(position).getName());
        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
        imageView.setImageResource(items.get(position).getImage());

        return convertView;
    }
}
