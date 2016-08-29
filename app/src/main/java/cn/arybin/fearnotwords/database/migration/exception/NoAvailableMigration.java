package cn.arybin.fearnotwords.database.migration.exception;

/**
 * Created by arybin on 16-8-29.
 */
public class NoAvailableMigration extends  MigrationException {
    public int fromVersion;
    public int toVersion;
    public NoAvailableMigration(int fromVersion,int toVersion){
        this.fromVersion=fromVersion;
        this.toVersion=toVersion;
    }
}
