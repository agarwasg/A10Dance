package com.pp.a10dance.helper;

public class LogUtils {

    /**
     * Generates a standardized tag for the logger.
     * 
     * @param clazz
     *            the {@link Class} to generate the tag for
     * @return a tag to use for the logger
     */
    public static String getTag(@SuppressWarnings("rawtypes") Class clazz) {
        return clazz.getSimpleName();
    }

}
