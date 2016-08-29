package cn.arybin.fearnotwords.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import cn.arybin.fearnotwords.R;
import cn.arybin.fearnotwords.database.migration.Migration;
import cn.arybin.fearnotwords.database.migration.MigrationQueue;

public class MainActivity extends BaseActivity {

    class MyMigration extends Migration {

        public MyMigration(int fromVersion, int toVersion) {
            super(fromVersion, toVersion);
        }

        @Override
        public void migrate(SQLiteDatabase db) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MyMigration m1 = new MyMigration(1, 2);
        MyMigration m2 = new MyMigration(2, 3);
        MyMigration m3 = new MyMigration(3, 5);
        MyMigration m4 = new MyMigration(5, 7);
        MyMigration m5 = new MyMigration(1, 4);
        MyMigration m6 = new MyMigration(4, 5);


        System.out.println(MigrationQueue.build(1, 5, m1, m2, m3, m4, m5, m6).size());


    }
}
