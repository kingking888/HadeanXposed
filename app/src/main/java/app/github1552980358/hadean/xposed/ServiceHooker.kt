package app.github1552980358.hadean.xposed

import android.app.Service
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

interface ServiceHooker {
    
    fun hookService(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        hookConstructor(loadPackageParam)
        hookOnCreate(loadPackageParam)
        hookOnStartCommand(loadPackageParam)
    }
    
    private fun hookConstructor(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookConstructor("android.app.Service", loadPackageParam.classLoader, object: XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                // Just throw Exception to clean stack
                throw Exception()
            }
        })
    }
    
    private fun hookOnCreate(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.app.Service",
            loadPackageParam.classLoader,
            "onCreate",
            object: XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    // Just throw Exception to clean stack
                    throw Exception()
                }
            })
    }
    
    private fun hookOnStartCommand(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.app.Service",
            loadPackageParam.classLoader, "onStartCommand",
            object: XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    
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
            })
    }
    
}