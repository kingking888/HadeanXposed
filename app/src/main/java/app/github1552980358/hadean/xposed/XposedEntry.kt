package app.github1552980358.hadean.xposed

import android.app.AndroidAppHelper
import android.net.Uri
import app.github1552980358.hadean.BuildConfig
import app.github1552980358.hadean.database.DatabaseProvider.Companion.CONTEXT_URI
import app.github1552980358.hadean.database.SQLHelper.Companion.COLUMN_APPLICATION_ID
import app.github1552980358.hadean.database.SQLHelper.Companion.COLUMN_IS_LOCKED
import app.github1552980358.hadean.database.SQLHelper.Companion.COLUMN_LOCK_POLICY
import app.github1552980358.hadean.xposed.hooker.ActivityHooker
import app.github1552980358.hadean.xposed.hooker.ApplicationHooker
import app.github1552980358.hadean.xposed.hooker.BroadcastReceiverHooker
import app.github1552980358.hadean.xposed.hooker.ContextHooker
import app.github1552980358.hadean.xposed.hooker.ServiceHooker
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * [XposedEntry]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/15
 * @time    : 19:15
 **/

class XposedEntry: IXposedHookLoadPackage,
    ContextHooker, ApplicationHooker, ActivityHooker, ServiceHooker, BroadcastReceiverHooker {
    
    companion object {
        
        const val APP_UNLOCKED = 0
        const val APP_LOCKED = 1
        
        const val LOCK_POLICY_LOCK_EXITED = 0
        const val LOCK_POLICY_LOCK_MANUALLY = 1
        const val LOCK_POLICY_LOCK_SCREEN_OFF_INSTANT = 2
        const val LOCK_POLICY_LOCK_SCREEN_OFF_DELAY_5_MIN = 3
        const val LOCK_POLICY_LOCK_SCREEN_OFF_DELAY_10_MIN = 4
    
    }
    
    override fun handleLoadPackage(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        
        if (loadPackageParam.packageName == BuildConfig.APPLICATION_ID) {
            return
        }
        
        val cursor = AndroidAppHelper.currentApplication()
            .contentResolver.query(
                Uri.parse(CONTEXT_URI),
                arrayOf(COLUMN_APPLICATION_ID, COLUMN_IS_LOCKED, COLUMN_LOCK_POLICY),
                "$COLUMN_APPLICATION_ID=${loadPackageParam.packageName}",
                null,
                null
            )
        cursor?:return
        
        cursor.moveToFirst()
        var index: Int
        do {
            index = cursor.getColumnIndex(COLUMN_APPLICATION_ID)
            if (index == -1) {
                return
            }
            try {
                if (cursor.getString(index) != loadPackageParam.packageName) {
                    continue
                }
            } catch (e: Exception) {
                return
            }
            
            index = cursor.getColumnIndex(COLUMN_IS_LOCKED)
            if (index == -1) {
                return
            }
            try {
                // Prevent 3rd party application activation
                if (cursor.getInt(index) == APP_LOCKED) {
                    appLocked(loadPackageParam)
                    return
                }
            } catch (e: Exception) {
                return
            }
    
            index = cursor.getColumnIndex(COLUMN_LOCK_POLICY)
            if (index == -1) {
                return
            }
            try {
                appUnlocked(loadPackageParam, cursor.getInt(index))
            } catch (e: Exception) {
                return
            }
        } while (cursor.moveToNext())
        
        cursor.close()
        
    }
    
    private fun appLocked(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        hookContext(loadPackageParam)
        hookActivityLocked(loadPackageParam)
        hookService(loadPackageParam)
        hookBroadcastReceiver(loadPackageParam)
    }
    
    private fun appUnlocked(loadPackageParam: XC_LoadPackage.LoadPackageParam, policy: Int) {
        when (policy) {
            LOCK_POLICY_LOCK_EXITED -> {  }
            LOCK_POLICY_LOCK_SCREEN_OFF_INSTANT -> killInstant(loadPackageParam)
            LOCK_POLICY_LOCK_SCREEN_OFF_DELAY_5_MIN -> killDelay5Min(loadPackageParam)
            LOCK_POLICY_LOCK_SCREEN_OFF_DELAY_10_MIN -> killDelay10Min(loadPackageParam)
            LOCK_POLICY_LOCK_MANUALLY -> {  }
            else -> {  }
        }
    }
    
}