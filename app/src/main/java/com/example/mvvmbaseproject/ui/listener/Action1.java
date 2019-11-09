package com.example.mvvmbaseproject.ui.listener;

import io.reactivex.functions.Action;

public interface Action1<T> extends Action {
    void call(T t);
}
