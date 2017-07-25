package com.hq.viewstack;

import android.databinding.BaseObservable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by 浩琦 on 2017/6/30.
 */

public class LocalLock<T extends BaseObservable> {
    protected OnlyActivity activity;
    protected OnlyActivity.ViewStack viewStack;
    //页面绑定数据
    protected T localData;
    protected Animation apiInAnimation, apiOutAnimation, LeftInAnimation, rightOutAnimation;

    public LocalLock(OnlyActivity activity) {
        this.activity = activity;
        init();
    }

    private void init() {

    }

    /**
     * 初始化完成调用
     */
    protected void onCreate() {

    }
    protected void stop(){

    };
    protected void reStart(){

    };
    protected void des(){

    };
    /**
     * 是否开启动画 默认开
     *
     * @param onAni
     */
    protected void openAni(boolean onAni) {
        if (onAni){
            initAni();
        }
    }


    private void initAni() {
        apiInAnimation = AnimationUtils.loadAnimation(activity, R.anim.apl_in_view);
        apiInAnimation.setFillAfter(true);
        apiOutAnimation = AnimationUtils.loadAnimation(activity, R.anim.apl_out_view);
        apiOutAnimation.setFillAfter(true);
        LeftInAnimation = AnimationUtils.loadAnimation(activity, R.anim.left_in_view);
        LeftInAnimation.setFillAfter(true);
        rightOutAnimation = AnimationUtils.loadAnimation(activity, R.anim.right_out_view);
        rightOutAnimation.setFillAfter(true);
        viewStack.viewData.getRoot().startAnimation(LeftInAnimation);
    }

    protected void onCreateCache(boolean isCache) {

    }

    public void setViewStack(OnlyActivity.ViewStack viewStack) {
        this.viewStack = viewStack;
    }


}
