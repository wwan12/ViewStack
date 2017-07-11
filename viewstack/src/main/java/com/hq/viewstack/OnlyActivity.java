package com.hq.viewstack;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.hq.viewstack.Model.ViewModel;
import com.hq.viewstack.databinding.MainBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import dalvik.system.DexFile;

public class OnlyActivity extends AppCompatActivity {
    private ArrayList<ViewStack> viewStacks;
    private ArrayList<ViewModel> allViewName;
    private MainBinding mainBinding;
    private HashMap<Integer, Object> runTimeData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.main);
        init();
        bindViewName("com.example.testapp");
    }


    private void init() {
        viewStacks = new ArrayList<>(10);
        allViewName = new ArrayList<>(32);
        runTimeData = new HashMap(20);

    }

    protected void signViewName(ViewModel model) {
        allViewName.add(model);
    }


    /**
     * 单个View缓存
     */
    public class ViewStack {
        public ViewStack(String tag) {
            for (ViewModel viewModel : allViewName) {
                if (viewModel.tag.equals(tag)) {
                    ViewDataBinding viewData = DataBindingUtil.inflate(LayoutInflater.from(OnlyActivity.this), viewModel.resId, null, false);
                    this.viewData = viewData;
                    this.tag = tag;
                    this.resId = viewModel.resId;
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
    }


    /**
     * 跳转
     * 多次跳转只有一份缓存(次序会被记录)
     * tag view_tag, Cache 绑定类对象
     */
    public View startView(String tag, LocalLock lock) {
        mainBinding.main.removeAllViews();
        ViewStack viewStack = findView(tag);
        if (viewStack == null) {
            viewStack = new ViewStack(tag);
            mainBinding.main.addView(viewStack.viewData.getRoot());
            viewStacks.add(viewStack);
            if (lock != null) {
                lock.setTag(tag);
                lock.setViewStack(viewStack);
                viewStack.viewData.setVariable(findBrId(tag), lock);
            }
            return viewStack.viewData.getRoot();
        } else {
            mainBinding.main.addView(viewStack.viewData.getRoot());
//                viewStacks.add(viewStack);
            return viewStack.viewData.getRoot();
        }
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

    private View findView(int resId) {
        for (ViewStack viewStack : viewStacks) {
            if (viewStack.resId == resId) {
                return viewStack.viewData.getRoot();
            }
        }
        return null;
    }

    @Override
    public void onTrimMemory(int level) {
//        for (int i=viewStacks.size()-1;i>0;i--){
//            viewStacks.remove(i);
//        }
        super.onTrimMemory(level);
    }

    public void addRunTimeData(int id, Object data) {
        runTimeData.put(id, data);
    }
    public void removeRunTimeData(int id){
        runTimeData.remove(id);
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
}
