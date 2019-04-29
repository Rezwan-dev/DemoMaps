package com.heavygari.heavygaricustomer.controllers;


import android.os.Bundle;
import android.support.annotation.NonNull;
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
public class SearchLocation extends Fragment {

    public BottomSheetBehavior<View> sheetBehavior;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_searchlocation, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.rv);
        MyListAdapter adapter = new MyListAdapter();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        sheetBehavior = ((ActivityHome)getActivity()).sheetBehavior;

    }

    class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
        public MyListAdapter() {
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            switch (viewType) {
                case 0: {
                    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                    View listItem = layoutInflater.inflate(R.layout.test_item_h_1, parent, false);
                    ViewHolder viewHolder = new ViewHolder(listItem);
                    return viewHolder;
                }
                case 1: {
                    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                    View listItem = layoutInflater.inflate(R.layout.test_item, parent, false);
                    ViewHolder viewHolder = new ViewHolder(listItem);
                    return viewHolder;
                }
                case 2: {
                    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                    View listItem = layoutInflater.inflate(R.layout.test_item_h_2, parent, false);
                    listItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        }
                    });
                    ViewHolder viewHolder = new ViewHolder(listItem);
                    return viewHolder;
                }
            }
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemViewType(int position) {
            if(position == 0){
                return 0;
            }
            if(position == getItemCount() -1){
                return 2;
            }
            return 1;
        }

        @Override
        public int getItemCount() {
            return 6;
        }

        public  class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
