package app.github1552980358.hadean.xposed.hooker

import android.app.Activity
import android.app.AndroidAppHelper
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import app.github1552980358.hadean.receiver.ExternalBroadcastReceiver
import app.github1552980358.hadean.xposed.base.BaseHooker
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * [ActivityHooker]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/18
 * @time    : 15:44
 **/

interface ActivityHooker: BaseHooker {
    
    fun hookActivityLocked(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        hookConstructor("android.app.Activity", loadPackageParam, mutableListOf()) { param ->
            (param?.thisObject as Activity?)?.finish()
            throw Exception()
        }
        hookOnCreate(loadPackageParam)
    }
    
    private fun hookOnCreate(loadPackageParam: XC_LoadPackage.LoadPackageParam) =
        replaceMethod(
            "android.app.Activity",
            loadPackageParam,
            "onCreate",
            mutableListOf(Bundle::class.java)
        ) { param ->
            (param?.thisObject as Activity?)?.finish()
            throw Exception()
        }
    
    fun listenToLongBack(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.app.Activity",
            loadPackageParam.classLoader,
            "dispatchKeyEvent",
            KeyEvent::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    val keyEvent = (param?.args?.first() as KeyEvent?) ?: return
                    if (keyEvent.keyCode != KeyEvent.KEYCODE_BACK || !keyEvent.isLongPress) {
                        return
                    }
                    
                    (param?.thisObject as Activity?)?.sendBroadcast(
                        Intent(ExternalBroadcastReceiver.ACTION_LOCK_APPLICATION)
                            .putExtra(ExternalBroadcastReceiver.ACTION_LOCK_APPLICATION_NAME, AndroidAppHelper.currentPackageName())
                    )
                }
            }
        )
    }
    
}