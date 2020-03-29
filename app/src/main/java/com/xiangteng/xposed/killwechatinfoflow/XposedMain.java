package com.xiangteng.xposed.killwechatinfoflow;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class XposedMain implements IXposedHookLoadPackage {

    private static String LOG_TAG = "KWIF";
    private static ClassLoader classloader;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (!lpparam.packageName.equals("com.tencent.mm"))
            return;
        if (!lpparam.processName.equals("com.tencent.mm"))
            return;
        Log.i(LOG_TAG, "WeChat Hooked.");
        try {
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) {
                    Log.i(LOG_TAG, "attach() hooked.");
                    classloader = ((Context) param.args[0]).getClassLoader();
                    hook();
                }
            });
        } catch (Throwable t) {
            Log.e(LOG_TAG, "Hook attach() error.", t);
        }
    }

    private void hook() {
        try {
            Class mmkvClass = classloader.loadClass("com.tencent.mmkv.MMKV");
            XC_MethodHook callBack = new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    if (param.args[0].equals("BizTimeLineOpenStatus")) {
                        Log.i(LOG_TAG, "MMKV get config BizTimeLineOpenStatus called.");
                        param.setResult(1);
                        XposedHelpers.callMethod(param.thisObject, "putInt", "BizTimeLineOpenStatus", 1);
                    }
                }
            };

            try {
                findAndHookMethod(mmkvClass, "decodeInt", String.class, callBack);
                Log.i(LOG_TAG, "Successfully hooked com.tencent.mmkv.MMKV.decodeInt(String).");
            } catch (Throwable t) {
                Log.e(LOG_TAG, "Hook com.tencent.mmkv.MMKV.decodeInt(String) error.", t);
            }

            try {
                findAndHookMethod(mmkvClass, "decodeInt", String.class, int.class, callBack);
                Log.i(LOG_TAG, "Successfully hooked com.tencent.mmkv.MMKV.decodeInt(String, int).");
            } catch (Throwable t) {
                Log.e(LOG_TAG, "Hook com.tencent.mmkv.MMKV.decodeInt(String, int) error.", t);
            }

            try {
                findAndHookMethod(mmkvClass, "getInt", String.class, int.class, callBack);
                Log.i(LOG_TAG, "Successfully hooked com.tencent.mmkv.MMKV.getInt(String, int).");
            } catch (Throwable t) {
                Log.e(LOG_TAG, "Hook com.tencent.mmkv.MMKV.getInt(String, int) error.", t);
            }
        } catch (Throwable t) {
            Log.e(LOG_TAG, "hook() error.", t);
        }
    }
}

