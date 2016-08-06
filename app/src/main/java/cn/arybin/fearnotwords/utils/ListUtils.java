package cn.arybin.fearnotwords.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by arybin on 16-8-6.
 */
public class ListUtils {
    public static <T> T popList(List<T> list) {
        T tmp = list.get(0);
        list.remove(0);
        return tmp;
    }

    public static <T> void swapList(List<T> listA, List<T> listB) {
        ArrayList<T> tmpList = new ArrayList<>(listA);
        listA.clear();
        listA.addAll(listB);
        listB.clear();
        listB.addAll(tmpList);
    }

    public static <T> void shuffleList(List<T> list) {
        Random random = new Random();
        int size = list.size();
        int iterIndex = 0;
        Iterator<T> iter = list.iterator();
        while (iter.hasNext()) {
            int tmpIndex = random.nextInt(size);
            T tmp = list.get(tmpIndex);
            list.set(tmpIndex, iter.next());
            list.set(iterIndex++, tmp);
        }
    }
}
