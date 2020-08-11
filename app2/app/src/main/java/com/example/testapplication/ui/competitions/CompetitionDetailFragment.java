package com.example.testapplication.ui.competitions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.ui.BaseFragment;

public class CompetitionDetailFragment extends BaseFragment {

    private TextView compContent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_competition_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compContent = view.findViewById(R.id.comp_content);
        compContent.setText(model.comp_content.getValue());
    }
}