package com.example.testapp.lock;

import com.hq.viewstack.LocalLock;
import com.hq.viewstack.OnlyActivity;

/**
 * Created by 浩琦 on 2017/7/3.
 */

public class ViewLog extends LocalLock {
    public ViewLog(OnlyActivity context) {
        super(context);
    }

}
