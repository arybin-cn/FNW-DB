package cn.arybin.fearnotwords.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Iterator;

import cn.arybin.fearnotwords.database.migration.Migration;
import cn.arybin.fearnotwords.database.migration.MigrationQueue;
import cn.arybin.fearnotwords.database.migration.exception.MigrationException;
import cn.arybin.fearnotwords.database.migration.exception.NoAvailableMigration;

/**
 * Created by arybin on 16-8-29.
 */
public abstract class SQLiteHelper extends SQLiteOpenHelper {
    private ArrayList<Migration> migrations = null;

    public SQLiteHelper(Context context, String name, int version) {
        this(context, name, null, version);
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        initFields();
    }

    private void initFields() {
        migrations = new ArrayList<Migration>();
    }

    public ArrayList<Migration> getAllMigrations() {
        return migrations;
    }

    public void addMigration(Migration migration) {
        migrations.add(migration);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onMigrate(oldVersion, newVersion);
        ArrayList<MigrationQueue> migrationQueues = MigrationQueue.build(oldVersion, newVersion, migrations);
        if (migrationQueues != null) {
            onFindAvailableMigrationQueues(migrationQueues);
            Iterator<MigrationQueue> i = migrationQueues.iterator();
            while (i.hasNext()) {
                boolean succeed = true;
                MigrationQueue currentAttemptQueue = i.next();
                onMigrationQueue(currentAttemptQueue);
                try {
                    currentAttemptQueue.migrate(db);
                } catch (MigrationException e) {
                    succeed = false;
                    throw e;
                } finally {
                    if (succeed) {
                        onMigrationQueueSucceed(currentAttemptQueue);
                        break;
                    }
                }

            }

        } else {
            throw new NoAvailableMigration(oldVersion, newVersion);
        }
    }

    /**
     * optional hook methods below.
     */
    public void onMigrate(int fromVersion, int toVersion) {
    }

    public void onFindAvailableMigrationQueues(ArrayList<MigrationQueue> queues) {
    }

    public void onMigrationQueue(MigrationQueue queue) {
    }

    public void onMigrationQueueSucceed(MigrationQueue queue) {
    }

}
