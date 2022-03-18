package com.example.examplemod.util;

public class EnvironmentUtils {

    /**
     * True if is in debug environment - forge version is MOD_DEV
     */
    public static final boolean IS_DEBUG;

    static {
        String property = System.getProperty("sun.java.command");
        IS_DEBUG = property != null && property.contains("MOD_DEV");
    }
}
