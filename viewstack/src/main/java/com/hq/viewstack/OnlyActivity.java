package com.hq.viewstack;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;

import com.hq.viewstack.Model.ViewModel;
import com.hq.viewstack.databinding.MainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import dalvik.system.DexFile;

public class OnlyActivity extends AppCompatActivity {
    private LinkedList<ViewStack> viewStacks;
    private LinkedList<ViewModel> allViewName;
    private MainBinding mainBinding;
    private HashMap<Integer, Object> runTimeData;
    private HashMap<Integer, Object> temporaryData;
    private boolean allAni;
    private LocalLock lock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.main);
        init();
        bindViewName("com.example.testapp");
    }


    private void init() {
        viewStacks = new LinkedList<>();
        allViewName = new LinkedList<>();
        runTimeData = new HashMap(20);
        allAni=true;

    }

    protected void signViewName(ViewModel model) {
        allViewName.add(model);
    }


    /**
     * 单个View缓存
     */
    public class ViewStack {
        public ViewStack(String tag,LocalLock lock) {
            for (ViewModel viewModel : allViewName) {
                if (viewModel.tag.equals(tag)) {
                    ViewDataBinding viewData = DataBindingUtil.inflate(LayoutInflater.from(OnlyActivity.this), viewModel.resId, null, false);
                    this.viewData = viewData;
                    this.tag = tag;
                    this.resId = viewModel.resId;
                    this.lock=lock;
                }else {
                    new ViewNotFindException("没有添加这个View到列表中");
                }
            }
        }

        public ViewStack(int resId) {
            for (ViewModel viewModel : allViewName) {
                if (viewModel.resId == resId) {
                    ViewDataBinding viewData = DataBindingUtil.inflate(LayoutInflater.from(OnlyActivity.this), resId, null, false);
                    this.viewData = viewData;
                    this.resId = resId;
                }
            }
        }

        public int resId;
        public String tag;
        public ViewDataBinding viewData;
        public LocalLock lock;
    }


    /**
     * 跳转
     * 多次跳转只有一份缓存
     * tag view_tag, lock 绑定类对象
     */
    public void startView(String tag, LocalLock lock) {
        mainBinding.main.removeAllViews();
        ViewStack viewStack = findView(tag);
        if (viewStack == null) {
            addNewView(tag,lock);
        } else {
            loadExistView(viewStack);
        }
        finalView();
    }


    private void addNewView(String tag,LocalLock lock){
        ViewStack viewStack = new ViewStack(tag,lock);
        viewStacks.add(viewStack);
        mainBinding.main.addView(viewStack.viewData.getRoot());
        if (lock != null) {
            lock.setViewStack(viewStack);
            lock.openAni(allAni);
            lock.onCreate();
            viewStack.viewData.setVariable(findBrId(tag), lock);
        }
        this.lock=lock;
    }

    private void loadExistView(ViewStack viewStack){
        mainBinding.main.addView(viewStack.viewData.getRoot());
        this.lock=viewStack.lock;
        viewStack.lock.reStart();
    }
    private void finalView(){
        if (temporaryData==null)return;
        temporaryData.clear();
        temporaryData=null;
    }

//    public View startView(int resId, boolean isCache) {
//        mainBinding.main.removeAllViews();
//        for (int i = 0; i < viewStacks.size(); i++) {
//            if (findView(resId) == null) {
//                ViewStack stack = new ViewStack(resId);
//                mainBinding.main.addView(stack.viewData.getRoot());
//                if (isCache) {
//                    viewStacks.add(stack);
//                }
//                return stack.viewData.getRoot();
//            } else {
//                mainBinding.main.addView(viewStacks.get(i).viewData.getRoot());
//                viewStacks.add(viewStacks.get(i));
//                return viewStacks.get(i).viewData.getRoot();
//            }
//        }
//        return null;
//    }


    @Override
    protected void onStop() {
        super.onStop();
        if (lock!=null){
            lock.stop();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (lock!=null){
            lock.reStart();
        }
    }

    /**
     * 回退
     * 回退一个view
     */
    public void backView() {
        if (viewStacks.size() - 2 < 0) {
            finish();
        } else {
            startView(viewStacks.get(viewStacks.size() - 2).tag, null);
            removeView(viewStacks.get(viewStacks.size() - 1).tag);
            lock.des();
        }
    }

    /**
     * 删除一个view（不会在回退到）
     */
    public void removeView(String tag) {
        for (int i = 0; i < viewStacks.size(); i++) {
            if (viewStacks.get(i).tag.equals(tag)) {
                viewStacks.remove(i);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backView();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * */

    private ViewStack findView(String tag) {
        for (ViewStack viewStack : viewStacks) {
            if (viewStack.tag != null && viewStack.tag.equals(tag)) {
                return viewStack;
            }
        }
        return null;
    }

    private int findBrId(String tag) {
        for (ViewModel model : allViewName) {
            if (model.tag.equals(tag)) {
                return model.brId;
            }
        }
        return 0;
    }

//    private View findView(int resId) {
//        for (ViewStack viewStack : viewStacks) {
//            if (viewStack.resId == resId) {
//                return viewStack.viewData.getRoot();
//            }
//        }
//        return null;
//    }

    /**
     * 是否开启动画 默认开
     *
     * @param allAni
     */
    public void setAllAni(boolean allAni) {
        this.allAni = allAni;
    }

    @Override
    public void onTrimMemory(int level) {
//        for (int i=viewStacks.size()-1;i>0;i--){
//            viewStacks.remove(i);
//        }
        super.onTrimMemory(level);
    }

    /*
    * 整个应用运行时数据
    * */
    public void addRunTimeData(int id, Object data) {
        runTimeData.put(id, data);
    }

    public void removeRunTimeData(int id) {
        runTimeData.remove(id);
    }

    /*
 * 单个View临时数据(大对象)
 * 跳转后删除
 * */
    public void addTemporaryData(int id, Object data) {
        if (temporaryData==null){
            temporaryData = new HashMap<>();
        }
        temporaryData.put(id, data);
    }

    public void removeTemporaryData(int id) {
        if (temporaryData==null){
            temporaryData = new HashMap<>();
        }
        temporaryData.remove(id);
    }

    protected void bindViewName(String viewPath) {
        List<String> classNameList = getClassName(viewPath);
        for (int i = 0; i < classNameList.size(); i++) {
            Log.e("hjo", "获取到的类名：" + classNameList.get(i));
        }
    }

    public List<String> getClassName(String packageName) {
        List<String> classNameList = new ArrayList<String>();
        try {
            DexFile df = new DexFile(this.getPackageCodePath());//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式  
            while (enumeration.hasMoreElements()) {//遍历  
                String className = enumeration.nextElement();
                if (className.contains(packageName)) {//在当前所有可执行的类里面查找包含有该包名的所有类  
                    classNameList.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classNameList;
    }





    private class ViewNotFindException extends Exception{
        public ViewNotFindException(String message) {
            super(message);
        }
    }

}
