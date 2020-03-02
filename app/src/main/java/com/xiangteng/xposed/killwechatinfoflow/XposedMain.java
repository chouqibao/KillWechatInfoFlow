package com.xiangteng.xposed.killwechatinfoflow;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class XposedMain implements IXposedHookLoadPackage {

    private static String LOG_TAG = "KWIF";
    private static ClassLoader classloader;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.tencent.mm"))
            return;
        Log.i(LOG_TAG, "WeChat Hooked.");
        try {
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, "attach() hooked.");
                    classloader = ((Context) param.args[0]).getClassLoader();
                    hook();
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook attach() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }
    }

    private void hook() {
        /*try {
            findAndHookMethod("com.tencent.mm.storage.s", classloader, "blj", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, "com.tencent.mm.storage.s.blj() called.");
                    param.setResult(false);
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook com.tencent.mm.storage.s.blj() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }

        try {
            findAndHookMethod("com.tencent.mm.storage.s", classloader, "bvn", new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LOG_TAG, "com.tencent.mm.storage.s.bvn() called.");
                    param.setResult(false);
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook com.tencent.mm.storage.s.bvn() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }*/

        try {
            findAndHookMethod("com.tencent.mm.sdk.platformtools.az", classloader, "decodeInt", String.class, int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if (param.args[0].toString().equals("BizTimeLineOpenStatus")) {
                        Log.i(LOG_TAG, "com.tencent.mm.sdk.platformtools.az.decodeInt() called.");
                        param.setResult(0);
                    }
                }
            });
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook com.tencent.mm.sdk.platformtools.az.decodeInt() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
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
        } catch (Throwable e) {
            Log.e(LOG_TAG, "Hook startActivity() error.");
            Log.e(LOG_TAG, Log.getStackTraceString(e));
        }*/
    }
}

