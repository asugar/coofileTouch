package com.twinflag.coofiletouch.utils;

public class IsNullUtil {

	/**
	 * 将string类型转化为Integer类型，如果s未赋值，返回0；
	 * @param s
	 * @return
	 */
	public static Integer integerIsNull(String s) {
        if (null == s || "".equals(s)) {
            return 0;
        } else {
        	return Integer.valueOf(s);
        }
    }

	/**
	 * 判断String类型是否为空，保证未赋值的时候，将其制为空。
	 * @param s
	 * @return
	 */
    public static String stringIsNull(String s) {
        if (null == s || "".equals(s)) {
            return null;
        } else {
            return s;
        }
    }
    
    /**
     * 默认返回的是false
     * @param s
     * @return
     */
    public static Boolean booleanIsNull(String s){
    	 if (null == s || "".equals(s)) {
             return false;
         } else {
             return Boolean.valueOf(s);
         }
    }

}
