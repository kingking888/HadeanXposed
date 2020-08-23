package app.github1552980358.hadean.xposed.hooker

import android.content.Context
import android.content.Intent
import app.github1552980358.hadean.xposed.base.BaseHooker
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

interface BroadcastReceiverHooker: BaseHooker {
    
    fun hookBroadcastReceiver(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        hookConstructor(
            "android.content.BroadcastReceiver",
            loadPackageParam,
            mutableListOf()
        ) {  throw Exception() }
        hookOnReceive(loadPackageParam)
    }
    
    /**
     * @hide
     **/
    @Deprecated("See [hookConstructor] of [BaseHooker]", ReplaceWith("BaseHooker.hookConstructor"), DeprecationLevel.HIDDEN)
    private fun hookConstructor(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookConstructor("android.content.BroadcastReceiver", loadPackageParam.classLoader, object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                throw Exception()
            }
        })
    }
    
    private fun hookOnReceive(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        replaceMethod(
            "android.content.BroadcastReceiver",
            loadPackageParam,
            "onReceive",
            mutableListOf(Context::class.java, Intent::class.java)
        ) { throw Exception() }
        
        /**
         * XposedHelpers.findAndHookMethod(
         *     "android.content.BroadcastReceiver",
         *     loadPackageParam.classLoader,
         *     "onReceive",
         *     Context::class.java,
         *     Intent::class.java,
         *     object : XC_MethodReplacement() {
         *         override fun replaceHookedMethod(param: MethodHookParam?): Any? {
         *             throw Exception()
         *         }
         *      }
         * )
         **/
        
    }
    
}