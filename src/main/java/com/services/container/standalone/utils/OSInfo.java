/**
 * 
 */
package com.services.container.standalone.utils;

import com.services.container.standalone.model.OSType;

/**
 * @author Fabrizio Torelli (hellgate75@gmail.com)
 *
 */
public class OSInfo {
    private static String OS = System.getProperty("os.name").toLowerCase();

    /**
     * Verify if Operating System is Windows
     * @return flag of verification
     */
    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    /**
     * Verify if Operating System is Mac OS
     * @return flag of verification
     */
    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    /**
     * Verify if Operating System is Unix
     * @return flag of verification
     */
    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    /**
     * Verify if Operating System is Solaris
     * @return flag of verification
     */
    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }
    /**
     * Retrieve Operating System type
     * @return {@link OSType} reflecting current Operating System
     */
    public static OSType getOS(){
        if (isWindows()) {
            return OSType.WINDOWS;
        } else if (isMac()) {
            return OSType.MACOSX;
        } else if (isUnix()) {
            return OSType.LINUX;
        } else if (isSolaris()) {
            return OSType.SOLARIS;
        } else if (OS.trim().length()>0) {
            return OSType.OTHER;
	    } else {
	        return OSType.ERROR;
	    }
    }

}
