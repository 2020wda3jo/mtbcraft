package com.example.testapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    public MutableLiveData<String> message = new MutableLiveData<>();
    public MutableLiveData<String> courseName = new MutableLiveData<>();
}
