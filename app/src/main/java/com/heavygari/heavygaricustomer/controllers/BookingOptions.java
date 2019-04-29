package com.heavygari.heavygaricustomer.controllers;


import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heavygari.heavygaricustomer.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class BookingOptions extends Fragment {

    public BottomSheetBehavior<View> sheetBehavior;
    public View back_overlay;
    private View toolBarBottom;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.booking_options, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.rv);
        MyListAdapter adapter = new MyListAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

    }

    class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
        public MyListAdapter() {
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.test_item_1, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return 20;
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
