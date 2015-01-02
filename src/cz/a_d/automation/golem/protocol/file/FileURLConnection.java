/*
 */
package cz.a_d.automation.golem.protocol.file;

import cz.a_d.automation.golem.io.FileAccessModificator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Improved version of FileConnection from URL, allowing R/W access and other features for dealing with files by using URL connection.
 *
 * @author casper
 */
public class FileURLConnection extends URLConnection {

    /**
     * File object provided by this class for I/O operation.
     */
    protected File file;

    /**
     * FileChannel instance created by connect method and covering I/O operations on top of connected file.
     */
    protected FileStreamChannel channel;
    /**
     * Set of access rights modificators.
     */
    protected Set<FileAccessModificator> rights;

    /**
     * Construct FileURL connection from given parameters, constructor is for FileURLHandler.
     *
     * @param url    - url instance connected with rwFile.
     * @param file   - rwFile instance connected with URL.
     * @param rights - rights extracted from URL.
     * @throws UnsupportedEncodingException
     */
    protected FileURLConnection(URL url, File file, FileAccessModificator[] rights) throws UnsupportedEncodingException {
        super(url);
        this.file = file;
        if (rights != null) {
            this.rights = new HashSet<>(Arrays.asList(rights));
        } else {
            this.rights = new HashSet<>(0);
        }
        connected = false;
        doInput = this.rights.contains(FileAccessModificator.READ);
        doOutput = this.rights.contains(FileAccessModificator.WRITE);
    }

    /**
     * Open streams to file specified by constructor allowed operation with stream are specified in constructor of object.
     *
     * @throws IOException
     */
    @Override
    public void connect() throws IOException {
        if (!connected) {
            if ((!file.exists()) && (!rights.contains(FileAccessModificator.CREATE))) {
                throw new IOException("Required file:" + file.toString() + " not exist and rights doesn't allow create new file");
            }
            if (file.isFile()) {
                channel = new FileStreamChannel(file, rights);
                connected = true;
            }
        }
    }

    /**
     * Get output stream for connected file.
     *
     * @return null in case when file is not writeable or connected.
     * @throws IOException
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        OutputStream retValue = null;
        if ((doOutput) && (connected) && (channel!=null)) {
            retValue = channel.getOutputStream();
        }
        return retValue;
    }

    /**
     * Get input stream for conneced file.
     *
     * @return null in case when file is not readable or connected.
     * @throws IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        InputStream retValue = null;
        if ((doInput) && (connected) && (channel!=null)) {
            retValue = channel.getInputStream();
        }
        return retValue;
    }
}
