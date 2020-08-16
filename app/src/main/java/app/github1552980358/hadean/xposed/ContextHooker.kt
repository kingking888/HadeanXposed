package app.github1552980358.hadean.xposed

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * [ContextHooker]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/15
 * @time    : 22:26
 **/

interface ContextHooker {
    
    fun hookContext(lpparm: XC_LoadPackage.LoadPackageParam) {
        hookStartActivity(lpparm)
        hookStartService(lpparm)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hookStartForegroundService(lpparm)
        }
    }
    
    private fun hookStartActivity(lpparm: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.content.Context",
            lpparm.classLoader,
            "startActivity",
            Intent::class.java,
            object: XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    return null
                }
            })
        XposedHelpers.findAndHookMethod(
            "android.content.Context",
            lpparm.classLoader,
            "startActivity",
            Intent::class.java,
            Bundle::class.java,
            object: XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    return null
                }
            })
    }
    
    private fun hookStartService(lpparm: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.content.Context",
            lpparm.classLoader,
            "startService",
            Intent::class.java,
            object: XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    return null
                }
            })
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    private fun hookStartForegroundService(lpparm: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.content.Context",
            lpparm.classLoader,
            "startForegroundService",
            Intent::class.java,
            object: XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    return null
                }
            })
    }
    
}