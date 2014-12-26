/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.protocol.file;

import cz.a_d.automation.golem.io.FileAccessModificator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is wraper between Channel and Stream I/O specialized to provide simple way for file data modification. Including keeping
 * separate position marker in file for read and write operations.
 *
 * @author casper
 */
public class FileStreamChannel {

    /**
     * File channel abstraction used by this stream.
     */
    protected final FileChannel channel;
    /**
     * Input stream channel representation.
     */
    protected InputStream in = null;
    /**
     * Output stream channel representation.
     */
    protected OutputStream out = null;

    /**
     * Construct wrapper from File and access rights information if it is possible otherwise throw exception.
     *
     * @param file   - must be non null file instance pointing to file.
     * @param access - list of requested access.It must contains at least READ or WRITE.
     *
     * @throws IOException - In case when channel initialization cause some I/O error or in case when access doesn't contains valid
     *                     combination of rights.
     */
    public FileStreamChannel(File file, Set<FileAccessModificator> access) throws IOException {
        if ((file == null) || (access == null)) {
            throw new NullPointerException("FileStreamChannel cannot be initialized by values");
        }

        if ((!access.contains(FileAccessModificator.CREATE)) && (!file.exists())) {
            throw new IOException("File not exist and access rights doesn't allow file creation");
        }
        if (file.isDirectory()) {
            throw new IOException("FileStreamChannel must be initialized by file");
        }
        if ((access.contains(FileAccessModificator.READ)) && (access.contains(FileAccessModificator.WRITE))) {
            channel = new RandomAccessFile(file, "rw").getChannel();
            in = new ChannelInputStream(0l);
            long position = 0;
            if (!access.contains(FileAccessModificator.CREATE)) {
                position = channel.size();
            }
            out = new ChannelOutputStream(position);
        } else if (access.contains(FileAccessModificator.READ)) {
            channel = new FileInputStream(file).getChannel();
            in = new ChannelInputStream(0l);
        } else if (access.contains(FileAccessModificator.WRITE)) {
            channel = new FileOutputStream(file).getChannel();
            long position = 0;
            if (!access.contains(FileAccessModificator.CREATE)) {
                position = channel.size();
            }
            out = new ChannelOutputStream(position);
        } else {
            throw new IOException("Access configuration doesn't contains valid combination of access rights:" + Arrays.deepToString(access.toArray()));
        }

    }

    /**
     * Return channel in representation of input stream.
     *
     * @return Input stream in case when channel is able read data, otherwise null.
     */
    public InputStream getInputStream() {
        return in;
    }

    /**
     * Return channel in representation of output stream.
     *
     * @return Output stream in case when channel is able write data, otherwise null.
     */
    public OutputStream getOutputStream() {
        return out;
    }

    /**
     * Internal class for wraping channel by InputStream.
     */
    protected class ChannelInputStream extends InputStream {

        /**
         * Read buffer.
         */
        protected ByteBuffer bb;

        /**
         * Position marker in file data after last read operation.
         */
        protected long position;

        /**
         * Flag for end of stream/file.
         */
        protected boolean eos;

        /**
         * Constructor for input stream channel wrapper. It is supporting system property: GOLEM.FILE_STREAM_CHANNEL.READ_BUFFER_SIZE for
         * defining size of buffer used for read.
         *
         * @param position - position in file where read will start
         */
        public ChannelInputStream(long position) {
            this.position = position;

            String property = System.getProperty("GOLEM.FILE_STREAM_CHANNEL.READ_BUFFER_SIZE");
            int buffSize = -1;
            if ((property != null) && (!property.isEmpty())) {
                try {
                    buffSize = Integer.parseInt(property);
                } catch (NumberFormatException ex) {
                    buffSize = 0;
                    Logger.getLogger(FileStreamChannel.ChannelInputStream.class.getName()).log(Level.WARNING, "Invalid property value:{0} cannot be converted to number", property);
                }
            }
            if (buffSize <= 0) {
                buffSize = 1024;
            }
            bb = ByteBuffer.allocateDirect(buffSize);
            bb.flip();
        }

        @Override
        public boolean markSupported() {
            return true;
        }

        @Override
        public synchronized void reset() throws IOException {
            bb.reset();
        }

        @Override
        public synchronized void mark(int readlimit) {
            if (readlimit > bb.capacity()) {
                ByteBuffer newBuff = ByteBuffer.allocateDirect(readlimit);
                while (bb.hasRemaining()) {
                    newBuff.put(bb.get());
                }
                bb = newBuff;
                bb.flip();
            } else if (readlimit > (bb.capacity() - bb.position())) {
                int dataIndex = 0;
                while (bb.hasRemaining()) {
                    bb.put(dataIndex++, bb.get());
                }
                bb.position(0);
                bb.limit(dataIndex);
            }
            bb.mark();
        }

