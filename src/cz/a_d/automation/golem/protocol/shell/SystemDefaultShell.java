/*
 */
package cz.a_d.automation.golem.protocol.shell;

/**
 * Represents program which will be used to execute default shell based on platform where program is currently running.
 *
 * @author casper
 */
public enum SystemDefaultShell {

    /**
     * Windows platform.
     */
    Windows("cmd"),
    /**
     * Unix platform.
     */
    Unix("/bin/sh"),
    /**
     * Apple Macintosh platform.
     */
    Mac("/bin/sh"),
    /**
     * Solaris platform.
     */
    Solaris("/bin/sh");

    private final String shell;

    private SystemDefaultShell(String shell) {
        this.shell = shell;
    }

    /**
     * Getter to access program used to execute default system shell.
     *
     * @return command which will be executed for accessing system default shell.
     */
    public String getShell() {
        return shell;
    }

    /**
     * Converting value from system property os.name to known operating system or null in case when system is not supported by this enum.
     *
     * @param osName string name of operating system.
     * @return instance of enum or null.
     */
    public static SystemDefaultShell getShell(String osName) {
        SystemDefaultShell retValue = null;
        if (osName != null && (!osName.isEmpty())) {
            osName = osName.toLowerCase();
            if ((osName.contains("nux")) || (osName.contains("nix")) || (osName.contains("aix"))) {
                retValue = Unix;
            } else if (osName.contains("win")) {
                retValue = Windows;
            } else if (osName.contains("mac")) {
                retValue = Mac;
            } else if (osName.contains("sunos")) {
                retValue = Solaris;
            }
        }
        return retValue;
    }
}
