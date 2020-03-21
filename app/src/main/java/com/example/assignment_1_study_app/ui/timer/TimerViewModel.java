package com.example.assignment_1_study_app.ui.timer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TimerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the Timer fragment");
    }

    public LiveData<String> getText() { return mText; }
}
