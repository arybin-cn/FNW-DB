package cn.arybin.fearnotwords.database.migration;

import android.database.sqlite.SQLiteDatabase;

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
     * make sure to throw a MigrationException if current migration fail.
     */
    public abstract void migrate(SQLiteDatabase db);
}
