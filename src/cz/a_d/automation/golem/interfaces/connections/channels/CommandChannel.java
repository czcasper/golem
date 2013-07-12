/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.connections.channels;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 *
 * @author maslu02
 */
public interface CommandChannel extends ConnectionChannel {

    public int write(CharSequence command) throws IOException;

    public int write(CharBuffer command) throws IOException;

    public int read(CharSequence respose) throws IOException;

    public int read(CharBuffer respose) throws IOException;

    public void setCharSet(Charset channelCharset);

    public Charset getCharSet();
}
