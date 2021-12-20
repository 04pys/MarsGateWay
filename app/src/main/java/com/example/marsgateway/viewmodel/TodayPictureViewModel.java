package com.example.marsgateway.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TodayPictureViewModel extends ViewModel {
    public MutableLiveData<String> titleTv = new MutableLiveData<>();
    public MutableLiveData<String> explanationTv = new MutableLiveData<>();
}
