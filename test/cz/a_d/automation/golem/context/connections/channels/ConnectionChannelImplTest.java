/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.context.connections.channels;

import cz.a_d.automation.golem.context.connections.GolemConnectionFactory;
import cz.a_d.automation.golem.interfaces.connections.Connection;
import cz.a_d.automation.golem.interfaces.connections.channels.ConnectionChannel;
import cz.a_d.automation.golem.io.FileAccessModificator;
import cz.a_d.automation.golem.protocol.GolemURLStreamHandlerFactory;
import cz.a_d.automation.golem.protocol.file.FileHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author casper
 */
public class ConnectionChannelImplTest {

    protected static File testFile;
    protected List<List<Byte>> testData;
    protected Connection connection;

    public ConnectionChannelImplTest() throws IOException {
        testFile = File.createTempFile("connectionChannel", "test");
        Random r = new Random();
        int count = r.nextInt(150) + 150;
        testData = new ArrayList<>(count);

        FileOutputStream os = new FileOutputStream(testFile);
        for (int i = 0; i < count; i++) {
            byte[] tmp = new byte[r.nextInt(60) + 100];
            r.nextBytes(tmp);
            os.write(tmp);
            List<Byte> tmpList = new ArrayList<>(tmp.length);
            for (byte b : tmp) {
                tmpList.add(b);
            }
            testData.add(tmpList);
        }
        GolemURLStreamHandlerFactory.getDefaultInstance();
        connection = GolemConnectionFactory.getGlobalFactory().open(FileHelper.createFileURL(testFile));
    }

    @AfterClass
    public static void cleanup() {
        testFile.delete();
    }

