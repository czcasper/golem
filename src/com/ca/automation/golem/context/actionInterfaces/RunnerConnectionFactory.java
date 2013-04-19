/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context.actionInterfaces;

import java.io.IOException;
import java.net.URI;

/**
 *
 * @author basad01
 */
public interface RunnerConnectionFactory {
    
   
    /**
     *
     * @param url
     * @param userName
     * @param passwd
     * @return
     * @throws IOException
     */
    public RunnerConnection open(URI url,String userName,String passwd) throws IOException;
    /**
     *
     * @param url
     * @return
     * @throws IOException
     */
    public RunnerConnection open(URI url) throws IOException;
    
    
}
