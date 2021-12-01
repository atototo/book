package com.bs.search.util;

public class StringUtil {
    private StringUtil() {
        throw new AssertionError();
    }
    public static String[] subStringByIdx(String str, int lenSize) {
        String[] strArr = {str};
        if (isEmpty(str) || lenSize == 0) {
            return strArr;
        }

        int len = str.length();
        strArr = new String[len / lenSize + 1];
        for (int startLen = 0; startLen <= len / lenSize; startLen++) {
            int lastLen = (startLen + 1) * lenSize;
            if (lastLen > len)
                lastLen = len;

            strArr[startLen] = str.substring(startLen * lenSize, lastLen);
        }

        return strArr;
    }

    public static boolean isEmpty(String str) {
        if (str != null) {
            int len = str.length();
            for (int i = 0; i < len; ++i) {
                if (str.charAt(i) > ' ') {
                    return false;
                }
            }
        }
        return true;
    }

}
