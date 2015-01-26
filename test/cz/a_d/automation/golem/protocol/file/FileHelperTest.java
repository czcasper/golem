/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
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
