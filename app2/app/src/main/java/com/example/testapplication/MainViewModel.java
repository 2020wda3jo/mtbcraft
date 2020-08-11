package com.example.testapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public MutableLiveData<String> courseName = new MutableLiveData<>();
    public MutableLiveData<String> r_Id = new MutableLiveData<>();
    public MutableLiveData<String> r_Image = new MutableLiveData<>();
    public MutableLiveData<String> r_Nickname = new MutableLiveData<>();
    public MutableLiveData<String> r_ClubName = new MutableLiveData<>();

    public MutableLiveData<String> comp_num = new MutableLiveData<>();
    public MutableLiveData<String> comp_period = new MutableLiveData<>();
    public MutableLiveData<String> comp_badge = new MutableLiveData<>();
    public MutableLiveData<String> comp_course = new MutableLiveData<>();
    public MutableLiveData<String> comp_image = new MutableLiveData<>();
    public MutableLiveData<String> comp_content = new MutableLiveData<>();
    public MutableLiveData<String> comp_name = new MutableLiveData<>();
    public MutableLiveData<String> save_path = new MutableLiveData<>();
    public MutableLiveData<String> c_gpx = new MutableLiveData<>();
    public MutableLiveData<String> c_name = new MutableLiveData<>();
    public MutableLiveData<String> comp_point = new MutableLiveData<>();

}
