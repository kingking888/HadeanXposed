package app.github1552980358.hadean.database

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.content.UriMatcher.NO_MATCH
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import app.github1552980358.hadean.BuildConfig
import app.github1552980358.hadean.database.SQLHelper.Companion.DATABASE_NAME
import app.github1552980358.hadean.database.SQLHelper.Companion.TABLE_APPLICATIONS_LIST

/**
 * [DatabaseProvider]
 * @author  : 1552980328
 * @since   :
 * @date    : 2020/8/14
 * @time    : 21:16
 **/

class DatabaseProvider : ContentProvider() {

    companion object {

        const val MATCHER_APPLICATION = 0

        const val RETURN_INT_ERROR = 0

    }

    private var sqlHelper: SQLHelper? = null
    private var sqlDatabase: SQLiteDatabase? = null

    private var uriMatcher = UriMatcher(NO_MATCH).apply {
        addURI(BuildConfig.APPLICATION_ID, DATABASE_NAME, MATCHER_APPLICATION)
    }

    override fun onCreate(): Boolean {
        sqlHelper = SQLHelper(context!!, 1)
        sqlDatabase = sqlHelper?.writableDatabase
        return sqlHelper == null || sqlDatabase == null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        if (uriMatcher.match(uri) == MATCHER_APPLICATION) {
            return sqlDatabase
                ?.query(TABLE_APPLICATIONS_LIST, projection, selection, selectionArgs, null, null, sortOrder)
        }
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (uriMatcher.match(uri) == MATCHER_APPLICATION) {
            return ContentUris
                .withAppendedId(uri, sqlDatabase?.insert(TABLE_APPLICATIONS_LIST, null, values) ?: return null)
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        if (uriMatcher.match(uri) == MATCHER_APPLICATION) {
            return sqlDatabase?.delete(TABLE_APPLICATIONS_LIST, selection, selectionArgs) ?: RETURN_INT_ERROR
        }
        return RETURN_INT_ERROR
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        if (uriMatcher.match(uri) == MATCHER_APPLICATION) {
            return sqlDatabase?.update(TABLE_APPLICATIONS_LIST, values, selection, selectionArgs) ?: RETURN_INT_ERROR
        }
        return RETURN_INT_ERROR
    }

}