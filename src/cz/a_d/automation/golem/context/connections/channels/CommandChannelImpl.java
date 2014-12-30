/*
 */
package cz.a_d.automation.golem.context.connections.channels;

import cz.a_d.automation.golem.interfaces.connections.channels.CommandChannel;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Implementation of Interface describing Golems interactions with systems which are accepting commands. Commands are send with defined
 * encoding and response is processed with same encoding like executed commands. Source and destination of command is initiated from URL
 * connections.
 *
 * @author casper
 */
public class CommandChannelImpl extends ConnectionChannelImpl implements CommandChannel {

    /**
     * Instance of character set currently used for encoding of channel data.
     */
    protected Charset channelEncoding;

    /**
     * Create new instance of connection implementation from URLconnection. In case when URLconnection define encoding than is used,
     * otherwise default system encoding is used.
     *
     * @param connection connection which will be wrapped by this command channel and used to communicate with system resource. Cannot be
     *                   null.
     * @throws IOException if some other I/O error occurs.
     */
    public CommandChannelImpl(URLConnection connection) throws IOException {
        super(connection);
        String strCharset = connection.getContentEncoding();
        if ((strCharset != null) && (!strCharset.isEmpty())) {
            channelEncoding = Charset.forName(strCharset);
        } else {
            channelEncoding = Charset.defaultCharset();
        }
    }

    @Override
    public int write(CharSequence command) throws IOException {
        return write(CharBuffer.wrap(command));
    }

    @Override
    public int write(CharBuffer command) throws IOException {
        int retValue = 0;
        if ((command != null) && (command.remaining() > 0)) {
            ByteBuffer encode = channelEncoding.encode(command);
            while (encode.hasRemaining()) {
                int tmp = write(encode);
                if (tmp >= 0) {
                    retValue += tmp;
                } else {
                    break;
                }

            }
        }
        return retValue;
    }

    @Override
    public int read(CharSequence respose) throws IOException {
        return read(CharBuffer.wrap(respose));
    }

    @Override
    public int read(CharBuffer respose) throws IOException {
        int retValue = 0;
        if ((respose != null) && (respose.hasRemaining())) {
            ByteBuffer encode = channelEncoding.encode(respose.subSequence(respose.position(), respose.capacity()));
            retValue = read(encode);
            if (retValue > 0) {
                encode.flip();
                CharBuffer decode = channelEncoding.decode(encode);
                while (decode.hasRemaining()) {
                    respose.put(decode.get());
                }
            }

        }
        return retValue;
    }

    @Override
    public void setCharSet(Charset channelCharset) {
        this.channelEncoding = channelCharset;
    }

    @Override
    public Charset getCharSet() {
        return channelEncoding;
    }

    @Override
    public void refresh(URLConnection connection) throws IOException {
        String strCharset = connection.getContentEncoding();
        if ((strCharset != null) && (!strCharset.isEmpty())) {
            channelEncoding = Charset.forName(strCharset);
        } else {
            channelEncoding = Charset.defaultCharset();
        }
        super.refresh(connection);
    }

}
