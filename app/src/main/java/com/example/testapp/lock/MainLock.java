package com.example.testapp.lock;

import android.databinding.ViewDataBinding;

import com.hq.viewstack.LocalLock;
import com.hq.viewstack.OnlyActivity;

/**
 * Created by 浩琦 on 2017/7/3.
 */

public class MainLock extends LocalLock {
    public MainLock(OnlyActivity context, ViewDataBinding dataBinding) {
        super(context, dataBinding);
    }

    public void toViewA() {
        context.startView("viewA",null);
    }
}
