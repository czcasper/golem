/*
 */
package cz.a_d.automation.golem.interfaces.connections.channels;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Interface describing Golems interactions with systems which are accepting commands. For easy manipulation is this interface extension of
 * Connection Channel. Commands are send with defined encoding and response is processed with same encoding like executed commands.
 *
 * @author casper
 */
public interface CommandChannel extends ConnectionChannel {

    /**
     * Writing command into channel.
     *
     * @param command character sequence of commands which will be executed. Cannot be null or empty.
     *
     * @return code returned by last command from sequence.
     *
     * @throws IOException if some other I/O error occurs.
     */
    public int write(CharSequence command) throws IOException;

    /**
     * Writing command into channel.
     *
     * @param command character buffer of commands which will be executed. Cannot be null or empty.
     *
     * @return code returned by last command from sequence.
     *
     * @throws IOException if some other I/O error occurs.
     */
    public int write(CharBuffer command) throws IOException;

    /**
     * Reading response from command into character sequence.
     *
     * @param respose character sequence which will be filled by response from command. Cannot be null.
     *
     * @return amount of character loaded into buffer.
     *
     * @throws IOException if some other I/O error occurs.
     */
    public int read(CharSequence respose) throws IOException;

    /**
     * Reading response from command into character sequence.
     *
     * @param respose character buffer which will be filled by response from command. Cannot be null.
     *
     * @return amount of character loaded into buffer.
     *
     * @throws IOException if some other I/O error occurs.
     */
    public int read(CharBuffer respose) throws IOException;

    /**
     * Changing character set which is used to encode and decode commands.
     *
     * @param channelCharset instance of Charset for encoding channel data. Must be different from null.
     */
    public void setCharSet(Charset channelCharset);

    /**
     * Getter for currently used character set, by this instance of command channel.
     *
     * @return instance of Charset for encoding channel data. Never return null.
     */
    public Charset getCharSet();
}
