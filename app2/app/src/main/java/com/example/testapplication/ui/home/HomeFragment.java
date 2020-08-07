package com.example.testapplication.ui.home;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.testapplication.R;
import com.example.testapplication.ui.BaseFragment;
import com.squareup.picasso.Picasso;

import java.io.File;

public class HomeFragment extends BaseFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String Save_Path = requireContext().getFilesDir().getPath();

        model.message.observe(getViewLifecycleOwner(), message->{
            Log.i("Home", message);
        });

        SharedPreferences auto = requireContext().getSharedPreferences("auto", Activity.MODE_PRIVATE);
        String r_image = auto.getString("r_image", "");

        ImageView img = view.findViewById(R.id.imageView);
/*        Picasso.get().load("http://13.209.229.237:8080/app/getGPX/mission/missionbg1.png")
                .into(img);*/
        Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(new File(Save_Path + "/" + r_image)));
        img.setImageBitmap(myBitmap);
    }
}