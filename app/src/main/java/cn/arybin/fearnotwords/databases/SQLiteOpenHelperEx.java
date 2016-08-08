package cn.arybin.fearnotwords.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import cn.arybin.fearnotwords.databases.migration.Migration;

/**
 * Created by arybin on 16-8-8.
 * Note:
 * SQLiteOpenHelperEx enhance Google's SQLiteOpenHelper with RAILS-LIKE migration mechanism!!!
 * Usage:override onOrigin method to create first version of your database, AND DO NOT MODIFY
 * THIS METHOD EVEN IF THE STRUCTURE OF DATABASE CHANGE IN THE FUTURE.
 * Why:
 * Instead of building new database structure directly for every new version(use onCreate), the
 * SQLiteOpenHelperEx use migration to migrate both database and data in database. In this way,
 * the update of database will be compatible to all potentially distributed database version.
 * JUST:OVERRIDE onOrigin (it is optional to override onResult or onMigrate for callback)
 */
public abstract class SQLiteOpenHelperEx extends SQLiteOpenHelper {
    private static final int VERSION_ORIGIN = 0;
    private boolean isOrigin;
    private int targetVersion;
    private ArrayList<Migration> migrations;

    public SQLiteOpenHelperEx(Context context, String name, int version) {
        super(context, name, null, version);
        this.targetVersion = version;
        this.migrations = new ArrayList<Migration>();
    }

    public abstract ExeResult onOrigin(SQLiteDatabase db);

    public void onResult(ExeResult res) {
    }

    public void onMigrate(Migration currentMigration, float completePercentage) {
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        synchronized (this) {
            isOrigin = true;
        }
        onResult(onOrigin(sqLiteDatabase));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        autoMigrate(sqLiteDatabase);
    }

    public ExeResult migrate(int targetVersion) {

        return null;
    }

    public boolean canMigrate(int fromVersion, int toVersion) {
        return getMigrationsReverse(fromVersion, toVersion) != null;
    }

    public ArrayList<Migration> getMigrations(int fromVersion, int toVersion) {
        ArrayList<Migration> migrations = getMigrationsReverse(fromVersion, toVersion);
        if (migrations == null) {
            return null;
        } else {
            Collections.reverse(migrations);
            return migrations;
        }
    }

    public ArrayList<Migration> getMigrationsReverse(int fromVersion, int toVersion) {
        if (fromVersion == toVersion) {
            return new ArrayList<Migration>();
        }
        ArrayList<Migration> properMigrations = new ArrayList<Migration>();
        Iterator<Migration> tmpIterator = null;
        Migration tmpMigration = null;

        ArrayList<Migration> potentialMigrations = new ArrayList<Migration>();
        tmpIterator = migrations.iterator();
        while (tmpIterator.hasNext()) {
            tmpMigration = tmpIterator.next();
            if (tmpMigration.toVersion == toVersion && tmpMigration.fromVersion >= fromVersion) {
                potentialMigrations.add(tmpMigration);
            }
        }
        tmpIterator = potentialMigrations.iterator();
        while (tmpIterator.hasNext()) {
            tmpMigration = tmpIterator.next();
            ArrayList<Migration> tmpMigrations = getMigrations(fromVersion, tmpMigration.fromVersion);
            if (tmpMigrations != null) {
                properMigrations.addAll(tmpMigrations);
                return properMigrations;
            }
        }
        return null;
    }

    public void addMigration(Migration migration) {
        migrations.add(migration);
    }

    public ArrayList<Migration> getAllMigrations() {
        return migrations;
    }

    ;

    private ExeResult doMigrate(SQLiteDatabase db, Migration migration) {

        if (migration.fromVersion != db.getVersion()) {
            return new ExeResult.Fail("The database's version is not equal with migration's fromVersion");
        }

        db.beginTransaction();
        ExeResult res = migration.migrate(db);
        if (res.succeed) {
            db.setVersion(migration.toVersion);
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        return res;
    }

    private void originate(SQLiteDatabase db) {
        db.setVersion(VERSION_ORIGIN);
    }

    private void checkOrigin(SQLiteDatabase db) {
        synchronized (this) {
            if (isOrigin) {
                originate(db);
                isOrigin = false;
            }
        }
    }

    private void autoMigrate(SQLiteDatabase db) {
        ArrayList<Migration> migrations = getMigrations(db.getVersion(), targetVersion);
        if (migrations != null) {
            int iterateCount = 0;
            int migrationCount = migrations.size();
            Iterator<Migration> iterator = migrations.iterator();
            while (iterator.hasNext()) {
                Migration migration = iterator.next();
                onMigrate(migration, 1l * iterateCount++ / migrationCount);
                ExeResult res = doMigrate(db, migration);
                onResult(res);
                if (!res.succeed) {
                    break;
                }
            }
        }
    }

    private SQLiteDatabase processDB(SQLiteDatabase db) {
        checkOrigin(db);
        autoMigrate(db);
        return db;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return processDB(super.getReadableDatabase());
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return processDB(super.getWritableDatabase());
    }
}