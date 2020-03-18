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
            findAndHookMethod("com.tencent.mmkv.MMKV", classloader, "decodeInt", long.class, String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    if (param.args[1].equals("BizTimeLineOpenStatus")) {
                        Log.i(LOG_TAG, "com.tencent.mmkv.MMKV.decodeInt(\"BizTimeLineOpenStatus\") called.");
                        param.setResult(0);
                        XposedHelpers.callMethod(param.thisObject, "putInt", "BizTimeLineOpenStatus", 0);
                    }
                }
            });
        } catch (Throwable t) {
            Log.e(LOG_TAG, "Hook com.tencent.mmkv.MMKV.decodeInt() error.", t);
        }

        /*try {
            findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Intent intent = (Intent) param.args[0];
                    String target = intent.getComponent().getClassName();
                    if (target.equals("com.tencent.mm.plugin.brandservice.ui.timeline.BizTimeLineUI")) {
                        //Class<?> clazz = lpparam.classLoader.loadClass("com.tencent.mm.ui.conversation.NewBizConversationUI");
                        Class<?> clazz = classloader.loadClass("com.tencent.mm.ui.conversation.BizConversationUI");
                        param.args[0] = new Intent((Activity) param.thisObject, clazz);
                    }
                }
            });
        } catch (Throwable t) {
            Log.e(LOG_TAG, "Hook startActivity() error.", t);
        }*/
    }
}

