package cn.arybin.fearnotwords.database.migration;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by arybin on 16-8-29.
 */
public class MigrationQueue extends Migration {
    private ArrayList<Migration> migrations = null;

    private MigrationQueue(int fromVersion, int toVersion, ArrayList<Migration> migrations) {
        super(fromVersion, toVersion);
        this.migrations = migrations;
    }

    public static ArrayList<MigrationQueue> build(int fromVersion, int toVersion, List<Migration> sourceMigrations) {
        return build(fromVersion, toVersion, sourceMigrations.toArray(new Migration[]{}));
    }

    public boolean push(Migration migration) {
        if (migration.fromVersion == toVersion) {
            migrations.add(migration);
            this.toVersion = migration.toVersion;
            return true;
        }
        return false;
    }

    public boolean unshift(Migration migration) {
        if (migration.toVersion == fromVersion) {
            migrations.add(0, migration);
            this.fromVersion = migration.fromVersion;
            return true;
        }
        return false;
    }

    public static MigrationQueue create(Migration migration) {
        ArrayList<Migration> migrations = new ArrayList<Migration>();
        migrations.add(migration);
        return new MigrationQueue(migration.fromVersion, migration.toVersion, migrations);
    }

    /**
     * Retrieve all available MigrationQueues covers fromVersion to toVersion.
     *
     * @return null if there is not any available MigrationQueue.
     */
    public static ArrayList<MigrationQueue> build(int fromVersion, int toVersion, Migration... sourceMigrations) {
        ArrayList<MigrationQueue> listOfQueue = null;
        if (fromVersion == toVersion) {
            //For recursive invocation.
            return new ArrayList<MigrationQueue>();
        }
        for (Migration migration : sourceMigrations) {
            if (migration.fromVersion == fromVersion && migration.toVersion <= toVersion) {
                if (listOfQueue == null) {
                    listOfQueue = new ArrayList<MigrationQueue>();
                }
                ArrayList<MigrationQueue> tmpListOfQueue = build(migration.toVersion, toVersion, sourceMigrations);
                if (tmpListOfQueue != null && tmpListOfQueue.size() > 0) {
                    Iterator<MigrationQueue> i = tmpListOfQueue.iterator();
                    while (i.hasNext()) {
                        MigrationQueue tmpQueue = i.next();
                        if (tmpQueue.unshift(migration)) {
                            listOfQueue.add(tmpQueue);
                        }
                    }
                } else if (tmpListOfQueue != null) {
                    listOfQueue.add(MigrationQueue.create(migration));
                } else {
                    //Potentially Broken Queue
                    return null;
                }
            }
        }
        return listOfQueue;
    }


    @Override
    public void migrate(SQLiteDatabase db) {
        for (Migration migration : migrations) {
            migration.migrate(db);
        }
    }
}
