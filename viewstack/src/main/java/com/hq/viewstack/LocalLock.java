package com.hq.viewstack;

import android.databinding.BaseObservable;

/**
 * Created by 浩琦 on 2017/6/30.
 */

public class LocalLock<T extends BaseObservable> {
    protected OnlyActivity context;
    protected OnlyActivity.ViewStack viewStack;
    protected String tag;
    protected T localData;
    public LocalLock(OnlyActivity context) {
        this.context = context;

    }

    protected void onCreateCache(boolean isCache){

    }

    protected void setTag(String tag){
        this.tag=tag;
    }
    public void setViewStack(OnlyActivity.ViewStack viewStack) {
        this.viewStack = viewStack;
    }




}
