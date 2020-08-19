package app.github1552980358.hadean.xposed

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import app.github1552980358.hadean.xposed.base.BaseHooker
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * [ContextHooker]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/15
 * @time    : 22:26
 **/

interface ContextHooker: BaseHooker {
    
    fun hookContext(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        hookStartActivity(loadPackageParam)
        hookStartService(loadPackageParam)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            hookStartForegroundService(loadPackageParam)
        }
    }
    
    private fun hookStartActivity(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        replaceMethod(
            "android.content.Context",
            loadPackageParam,
            "startActivity",
            mutableListOf(Intent::class.java)
        ) {  }
    
        replaceMethod(
            "android.content.Context",
            loadPackageParam,
            "startActivity",
            mutableListOf(Intent::class.java, Bundle::class.java)
        ) {  }
        
        /**
         * XposedHelpers.findAndHookMethod(
         *     "android.content.Context",
         *     loadPackageParam.classLoader,
         *     "startActivity",
         *     Intent::class.java,
         *     object: XC_MethodReplacement() {
         *         override fun replaceHookedMethod(param: MethodHookParam?): Any? {
         *             return null
         *         }
         *     })
         **/
        
        /**
         * XposedHelpers.findAndHookMethod(
         *     "android.content.Context",
         *     loadPackageParam.classLoader,
         *     "startActivity",
         *     Intent::class.java,
         *     Bundle::class.java,
         *     object: XC_MethodReplacement() {
         *         override fun replaceHookedMethod(param: MethodHookParam?): Any? {
         *             return null
         *         }
         *     })
         **/
    }
    
    private fun hookStartService(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        replaceMethod(
            "android.content.Context",
            loadPackageParam,
            "startService",
            mutableListOf(Intent::class.java)
        ) {  }
        
        /**
         * XposedHelpers.findAndHookMethod(
         *     "android.content.Context",
         *     loadPackageParam.classLoader,
         *     "startService",
         *     Intent::class.java,
         *     object: XC_MethodReplacement() {
         *         override fun replaceHookedMethod(param: MethodHookParam?): Any? {
         *             return null
         *         }
         *     })
         **/
    }
    
    @RequiresApi(Build.VERSION_CODES.O)
    private fun hookStartForegroundService(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        
        replaceMethod(
            "android.content.Context",
            loadPackageParam,
            "startForegroundService",
            mutableListOf(Intent::class.java)
        ) {  }
        
        /**
         * XposedHelpers.findAndHookMethod(
         *     "android.content.Context",
         *     loadPackageParam.classLoader,
         *     "startForegroundService",
         *     Intent::class.java,
         *     object: XC_MethodReplacement() {
         *         override fun replaceHookedMethod(param: MethodHookParam?): Any? {
         *             return null
         *         }
         *     })
         **/
    }
    
}