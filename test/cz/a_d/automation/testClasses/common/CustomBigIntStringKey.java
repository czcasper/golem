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
package cz.a_d.automation.testClasses.common;

import cz.a_d.automation.golem.spools.keys.AbstractSpoolKeyImpl;
import java.math.BigInteger;

/**
 *
 * @author casper
 */
public class CustomBigIntStringKey extends AbstractSpoolKeyImpl<BigInteger> {

    public CustomBigIntStringKey(BigInteger keyValue) {
        super(keyValue);
    }

    @Override
    public String toString() {
        String retValue ="";
        if(keyValue!=null){
            retValue = new String(keyValue.toByteArray());
        }
        return retValue;
    }

    @Override
    public boolean fromString(String key) {
        boolean retValue = false;
        if((key!=null)&&(!key.isEmpty())){
            keyValue = new BigInteger(key.getBytes());
            retValue=true;
        }
        return retValue;
    }

}
