package app.github1552980358.hadean.xposed

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import app.github1552980358.hadean.receiver.ExternalBroadcastReceiver.Companion.ACTION_LOCK_APPLICATION
import app.github1552980358.hadean.xposed.base.BaseHooker
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * [ApplicationHooker]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/20
 * @time    : 14:08
 **/

interface ApplicationHooker: BaseHooker {
    
    fun listenToScreen(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.app.Application",
            loadPackageParam.classLoader,
            "onCreate",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    (param?.thisObject as Application?)?.registerReceiver(
                        object : BroadcastReceiver() {
                            override fun onReceive(context: Context?, intent: Intent?) {
                                // Filtering
                                if (intent?.action != Intent.ACTION_SCREEN_OFF) {
                                    return
                                }
                                
                                // Call to lock and kill
                                context?.sendBroadcast(Intent(ACTION_LOCK_APPLICATION))
                                // Remove this receiver
                                context?.unregisterReceiver(this)
                                // Kill app and clear VM stack
                                throw Exception()
                            }
                        },
                        IntentFilter(Intent.ACTION_SCREEN_OFF)
                    )
                }
            }
        )
    }
    
}