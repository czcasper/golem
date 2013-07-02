/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.context.actionInterfaces;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Provides a connection to a FTP server with basic operations available.
 *
 * @author pasol01
 */
public interface RunConnection {

    /**
     * Disconnects from the FTP server.
     *
     * @throws IOException if there is a connection problem
     */
    public void close() throws IOException;

    /**
     * Returns {@code true} if the connection is open.
     *
     * @return {@code true} if the connection is open
     */
    public boolean isOpen();

    /**
     * Uploads a file represented by the given {@code InputStream} to the FTP
     * server. The {@code InputStream} must be closed after this method.
     *
     * @param input input from the file
     * @param remoteLocation name of the remote file location
     * @return {@code true} if the transfer was successful
     * @throws IOException if there is a connection problem
     */
    public boolean uploadFile(ReadableByteChannel input, String remoteLocation) throws IOException;

    /**
     * Uploads a file represented by the given {@code File} to the FTP server.
     *
     * @param localFile local file to be uploaded
     * @param remoteLocation name of the remote file location
     * @return {@code true} if the transfer was successful
     * @throws IOException if there is a connection problem
     */
    public boolean uploadFile(File localFile, String remoteLocation) throws IOException;

    /**
     * Returns an {@code InputStream} from the remote file represented by the
     * given {@code String}. The stream needs to be closed manually when no
     * longer needed.
     *
     * @param remoteLocation name of the remote file
     * @return stream from the remote file
     * @throws IOException if there is a connection problem
     */
    //public InputStream downloadFile(String remoteLocation) throws IOException;
    
    public ReadableByteChannel downloadFile(String remoteLocation) throws IOException;

    /**
     * Downloads content of the given remote file to the given local file,
     * returns {@code true} if successful.
     *
     * @param localFile file on the local disk
     * @param remoteLocation name of the remote file
     * @return {@code true} if the transfer was successful
     * @throws IOException if there was a connection problem
     */
    public boolean downloadFile(File localFile, String remoteLocation) throws IOException;

    /**
     * Executes the given command on the server.
     *
     * @param command command to execute
     * @return return code
     * @throws IOException if there was a connection problem
     */
    public int execute(String command) throws IOException;

    /**
     * Returns the last reply {@code String} from the server.
     *
     * @return reply from the server
     */
    public String getReplyString();

    /**
     *
     * @return
     */
    public String[] getReplyStrings();

    /**
     * Returns a list of all files in the working directory on the server.
     *
     * @param pathname 
     * @return list of files
     * @throws IOException if there is a connection problem
     */
    //TODO create interface remote file and define all needed methods
//    public FTPFile[] listFiles(String pathname) throws IOException;

    /**
     * Downloads the content of the given remote file to the given
     * {@code OutputStream}. Doesn't close the {@code OutputStream}.
     *
     * @param filePathRemote name of the remote file
     * @param local local stream
     * @return {@code true} if the transfer was successful
     * @throws IOException if there was a connection problem
     */
    public boolean downloadFileToOStream(String filePathRemote, WritableByteChannel local) throws IOException;
    
    /**
     *
     * @return
     */
    public boolean completePendingCommand();
}
