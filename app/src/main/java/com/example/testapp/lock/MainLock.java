package com.example.testapp.lock;

import android.databinding.ViewDataBinding;

import com.hq.viewstack.LocalLock;
import com.hq.viewstack.OnlyActivity;

/**
 * Created by 浩琦 on 2017/7/3.
 */

public class MainLock extends LocalLock {
    public MainLock(OnlyActivity context) {
        super(context);
    }

    public void toViewA() {
        context.startView("viewA",new ViewLog(context));
    }
}
