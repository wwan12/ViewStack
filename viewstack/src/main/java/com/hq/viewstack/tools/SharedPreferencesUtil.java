package com.hq.viewstack.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 类描述：SharePreference存储工具类
 * sp工具类，存储时会有两个xml文件，我们一般是存储在USERCONFIG.xml中，经常修改的文件
 * 而SYSCONFIG.xml是修改频率较少，只有两个方法会用到saveStringData（），getStringData（）
 * 修改备注：
 */
public  class SharedPreferencesUtil {
    private final String tag = "SharedPreferencesUtil";
    private static Context mcontext;
    public final static String SYSCONFIG = "sysconfig";
    public final static String USERCONFIG = "userconfig";

    /**
     * 存储长时有效数据
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static void saveStringData(Context context, String key, String value) {
        if (context != null) {
            mcontext = context;
        }
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(SYSCONFIG,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * 获取长时有效数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getStringData(Context context, String key,
                                       String defValue) {
        if (context != null) {
            mcontext = context;
        }
        SharedPreferences	sharedPreferences = mcontext.getSharedPreferences(SYSCONFIG,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defValue);
    }

    // 保存boolean 类型
    public static void saveSharePreBoolean(Context context, String key,
                                           Boolean value) {
        SharedPreferences sp = context.getSharedPreferences(USERCONFIG,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    // 保存int 类型
    public static void saveSharePreInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(USERCONFIG,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getSharePreInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(USERCONFIG,
                Context.MODE_PRIVATE);
        return sp.getInt(key, value);
    }

    /**
     * 存储经常修改有效数据
     *
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static void saveSharePreString(Context context, String key, String value) {
        if (context != null) {
            mcontext = context;
        }
        SharedPreferences	sharedPreferences = mcontext.getSharedPreferences(USERCONFIG,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).commit();
    }
    /**
     * 获取token等易变数据
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getSharePreString(Context context, String key,
                                           String defValue) {
        if (context != null) {
            mcontext = context;
        }
        SharedPreferences sp = mcontext.getSharedPreferences(USERCONFIG,
                Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    /**
     * 布尔类型数据
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static boolean getSharePreBoolean(Context context, String key,
                                             Boolean value) {
        SharedPreferences sp = context.getSharedPreferences(USERCONFIG,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, value);
    }

}
