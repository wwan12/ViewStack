package com.hq.viewstack.Model;

/**
 * Created by 浩琦 on 2017/7/3.
 *
 * 单个view tag
 */

public class ViewModel {
    public ViewModel(String tag, int resId, int brId) {
        this.tag = tag;
        this.resId = resId;
        this.brId = brId;
//        this.stackId=stackId;
    }
//    public ViewModel(int resId, int brId) {
//        this.resId = resId;
//        this.brId=brId;
//    }

    public String tag;
    public int resId;
    public int brId;
}
