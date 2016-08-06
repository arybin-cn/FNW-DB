package cn.arybin.fearnotwords.core;

/**
 * Created by arybin on 16-8-6.
 */
public interface BaseIterator<T> {
    public T current();
    public T pass();
    public T skip();
    public T loop();
}
