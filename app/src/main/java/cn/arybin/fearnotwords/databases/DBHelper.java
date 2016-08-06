package cn.arybin.fearnotwords.databases;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by arybin on 16-8-5.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static DBHelper getInstance(Context context, String name, int version) {
          DBHelper instance = null;
        try{
            tryToGetInstance(context,name,version);
        }catch (Exception e){
            instance = null;
            e.printStackTrace();
        }
        return instance;
    }

    private static DBHelper tryToGetInstance(Context context, String name, int version) throws Exception {
        //Default cursor factory
        return new DBHelper(context,name,null,version);
    }


    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.beginTransaction();

        //insert rows here

        sqLiteDatabase.endTransaction();


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
