package app.github1552980358.hadean.xposed

import android.app.Service
import android.content.Intent
import app.github1552980358.hadean.xposed.base.BaseHooker
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * [ServiceHooker]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/15
 * @time    : 20:54
 **/

interface ServiceHooker: BaseHooker {
    
    fun hookService(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        // hookConstructor(loadPackageParam)
        hookConstructor(
            "android.app.Service",
            loadPackageParam,
            mutableListOf()
        ) {
            // Just throw Exception to clean stack
            throw Exception()
        }
        hookOnCreate(loadPackageParam)
        hookOnStartCommand(loadPackageParam)
    }
    
    /**
     * @hide
     **/
    @Deprecated("See [hookConstructor] of [BaseHooker]")
    private fun hookConstructor(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookConstructor(
            "android.app.Service",
            loadPackageParam.classLoader,
            object: XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    // Just throw Exception to clean stack
                    throw Exception()
                }
            })
    }
    
    private fun hookOnCreate(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        replaceMethod(
            "android.app.Service",
            loadPackageParam,
            "onCreate",
            mutableListOf()
        ) {
            // Just throw Exception to clean stack
            throw Exception()
        }
        /**
         * XposedHelpers.findAndHookMethod(
         *     "android.app.Service",
         *     loadPackageParam.classLoader,
         *     "onCreate",
         *     object: XC_MethodReplacement() {
         *         override fun replaceHookedMethod(param: MethodHookParam?): Any? {
         *             // Just throw Exception to clean stack
         *             throw Exception()
         *         }
         *    })
         **/
    }
    
    private fun hookOnStartCommand(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        replaceMethod(
            "android.app.Service",
            loadPackageParam,
            "onStartCommand",
            mutableListOf(
                Intent::class.java,
                Int::class.java,
                Int::class.java
            )
        ) { param ->
            // Stop foreground
            try {
                (param?.thisObject as Service?)?.apply {
                    stopForeground(true)
                    stopSelf()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            
            // Just throw Exception to clean stack
            throw Exception()
        }
        /**
         * XposedHelpers.findAndHookMethod(
         *     "android.app.Service",
         *     loadPackageParam.classLoader, "onStartCommand",
         *     object: XC_MethodReplacement() {
         *         override fun replaceHookedMethod(param: MethodHookParam?): Any? {
         *
         *             // Stop foreground
         *             try {
         *                 (param?.thisObject as Service?)?.apply {
         *                     stopForeground(true)
         *                     stopSelf()
         *                 }
         *             } catch (e: Exception) {
         *                 e.printStackTrace()
         *             }
         *
         *             // Just throw Exception to clean stack
         *             throw Exception()
         *         }
         *     })
         **/
    }
    
}