package cn.arybin.fearnotwords.databases.migration;

import android.database.sqlite.SQLiteDatabase;

import cn.arybin.fearnotwords.databases.ExeResult;

/**
 * Created by arybin on 16-8-8.
 */
public abstract class Migration {
    public int fromVersion;
    public int toVersion;

    public Migration(int fromVersion, int toVersion) {
        this.fromVersion = fromVersion;
        this.toVersion = toVersion;
    }

    /**
     * make sure to return a ExeResult whose field "success" is true if migration is successful!*
     */
    public abstract ExeResult migrate(SQLiteDatabase db);
}
