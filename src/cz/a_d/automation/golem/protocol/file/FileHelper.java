/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.protocol.file;

import cz.a_d.automation.golem.io.FileAccessModificator;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class is static helper for managing feature RW file access by Golem URL
 * connections.
 *
 * @author casper
 */
public class FileHelper {

    /**
     * Create and initialize URL from file, also access rights are detected from
     * file object and if file is not exist then rights create is added.
     *
     * @param f file instance which will be converted to URL.
     * @return URL with query containing access rights in format expected by
     * Golem file handler.
     * @throws MalformedURLException If a protocol handler for the URL could not
     * be found, or if some other error occurred while constructing the URL
     */
    public static URL createFileURL(File f) throws MalformedURLException {
        URL retValue = null;
        if (f != null) {
            List<FileAccessModificator> access = new ArrayList<>(4);
            if (f.canRead()) {
                access.add(FileAccessModificator.READ);
            }
            if (f.canWrite()) {
                access.add(FileAccessModificator.WRITE);
            }
            if (f.canExecute()) {
                access.add(FileAccessModificator.EXECUTE);
            }
            if (!f.exists()) {
                access.add(FileAccessModificator.CREATE);
                access.add(FileAccessModificator.WRITE);
            }
            retValue = createFileURL(f, access);
        }

        return retValue;
    }

    /**
     * Create URL from file with initialized query part of URL by access rights.
     *
     * @param f file instance which will be converted to URL.
     * @param per collection of permisions asked for this specific file.
     * @return URL with query containing access rights in format expected by
     * Golem file handler.
     * @throws MalformedURLException If a protocol handler for the URL could not
     * be found, or if some other error occurred while constructing the URL
     */
    public static URL createFileURL(File f, Collection<FileAccessModificator> per) throws MalformedURLException {
        URL retValue = null;
        if (f != null) {
            URI toURI = f.toURI();
            if (toURI != null) {
                String actions = null;
                if ((per != null) && (!per.isEmpty())) {
                    actions = "access={";
                    boolean first = true;
                    for (FileAccessModificator fileAccessModificator : per) {
                        if (first) {
                            first = false;
                        } else {
                            actions += ",";
                        }
                        actions += fileAccessModificator.name().toLowerCase();
                    }
                    actions += "}";
                }
                if (actions == null) {
                    retValue = toURI.toURL();
                } else {
                    retValue = new URL(toURI.toURL().toString() + "?" + actions);
                }
            }
        }
        return retValue;
    }
}
