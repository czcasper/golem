/*
 */
package cz.a_d.automation.testClasses.common;

import cz.a_d.automation.golem.spools.keys.AbstractSpoolKeyImpl;

/**
 *
 * @author casper
 */
// TODO Documentation: Create JavaDoc with references to test where this class is used.
public class CustomStringSpoolKey extends AbstractSpoolKeyImpl<String> {

    public CustomStringSpoolKey(String keyValue) {
        super(keyValue);
    }


    @Override
    public String toString() {
        return keyValue;
    }

    @Override
    public boolean fromString(String key) {
        boolean retValue = false;
        if((key!=null)&&(!key.isEmpty())){
            keyValue = key;
        }
        return retValue;
    }

}
