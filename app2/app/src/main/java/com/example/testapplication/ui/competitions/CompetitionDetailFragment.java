package com.example.testapplication.ui.competitions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testapplication.R;
import com.example.testapplication.ui.BaseFragment;
import com.squareup.picasso.Picasso;

public class CompetitionDetailFragment extends BaseFragment {

    private TextView compName, compDay, compContent;
    private ImageView compImage;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_competition_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compName = view.findViewById(R.id.comp_name2);
        compDay = view.findViewById(R.id.comp_day2);
        compContent = view.findViewById(R.id.comp_content);
        compImage = view.findViewById(R.id.imageView6);

        compName.setText(model.comp_name.getValue());
        compDay.setText(model.comp_period.getValue());
        compContent.setText(model.comp_content.getValue());
        Picasso.get().load("http://13.209.229.237:8080/app/getGPX/comp/" + model.comp_image.getValue())
                .into(compImage);
    }
}