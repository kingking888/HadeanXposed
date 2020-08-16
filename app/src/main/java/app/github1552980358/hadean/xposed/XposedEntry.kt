package app.github1552980358.hadean.xposed

import android.app.AndroidAppHelper
import android.net.Uri
import androidx.core.database.getStringOrNull
import app.github1552980358.hadean.database.SQLHelper.Companion.COLUMN_APPLICATION_ID
import app.github1552980358.hadean.database.SQLHelper.Companion.COLUMN_ENABLED
import app.github1552980358.hadean.database.SQLHelper.Companion.DATABASE_NAME
import app.github1552980358.hadean.database.SQLHelper.Companion.TABLE_APPLICATIONS_LIST
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * [XposedEntry]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/15
 * @time    : 19:15
 **/

class XposedEntry: IXposedHookLoadPackage, ServiceHooker, BroadcastReceiverHooker, ContextHooker {
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        
        var hook = false
        
        val cursor = AndroidAppHelper.currentApplication()
            .contentResolver.query(
                Uri.parse("content://app.github1552980358.hadean/$TABLE_APPLICATIONS_LIST"),
                arrayOf(COLUMN_APPLICATION_ID, COLUMN_ENABLED),
                null,
                null,
                null
            )
        cursor?:return
        
        var index: Int
        while (cursor.moveToNext()) {
            
            index = cursor.getColumnIndex(COLUMN_APPLICATION_ID)
            
            if (index == -1) {
                cursor.close()
                return
            }
            
            try {
                if (cursor.getString(index) != lpparam.packageName) {
                    continue
                }
            } catch (e: Exception) { return }
            
            index = cursor.getColumnIndex(COLUMN_ENABLED)
            
            if (index == -1) {
                cursor.close()
                return
            }
            
            try {
                if (cursor.getInt(index) != 1) {
                    cursor.close()
                    return
                }
            } catch (e: Exception) { return }
            
            hook = true
            break
        }
        
        if (!hook) {
            return
        }
        
        hookContext(lpparam)
        hookService(lpparam)
        hookBroadcastReceiver(lpparam)
    }
    
}