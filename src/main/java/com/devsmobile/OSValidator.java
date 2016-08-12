package com.devsmobile;

/**
 * Thanks to Mykong
 * https://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
 */
public class OSValidator {

    private static String OS = System.getProperty("os.name").toLowerCase();

    /**
     * Check if the OS is Windows
     * @return
     */
    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    /**
     * Check if the OS is Mac
     * @return
     */
    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    /**
     * Check if the OS is Unix/Linux
     * @return
     */
    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    /**
     * Check if the OS is Solaris
     * @return
     */
    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }

}
