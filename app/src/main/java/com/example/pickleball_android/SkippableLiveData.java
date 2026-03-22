package com.example.pickleball_android;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class SkippableLiveData<T> extends MutableLiveData<T> {
    private boolean firstEmissionSkipped = false;

    public SkippableLiveData(T defaultValue) {
        super(defaultValue);
    }

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, t -> {
            if (!firstEmissionSkipped) {
                // skip the very first emission (initialization)
                firstEmissionSkipped = true;
                return;
            }
            observer.onChanged(t);
        });
    }
}
