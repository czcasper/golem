/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.protocol.file;

import cz.a_d.automation.golem.io.FileAccessModificator;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for validation architectonic specification of functionality file Helper
 * class for creating URL with access rights from file.
 *
 * @author casper
 */
public class FileHelperTest {

    /**
     * Validate if method is working as is expected by architectonic
     * definitions. Adding rights to create and write for non existing files,
     * protection agains not valid input parameters and automatic rights
     * detection from existing files.
     *
     * @throws java.lang.Exception - throwed by method in case when something is
     * going wrong with URL initialization
     */
    @Test
    public void testCreateFileURL_File() throws Exception {
        File f = null;
        URL result = FileHelper.createFileURL(f);
        assertNull(result);

        f = new File("tmp.tst");
        result = FileHelper.createFileURL(f);
        assertNotNull(result);
        assertEquals("access={create,write}", result.getQuery());

        f = File.createTempFile("unitTest", "FileHelper");
        result = FileHelper.createFileURL(f);
        assertNotNull(result);
        assertEquals("access={read,write}", result.getQuery());
        f.delete();
    }

    /**
     * Validate method protections against null input parameter and option to
     * generate file URL without query part by using null or empty collection of
     * access rights.
     *
     * @throws java.lang.Exception - throwed by method in case when something is
     * going wrong with URL initialization
     */
    @Test
    public void testCreateFileURL_File_Collection() throws Exception {
        File f = null;
        Collection<FileAccessModificator> per = null;
        URL result = FileHelper.createFileURL(f, per);
        assertNull(result);

        f = File.createTempFile("unitTest", "FileHelper");
        result = FileHelper.createFileURL(f, per);
        assertNotNull(result);
        assertNull(result.getQuery());

        per = new ArrayList<>();
        result = FileHelper.createFileURL(f, per);
        assertNotNull(result);
        assertNull(result.getQuery());

        per.add(FileAccessModificator.READ);
        result = FileHelper.createFileURL(f, per);
        assertNotNull(result);
        assertEquals("access={read}", result.getQuery());
        f.delete();
    }
}
