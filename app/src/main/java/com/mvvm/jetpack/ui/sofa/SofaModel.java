package com.mvvm.jetpack.ui.sofa;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SofaModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SofaModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is sofa fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}