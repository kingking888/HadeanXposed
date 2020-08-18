package app.github1552980358.hadean.xposed

import android.app.Activity
import android.os.Bundle
import app.github1552980358.hadean.xposed.base.BaseHooker
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * [ActivityHooker]
 * @author  : 1552980328
 * @since   :
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
    
}