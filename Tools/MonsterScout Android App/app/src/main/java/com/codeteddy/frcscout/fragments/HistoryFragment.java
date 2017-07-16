package com.codeteddy.frcscout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codeteddy.frcscout.R;
import com.codeteddy.frcscout.adapter.HistoryAdapter;
import com.codeteddy.frcscout.utils.DatabaseHandler;
import com.codeteddy.frcscout.utils.QRCode;

import java.util.ArrayList;

/**
 * @author Alex
 *         Created by Alex on 10.03.2017.
 */

public class HistoryFragment extends Fragment {

    private View root;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private DatabaseHandler handler;
    private ArrayList<QRCode> codes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.list);
        handler = new DatabaseHandler(getActivity());
        codes = handler.getAllQRCodes();
        adapter = new HistoryAdapter(getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.LEFT) {

                    ArrayList<QRCode> arrayList = handler.getAllQRCodes();
                    QRCode code = arrayList.get(viewHolder.getAdapterPosition());

                    adapter.remove(viewHolder.getAdapterPosition());
                    handler.deleteQRCode(code);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        return root;
    }
}
