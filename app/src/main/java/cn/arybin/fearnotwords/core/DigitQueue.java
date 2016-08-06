package cn.arybin.fearnotwords.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cn.arybin.fearnotwords.utils.ListUtils;

/**
 * Created by AryBin on 16-8-6.
 */
public class DigitQueue implements BaseIterator<Integer> {

    public enum Action {
        pass, skip, loop
    }

    public class Operation {
        public Action action;
        public List<Integer> queue;

        public Operation(Action action, List<Integer> queue) {
            this.action = action;
            this.queue = queue;
        }
    }

    public interface Interpolator {
        /**
         * @param instance
         *            you know this.
         * @param operation
         *            current operation
         * @param lastPopSkip
         *            how many times the method pass(or skip or loop) is called
         *            since last pop of skipQueue.
         */
        public boolean shouldPopSkip(DigitQueue instance, Operation operation, int lastPopSkip);
    }

    private int current = -1;
    private int lastPopSkip = 0;
    private Interpolator interpolator = null;

    private ArrayList<Integer> mainQueue = null;
    private ArrayList<Integer> passedQueue = null;
    private ArrayList<Integer> skippedQueue = null;

    private DigitQueue(boolean random, int size, Interpolator interpolator) throws Exception {
        mainQueue = new ArrayList<Integer>(size);
        passedQueue = new ArrayList<Integer>(size);
        skippedQueue = new ArrayList<Integer>(size);
        this.interpolator = interpolator;
        for (int i = 0; i < size; i++) {
            mainQueue.add(i);
        }
        if (random) {
            ListUtils.shuffleList(mainQueue);
        }
        current = popMain();
    }

    public static DigitQueue newInstance(boolean random, int size, Interpolator interpolator) {
        DigitQueue instance = null;
        try {
            instance = tryToCreateInstance(random, size, interpolator);
        } catch (Exception e) {
            instance = null;
            e.printStackTrace();
        }
        return instance;
    }

    private static DigitQueue tryToCreateInstance(boolean random, int size, Interpolator interpolator)
            throws Exception {
        if (interpolator == null || size < 1) {
            throw new Exception("interpolator should not be null and size should equal or greater then 1");
        }
        return new DigitQueue(random, size, interpolator);
    }

    private int popSkip() {
        lastPopSkip = 0;
        return ListUtils.popList(skippedQueue);
    }

    private int popMain() {
        return ListUtils.popList(mainQueue);
    }

    private Integer operate(Operation operation) {
        if (!isQueueBroken()) {
            operation.queue.add(current);
            if (operation.action == Action.loop) {
                current = popSkip();
            } else {
                boolean willPopSkip = interpolator.shouldPopSkip(this, operation, lastPopSkip++);
                if (willPopSkip && skippedQueue.size() > 0) {
                    current = popSkip();
                } else {
                    // will not pop skipQueue, pop mainQueue.
                    if (mainQueue.size() > 0) {
                        current = popMain();
                    } else {
                        if (skippedQueue.size() > 0) {
                            // mainQueue empty, swap mainQueue and skippedQueue
                            ListUtils.swapList(mainQueue, skippedQueue);
                            current = popMain();
                        } else {
                            // end of DigitQueue
                            current = -1;
                        }
                    }
                }
            }
        }

        return current;
    }

    public boolean isQueueBroken() {
        return (current == -1);
    }

    @Override
    public Integer current() {
        return current;
    }

    @Override
    public Integer pass() {
        return operate(new Operation(Action.pass, passedQueue));
    }

    @Override
    public Integer skip() {
        return operate(new Operation(Action.skip, skippedQueue));
    }

    @Override
    public Integer loop() {
        return operate(new Operation(Action.loop, skippedQueue));
    }

}