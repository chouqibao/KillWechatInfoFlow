package com.xiangteng.xposed.killwechatinfoflow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class XposedMain implements IXposedHookLoadPackage {

    public static String LOG_TAG = "Xposed-KillWechatInfoFlow";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        //if (lpparam.packageName.equals("com.xiangteng.xposed.killwechatinfoflow")) {
            //Log.i(LOG_TAG, "Module Activated!");
        //}

        if (lpparam.packageName.equals("com.tencent.mm")) {
            findAndHookMethod(Activity.class, "startActivity", Intent.class, Bundle.class, new XC_MethodHook() {
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Intent intent = (Intent) param.args[0];
                    String target = intent.getComponent().getClassName();
                    if (target.equals("com.tencent.mm.plugin.brandservice.ui.timeline.BizTimeLineUI")) {
                        //Class<?> clazz = lpparam.classLoader.loadClass("com.tencent.mm.ui.conversation.NewBizConversationUI");
                        Class<?> clazz = lpparam.classLoader.loadClass("com.tencent.mm.ui.conversation.BizConversationUI");
                        param.args[0] = new Intent((Activity) param.thisObject, clazz);
                    }
                }
            });
        }
    }
}

