/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.toRefactor;

import com.ca.automation.golem.context.actionInterfaces.RunConnection;
import com.ca.automation.golem.context.actionInterfaces.RunConnectionFactory;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author maslu02
 */

//TODO refator to use URI api for creting and registering connection, also add suport for customized handlers like jse2ftp
public class RunnerConnectionFactoryImpl implements RunConnectionFactory {

    private static Map<String, RunConnectionFactory> providerMap;

    static {
        providerMap = new HashMap<String, RunConnectionFactory>();
//        providerMap.put("jesftp", new Jes2FtpConnectionFactory());
    }
    
    /**
     *
     * @return
     */
    public static RunConnectionFactory createNewFactory(){
        return new RunnerConnectionFactoryImpl();
    }

    /**
     *
     * @param url
     * @param userName
     * @param passwd
     * @return
     * @throws IOException
     */
    @Override
    public RunConnection open(URI url, String userName, String passwd) throws IOException {
        RunConnection retValue = null;
        if (url != null) {
            String scheme = url.getScheme();
            if(scheme.compareToIgnoreCase("jesftp")==0) {
                retValue = providerMap.get(scheme).open(url, userName, passwd);
            }
        }
        return retValue;

    }

    /**
     *
     * @param url
     * @return
     */
    @Override
    public RunConnection open(URI url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
