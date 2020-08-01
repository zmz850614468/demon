package com.lilanz.tooldemo.prints.hanYin;

public class CheckClick {
    private static final long CLICK_DELAY = 1000;
    private static long mOldClickTime;

    /**
     * 防止连续点击
     *
     * @return
     */
    public static boolean isClickEvent() {
        long time = System.currentTimeMillis();
        if (time - mOldClickTime < CLICK_DELAY)
            return false;

        mOldClickTime = time;
        return true;
    }
}
