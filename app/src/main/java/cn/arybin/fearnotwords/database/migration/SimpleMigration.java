package cn.arybin.fearnotwords.database.migration;

/**
 * Created by arybin on 16-8-8.
 * SimpleMigration is Migration that migrate database from currentVersion to (currentVersion+1)
 */
public abstract class SimpleMigration extends Migration {
    public SimpleMigration(int currentVersion) {
        super(currentVersion, currentVersion + 1);
    }

}
