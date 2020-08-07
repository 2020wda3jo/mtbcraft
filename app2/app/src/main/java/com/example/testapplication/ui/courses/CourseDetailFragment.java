package com.example.testapplication.ui.courses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testapplication.R;
import com.example.testapplication.ui.BaseFragment;

public class CourseDetailFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model.courseName.observe(getViewLifecycleOwner(), name->{
            if ( name != null ) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
                appCompatActivity.getSupportActionBar().setTitle(name);
            }
        });
    }
}