        @Override
        public void close() throws IOException {
            channel.close();
        }

        @Override
        public int available() throws IOException {
            return bb.remaining();
        }

        @Override
        public long skip(long n) throws IOException {
            long retValue;
            if (n < bb.remaining()) {
                bb.position(bb.position() + (int) n);
                retValue = n;
            } else {
                long shift = n - bb.remaining();
                bb.position(bb.limit());
                long remaining = channel.size() - position;
                if (shift < remaining) {
                    position += shift;
                    retValue = n;
                } else {
                    position += remaining;
                    retValue = n - (shift - remaining);
                }
            }
            return retValue;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (!bb.hasRemaining()) {
                readIntoBuffer();
            }
            int retValue = -2;
            if (bb.hasRemaining()) {
                int pos = bb.position();
                bb.get(b, off, Math.min(len, bb.remaining()));
                retValue = bb.position() - pos;
            } else if ((eos) && (!bb.hasRemaining())) {
                retValue = -1;
            }
            return retValue;
        }

        @Override
        public int read() throws IOException {
            if (!bb.hasRemaining()) {
                readIntoBuffer();
            }
            int retValue = -2;
            if (bb.hasRemaining()) {
                retValue = bb.get();
            } else if ((eos) && (!bb.hasRemaining())) {
                retValue = -1;
            }
            return retValue;
        }

        /**
         * Method fill buffer by data from channel. Buffer is used by way which guarantees usage of full buffer capacity. This is needed by
         * mark and reset method to be working properly.
         *
         * @throws IOException
         */
        protected void readIntoBuffer() throws IOException {
            synchronized (channel) {
                int pos = bb.position();
                if (bb.limit() < bb.capacity()) {
                    bb.position(bb.limit());
                    bb.limit(bb.capacity());
                } else {
                    bb.clear();
                    pos=0;
                }
                if (channel.position() != position) {
                    channel.position(position);
                }
                eos = channel.read(bb) == -1;
                position = channel.position();
                bb.limit(bb.position());
                bb.position(pos);
            }
        }
    }

    /**
     * Internal class for wraping channel by output stream.
     */
    protected class ChannelOutputStream extends OutputStream {

        /**
         * Internal channel buffer.
         */
        protected ByteBuffer bb;
        /**
         * Valid position in channel after last write operation.
         */
        protected long position;
        /**
         * End of stream marker.
         */
        protected boolean eos;

        /**
         * Constructor for channel wrapper for output stream. It supports system property GOLEM.FILE_STREAM_CHANNEL.WRITE_BUFFER_SIZE for
         * defining size of buffer used for write operation.
         *
         * @param position - position in file where write will start.
         */
        public ChannelOutputStream(long position) {
            this.position = position;
            String property = System.getProperty("GOLEM.FILE_STREAM_CHANNEL.WRITE_BUFFER_SIZE");
            int buffSize = -1;
            if ((property != null) && (!property.isEmpty())) {
                try {
                    buffSize = Integer.parseInt(property);
                } catch (NumberFormatException ex) {
                    buffSize = 0;
                    Logger.getLogger(FileStreamChannel.ChannelInputStream.class.getName()).log(Level.WARNING, "Invalid property value:{0} cannot be converted to number", property);
                }
            }
            if (buffSize <= 0) {
                buffSize = 1024;
            }
            bb = ByteBuffer.allocateDirect(buffSize);
        }

        @Override
        public void close() throws IOException {
            flush();
            channel.close();
        }

        @Override
        public void flush() throws IOException {
            writeBuffer();
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            if (len > bb.remaining()) {
                writeBuffer();
            }
            bb.put(b, off, len);
        }

        @Override
        public void write(int b) throws IOException {
            if (!bb.hasRemaining()) {
                writeBuffer();
            }
            bb.put((byte) b);
        }

        /**
         * Method for save writing data from buffer to channel. Write all data from buffer to channel and clear buffer if data are
         * successfully written into channel.
         *
         * @throws IOException
         */
        protected void writeBuffer() throws IOException {
            bb.flip();
            if (channel.position() != position) {
                channel.position(position);
            }

            while (bb.hasRemaining()) {
                if (channel.write(bb) == -1) {
                    eos = true;
                    break;
                } else {
                    eos = false;
                }
            }
            this.position = channel.position();
            if (!bb.hasRemaining()) {
                bb.clear();
            }
        }
    }
}
