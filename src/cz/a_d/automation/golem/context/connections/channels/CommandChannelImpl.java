/*
 */
package cz.a_d.automation.golem.context.connections.channels;

import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.connections.channels.CommandChannel;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 *
 * @author maslu02
 */
public class CommandChannelImpl extends ConnectionChannelImpl implements CommandChannel {

    protected Charset channelEncoding;

    public CommandChannelImpl(Connection parent, URLConnection connection) throws IOException {
        super(parent, connection);
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
}
