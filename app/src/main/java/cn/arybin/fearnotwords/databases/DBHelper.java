package cn.arybin.fearnotwords.databases;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by arybin on 16-8-5.
 */
public class DBHelper extends SQLiteOpenHelperEx {
    public DBHelper(Context context, String name, int version) {
        super(context, name, version);
    }

    @Override
    public ExeResult onOrigin(SQLiteDatabase db) {
        return null;
    }
}
