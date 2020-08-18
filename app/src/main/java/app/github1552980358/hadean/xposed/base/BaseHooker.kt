package app.github1552980358.hadean.xposed.base

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * [BaseHooker]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/18
 * @time    : 15:49
 **/

interface BaseHooker {
    
    fun <T> hookConstructor(
        className: String,
        loadPackageParam: XC_LoadPackage.LoadPackageParam,
        classes: Array<Any>,
        block: (param: XC_MethodHook.MethodHookParam?) -> T?
    ) = hookConstructor(className, loadPackageParam, classes.toMutableList(), block)
    
    fun <T> hookConstructor(
        className: String,
        loadPackageParam: XC_LoadPackage.LoadPackageParam,
        classes: MutableList<Any>,
        block: (param: XC_MethodHook.MethodHookParam?) -> T?
    ) {
        XposedHelpers.findAndHookConstructor(
            className,
            loadPackageParam.classLoader,
            classes.apply {
                add(object: XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                        return block(param)
                    }
                })
            }
        )
    }
    
    fun <T> replaceMethod(
        className: String,
        loadPackageParam: XC_LoadPackage.LoadPackageParam,
        methodName: String,
        classes: Array<Any>,
        block: (param: XC_MethodHook.MethodHookParam?) -> T?
    ) = replaceMethod(className, loadPackageParam, methodName, classes.toMutableList(), block)
    
    fun <T> replaceMethod(
        className: String,
        loadPackageParam: XC_LoadPackage.LoadPackageParam,
        methodName: String,
        classes: MutableList<Any>,
        block: (param: XC_MethodHook.MethodHookParam?) -> T?
    ) {
        XposedHelpers.findAndHookMethod(
            className,
            loadPackageParam.classLoader,
            methodName,
            classes.apply {
                add(object: XC_MethodReplacement() {
                    override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                        return block(param)
                    }
                })
            })
    }
    
}