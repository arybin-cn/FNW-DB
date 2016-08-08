package cn.arybin.fearnotwords.databases;

/**
 * Created by arybin on 16-8-8.
 */
public class ExeResult {
    public boolean succeed;
    public String detail;

    public ExeResult(boolean succeed, String detail) {
        this.succeed = succeed;
        this.detail = detail;
    }


    public static class Fail extends ExeResult {
        public Fail(String detail) {
            super(false, detail);
        }
    }

    public static class Succeed extends ExeResult {
        public Succeed(String detail) {
            super(true, detail);
        }
    }
}
