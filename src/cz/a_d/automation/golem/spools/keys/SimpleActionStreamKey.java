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
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.ActionStreamKey;

/**
 * Implementation of key used by Action stream spool to index values.
 *
 * @author casper
 */
public class SimpleActionStreamKey extends AbstractSpoolKeyImpl<String> implements ActionStreamKey<String> {

    /**
     * Construct new instance of key from given string parameter value.
     *
     * @param keyValue string representation of key value used to provide information for spool key.
     */
    public SimpleActionStreamKey(String keyValue) {
        super(keyValue);
    }

    @Override
    public String toString() {
        return (keyValue != null) ? keyValue : "";
    }

    @Override
    public boolean fromString(String key) {
        boolean retValue = false;
        if ((key != null) && (!key.isEmpty())) {
            keyValue = key;
            retValue = true;
        }
        return retValue;
    }
}
