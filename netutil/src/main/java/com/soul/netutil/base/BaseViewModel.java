package com.soul.netutil.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * description:
 * @author soul
 * Create date: 2019/7/1/001 0:33
*/
public class BaseViewModel extends ViewModel {

    private CompositeDisposable disposables = new CompositeDisposable();

    public static BaseViewModel get(FragmentActivity activity) {
        if (activity == null) return null;
        return ViewModelProviders.of(activity).get(BaseViewModel.class);
    }

    public static BaseViewModel get(Fragment fragment) {
        if (fragment == null) return null;
        return ViewModelProviders.of(fragment).get(BaseViewModel.class);
    }

    public void add(Object o) {
        if (o instanceof Disposable) {
            addDisposable((Disposable) o);
        } else {
            throw new IllegalArgumentException("参数有误,请传入可取消的订阅者");
        }
    }

    void addDisposable(Disposable disposable) {
        if (disposables == null) {
            disposables = new CompositeDisposable();
        }
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        if (disposables != null) {
            disposables.clear();
        }
    }
}
