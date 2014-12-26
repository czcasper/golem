/*
 */
package cz.a_d.automation.golem.io;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author casper
 */
public class AdvancetFileURI {

    public static final String PARAMETER_SEPARATOR = ";";

    public static URI toURI(File f, FileAccessModificator... access) throws URISyntaxException {
        URI retValue = null;
        if (f != null) {
            String build = "";
            if ((access != null)&&(access.length!=0)) {
                build = "access=";
                for(FileAccessModificator m : access){
                    
                }
            }
            retValue = toURI(f, build);
        }
        return retValue;
    }

    public static URI toURI(File f, String... modificators) throws URISyntaxException {
        URI retValue = null;
        if (f != null) {
            String build = "";
            if (modificators != null) {
                boolean first = true;
                for (String param : modificators) {
                    if ((param == null) || (param.isEmpty())) {
                        continue;
                    }
                    if (first) {
                        first = false;
                    } else {
                        build += PARAMETER_SEPARATOR;
                    }
                    build += param;
                }
            }
            retValue = new URI(f.toURI().toString(), build, "");
        }
        return retValue;
    }
}
