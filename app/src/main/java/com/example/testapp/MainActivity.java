package com.example.testapp;

import android.os.Bundle;

import com.example.testapp.lock.MainLock;
import com.hq.viewstack.Model.ViewModel;
import com.hq.viewstack.OnlyActivity;
          
public class MainActivity extends OnlyActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signViewName(new ViewModel("main",R.layout.activity_main,BR.mainLock));
        signViewName(new ViewModel("viewA",R.layout.view_log,0));
        startView("main",new MainLock(this));
    }
}