    /**
     * Test of read method, of class ConnectionChannelImpl.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testRead() throws IOException {
        /**
         * Initialize and test protection agains null parameter value.
         */
        connection.open();
        try (ConnectionChannel instance = connection.getChannel()) {
            ByteBuffer dst = null;
            int result = instance.read(dst);
            assertTrue(result == 0);

            /**
             * Test reading data from readable channel.
             */
            Random r = new Random();
            dst = ByteBuffer.allocateDirect(r.nextInt(50) + 10);
            Iterator<List<Byte>> it = testData.iterator();
            Iterator<Byte> itemIt = null;
            while ((it.hasNext()) || (itemIt.hasNext())) {
                if ((itemIt == null) || (!itemIt.hasNext())) {
                    itemIt = it.next().iterator();
                }
                result = instance.read(dst);
                assertTrue(result > 0);
                dst.flip();
                while (dst.hasRemaining()) {
                    if (!itemIt.hasNext()) {
                        itemIt = it.next().iterator();
                    }
                    assertSame(itemIt.next(), dst.get());
                }
                dst.clear();
            }
            assertTrue(instance.isOpen());
            result = instance.read(dst);
            assertEquals(-1, result);
            assertTrue(instance.isOpen());
        }
    }

    /**
     * Test of isOpen method, of class ConnectionChannelImpl.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testIsOpen_Close() throws IOException {
        /**
         * Test expected state of connection which come from this test.
         */
        connection.open();
        ConnectionChannel instance = connection.getChannel();
        boolean result = instance.isOpen();
        assertTrue(result);

        /**
         * Test indication of closed connection.
         */
        instance.close();
        result = instance.isOpen();
        assertFalse(result);
        assertFalse(connection.isOpen());

        /**
         * Test reopened connection state.
         */
        connection.open();
        result = instance.isOpen();
        assertTrue(result);
    }

    /**
     * Test of write method, of class ConnectionChannelImpl.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testWrite() throws IOException {
        /**
         * Initialize and test protection agains null parameter value.
         */
        connection.open();
        try (ConnectionChannel instance = connection.getChannel()) {
            ByteBuffer src = null;
            int result = instance.write(src);
            assertEquals(0, result);

            /**
             * Test writing of data to into channel.
             */
            assertTrue(instance.isWriteable());

            Random r = new Random();
            int count = r.nextInt(150) + 150;

            for (int i = 0; i < count; i++) {
                byte[] tmp = new byte[r.nextInt(60) + 100];
                r.nextBytes(tmp);
                src = ByteBuffer.wrap(tmp);
                int write = instance.write(src);
                assertEquals(tmp.length, write);

                /**
                 * Storing writed data into file for keeping consistent test data for other tests.
                 */
                List<Byte> tmpList = new ArrayList<>(tmp.length);
                for (byte b : tmp) {
                    tmpList.add(b);
                }
                testData.add(tmpList);
            }
            assertTrue(instance.isOpen());
        }
    }

    /**
     * Test of isReadable method, of class ConnectionChannelImpl.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testIsReadable() throws IOException {
        connection.open();
        try (ConnectionChannel instance = connection.getChannel()) {
            boolean result = instance.isReadable();
            assertTrue(result);
        }
        connection.close();
        connection = GolemConnectionFactory.getGlobalFactory().open(FileHelper.createFileURL(testFile, Arrays.asList(new FileAccessModificator[]{FileAccessModificator.WRITE})));
        connection.open();
        try (ConnectionChannel instance = connection.getChannel()) {
            boolean result = instance.isReadable();
            assertFalse(result);
        }
        connection.close();
        connection = GolemConnectionFactory.getGlobalFactory().open(FileHelper.createFileURL(testFile));
    }

    /**
     * Test of isWriteable method, of class ConnectionChannelImpl.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testIsWriteable() throws IOException {
        connection.open();
        try (ConnectionChannel instance = connection.getChannel()) {
            boolean result = instance.isWriteable();
            assertTrue(result);
        }
        connection.close();
        connection = GolemConnectionFactory.getGlobalFactory().open(FileHelper.createFileURL(testFile, Arrays.asList(new FileAccessModificator[]{FileAccessModificator.READ})));
        connection.open();
        try (ConnectionChannel instance = connection.getChannel()) {
            boolean result = instance.isWriteable();
            assertFalse(result);
        }
        connection.close();
        connection = GolemConnectionFactory.getGlobalFactory().open(FileHelper.createFileURL(testFile));
    }

    /**
     * Test of refresh method, of class ConnectionChannelImpl.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testRefresh() throws IOException {
        connection.open();
        assertTrue(connection.isOpen());

        ConnectionChannel instance = connection.getChannel();
        instance.refresh(null);
        assertTrue(connection.isOpen());

        File tmpFile = File.createTempFile("connectionChannel", "test");
        URL tmpFileUrl = FileHelper.createFileURL(tmpFile);
        URLConnection tmpCon = tmpFileUrl.openConnection();
        tmpCon.connect();
        instance.refresh(tmpCon);
        assertTrue(instance.isOpen());

        /**
         * Test writing of data to into channel.
         */
        assertTrue(instance.isWriteable());

        Random r = new Random();
        int count = r.nextInt(150) + 150;
        List<List<Byte>> tmpTestData = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            byte[] tmp = new byte[r.nextInt(60) + 100];
            r.nextBytes(tmp);
            ByteBuffer src = ByteBuffer.wrap(tmp);
            int write = instance.write(src);
            assertEquals(tmp.length, write);

            /**
             * Storing writed data into file for following read test.
             */
            List<Byte> tmpList = new ArrayList<>(tmp.length);
            for (byte b : tmp) {
                tmpList.add(b);
            }
            tmpTestData.add(tmpList);
        }
        assertEquals(count, tmpTestData.size());
        assertTrue(instance.isOpen());

        /**
         * Test reading data from readable channel.
         */
        ByteBuffer dst = ByteBuffer.allocateDirect(r.nextInt(50) + 10);
        Iterator<List<Byte>> it = tmpTestData.iterator();
        Iterator<Byte> itemIt = null;
        while ((it.hasNext()) || (itemIt.hasNext())) {
            if ((itemIt == null) || (!itemIt.hasNext())) {
                itemIt = it.next().iterator();
            }
            int result = instance.read(dst);
            assertTrue(result > 0);
            dst.flip();
            while (dst.hasRemaining()) {
                if (!itemIt.hasNext()) {
                    itemIt = it.next().iterator();
                }
                assertSame(itemIt.next(), dst.get());
            }
            dst.clear();
        }
        assertTrue(instance.isOpen());
        int result = instance.read(dst);
        assertEquals(-1, result);
        assertTrue(instance.isOpen());

        tmpFile.delete();

        instance.refresh(connection.getCurrent());
    }
}
