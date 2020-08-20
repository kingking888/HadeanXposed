package app.github1552980358.hadean.receiver

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import app.github1552980358.hadean.database.DatabaseProvider.Companion.CONTEXT_URI
import app.github1552980358.hadean.database.SQLHelper.Companion.COLUMN_APPLICATION_ID
import app.github1552980358.hadean.database.SQLHelper.Companion.COLUMN_IS_LOCKED
import app.github1552980358.hadean.xposed.XposedEntry.Companion.APP_LOCKED

/**
 * [ExternalBroadcastReceiver]
 * @author  : 1552980328
 * @since   : 0.1
 * @date    : 2020/8/20
 * @time    : 14:20
 **/

class ExternalBroadcastReceiver: BroadcastReceiver() {
    
    companion object {
        
        const val ACTION_LOCK_APPLICATION = "app.github1552980358.hadean.lock"
        const val ACTION_LOCK_APPLICATION_NAME = "appName"
    }
    
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != ACTION_LOCK_APPLICATION) {
            return
        }
        
        val appName = intent.getStringExtra(ACTION_LOCK_APPLICATION_NAME) ?: return
        
        // Do locking procedures,
        // will be added later
        
        context?.contentResolver?.update(
            Uri.parse(CONTEXT_URI),
            ContentValues().apply { put(COLUMN_IS_LOCKED, APP_LOCKED) },
            "${COLUMN_APPLICATION_ID}=${appName}",
            null
        )
    }
    
}