package com.heavygari.heavygaricustomer.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heavygari.heavygaricustomer.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnDrawerInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentDrawer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDrawer extends Fragment {

    private OnDrawerInteractionListener mListener;

    public FragmentDrawer() {
        // Required empty public constructor
    }


    public static FragmentDrawer newInstance() {
        FragmentDrawer fragment = new FragmentDrawer();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrawerInteractionListener) {
            mListener = (OnDrawerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDrawerInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnDrawerInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
