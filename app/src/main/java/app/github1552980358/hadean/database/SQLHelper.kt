package app.github1552980358.hadean.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * [SQLHelper]
 * @author  : 1552980328
 * @since   :
 * @date    : 2020/8/14
 * @time    : 14:36
 **/

class SQLHelper(context: Context, version: Int):
    SQLiteOpenHelper(context, DATABASE_NAME, null, version) {
    
    companion object {
        
        const val DATABASE_NAME = "hadean.db"
        
        const val TABLE_APPLICATIONS_LIST = "Applications"
        
        const val COLUMN_APPLICATION_ID = "id"
        const val COLUMN_IS_LOCKED = "is_locked"
        const val COLUMN_LOCK_POLICY = "lock_policy"
        
    }
    
    override fun onCreate(db: SQLiteDatabase) {
        db.beginTransaction()
        db.execSQL(
            "CREATE TABLE $TABLE_APPLICATIONS_LIST ( $COLUMN_APPLICATION_ID VARCHAR(100) NOT NULL PRIMARY KEY, $COLUMN_IS_LOCKED BOOLEAN NOT NULL , $COLUMN_LOCK_POLICY INTEGER NOT NULL )"
        )
    }
    
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {  }
    
    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {  }
    
}