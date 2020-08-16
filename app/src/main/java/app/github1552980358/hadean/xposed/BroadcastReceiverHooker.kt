package app.github1552980358.hadean.xposed

import android.content.Context
import android.content.Intent
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * [BroadcastReceiverHooker]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/15
 * @time    : 21:39
 **/

interface BroadcastReceiverHooker {
    
    fun hookBroadcastReceiver(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        hookConstructor(loadPackageParam)
        hookOnReceive(loadPackageParam)
    }
    
    private fun hookConstructor(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookConstructor("android.content.BroadcastReceiver", loadPackageParam.classLoader, object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                throw Exception()
            }
        })
    }
    
    private fun hookOnReceive(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.content.BroadcastReceiver",
            loadPackageParam.classLoader,
            "onReceive",
            Context::class.java,
            Intent::class.java,
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    throw Exception()
                }
            }
        )
    }
    
}