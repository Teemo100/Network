package com.soul.netutil.observer;

import android.support.annotation.NonNull;

import com.soul.netutil.base.BaseViewModel;

import io.reactivex.observers.DisposableObserver;

/**
 * description:
 * @author soul
 * Create date: 2019/7/1/001 0:33
*/
public class LifecycleObserver<T> extends DisposableObserver<T> {

    public LifecycleObserver(@NonNull BaseViewModel viewModel) {
        viewModel.add(this);
    }

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onComplete() {
    }
}
