package com.example.testapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public MutableLiveData<String> message = new MutableLiveData<>();
    public MutableLiveData<String> courseName = new MutableLiveData<>();
    public MutableLiveData<String> r_Id = new MutableLiveData<>();
    public MutableLiveData<String> r_Image = new MutableLiveData<>();
    public MutableLiveData<String> r_Nickname = new MutableLiveData<>();
    public MutableLiveData<String> r_ClubName = new MutableLiveData<>();
}
