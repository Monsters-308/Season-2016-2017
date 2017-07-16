package com.codeteddy.frcscout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codeteddy.frcscout.MainActivity;
import com.codeteddy.frcscout.R;
import com.codeteddy.frcscout.adapter.ImageAdapter;
import com.codeteddy.frcscout.utils.Item;

import java.util.ArrayList;

/**
 * Created by Alex on 14.02.2017.
 */

public class MainFragment extends Fragment{

    private View root;
    private ListView listView;
    private ArrayList<Item> items;
    private int[] images = {R.drawable.ic_clipboard,
            R.drawable.ic_menu_camera,
            R.drawable.ic_message,
            R.drawable.ic_history};
    private String[] names = {"Match Scouting", "Pit Scouting", "Commenting", "History"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);
        listView = (ListView)root.findViewById(R.id.list);
        items = new ArrayList<>();
        for (int i = 0; i < names.length;i++){
            Item item = new Item(names[i], images[i]);
            items.add(item);
        }
        ImageAdapter adapter = new ImageAdapter(getActivity(), items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MainActivity activity = (MainActivity)getActivity();
                if(activity instanceof  MainActivity){
                    switch (i){
                        case 0:
                            activity.setFragment(1);
                            break;
                        case 1:
                            activity.setFragment(2);
                            break;
                        case 2:
                            activity.setFragment(3);
                            break;
                        case 3:
                            activity.setFragment(4);
                            break;
                    }
                }

            }
        });


        return root;
    }
}
