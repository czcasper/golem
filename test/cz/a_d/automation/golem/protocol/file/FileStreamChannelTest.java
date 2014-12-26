/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.protocol.file;

import cz.a_d.automation.golem.io.FileAccessModificator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Test which validate architectonic specification of FileStreamChannel class.
 * This class is wrapper for file channel used by FileConnection to provide R/W
 * access for files defined by URI protocol
 *
 * @author casper
 */
public class FileStreamChannelTest {

    protected File testFile = null;
    protected Set<FileAccessModificator> acessModi = new HashSet<>();

    /**
     * JUnit variable for testing exception generated by tests.
     */
    @Rule
    public ExpectedException testException = ExpectedException.none();

    @Before
    public void setUp() throws IOException {
        testFile = File.createTempFile("unitTest", "FileStreamChannel");
        acessModi.add(FileAccessModificator.READ);
        acessModi.add(FileAccessModificator.WRITE);
        acessModi.add(FileAccessModificator.CREATE);
    }

    @After
    public void tearDown() {
        testFile.delete();
        acessModi.clear();
    }

    /**
     * Test construction from null objects.
     *
     * @throws IOException
     */
    @Test
    public void testConstructor01() throws IOException {
        testException.expect(NullPointerException.class);
        testException.expectMessage("FileStreamChannel cannot be initialized by values");
        FileStreamChannel instance = new FileStreamChannel(null, null);
    }

    /**
     * Test construction with non null file and null access rights.
     *
     * @throws IOException
     */
    @Test
    public void testConstructor02() throws IOException {
        testException.expect(NullPointerException.class);
        testException.expectMessage("FileStreamChannel cannot be initialized by values");
        FileStreamChannel instance = new FileStreamChannel(testFile, null);
    }

    /**
     * Test construction with non file object type.
     *
     * @throws IOException
     */
    @Test
    public void testConstructor03() throws IOException {
        File folder = new File("test");
        Set<FileAccessModificator> modi = new HashSet<>();
        modi.add(FileAccessModificator.WRITE);
        testException.expect(IOException.class);
        testException.expectMessage("FileStreamChannel must be initialized by file");
        FileStreamChannel instance = new FileStreamChannel(folder, modi);
    }

    /**
     * Test construction with non existing file and access set without creation.
     *
     * @throws IOException
     */
    @Test
    public void testConstructor04() throws IOException {
        File nonExist = new File("test.txt").getAbsoluteFile();
        Set<FileAccessModificator> modi = new HashSet<>();
        testException.expect(IOException.class);
        testException.expectMessage("File not exist and access rights doesn't allow file creation");
        FileStreamChannel instance = new FileStreamChannel(nonExist, modi);
    }

    @Test
    public void testConstructor05() throws IOException {
        File nonExist = new File("test.txt").getAbsoluteFile();
        Set<FileAccessModificator> modi = new HashSet<>();
        modi.add(FileAccessModificator.CREATE);
        testException.expect(IOException.class);

        testException.expectMessage("Access configuration doesn't contains valid combination of access rights:" + Arrays.deepToString(modi.toArray()));
        FileStreamChannel instance = new FileStreamChannel(nonExist, modi);
    }

    /**
     * Test of getInputStream method, of class FileStreamChannel.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetInputStream() throws IOException {
        FileStreamChannel instance = new FileStreamChannel(testFile, acessModi);
        InputStream result = instance.getInputStream();
        assertNotNull(result);
        
        OutputStream outputStream = instance.getOutputStream();
        String testString = "Hello world test";
        byte[] bytes = testString.getBytes();
        outputStream.write(bytes);
        outputStream.flush();
        
        byte[] readBuffer= new byte[bytes.length];
        int read = result.read(readBuffer);
        assertEquals(bytes.length, read);
        assertArrayEquals(testString.getBytes(), readBuffer);
        
        acessModi.clear();
        acessModi.add(FileAccessModificator.READ);
        instance = new FileStreamChannel(testFile, acessModi);
        result = instance.getInputStream();
        assertNotNull(result);
        read = result.read(readBuffer);
        assertEquals(bytes.length, read);
        assertArrayEquals(testString.getBytes(), readBuffer);
        assertNull(instance.getOutputStream());
        
        acessModi.clear();
        acessModi.add(FileAccessModificator.WRITE);
        instance = new FileStreamChannel(testFile, acessModi);
        assertNull(instance.getInputStream());        
        assertNotNull(instance.getOutputStream());        
    }

    /**
     * Test of getOutputStream method, of class FileStreamChannel.
     * @throws java.io.IOException
     */
    @Test
    public void testGetOutputStream() throws IOException {
        FileStreamChannel instance = new FileStreamChannel(testFile, acessModi);
        OutputStream result = instance.getOutputStream();
        assertNotNull(result);
        
        String testString = "Hello world test";
        byte[] bytes = testString.getBytes();
        result.write(bytes);
        result.flush();
        assertEquals(testFile.length(), bytes.length);
    }

}
