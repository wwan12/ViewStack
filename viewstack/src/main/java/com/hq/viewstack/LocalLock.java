package com.hq.viewstack;

import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;

/**
 * Created by 浩琦 on 2017/6/30.
 */

public class LocalLock<T extends BaseObservable> {
    protected OnlyActivity context;
    protected ViewDataBinding dataBinding;
    protected String tag;

    public LocalLock(OnlyActivity context, ViewDataBinding dataBinding) {
        this.context = context;
        this.dataBinding=dataBinding;
    }

    protected T localData;

    protected void onCreateCache(boolean isCache){

    }

    protected void setTag(String tag){
        this.tag=tag;
    }
}
