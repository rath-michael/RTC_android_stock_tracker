package com.example.capstoneproject.UI.Error;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capstoneproject.R;

public class ErrorFragment extends Fragment {

    private static final String TAG = "ERROR_FRAGMENT";

    public ErrorFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_error, container, false);

        return root;
    }
}