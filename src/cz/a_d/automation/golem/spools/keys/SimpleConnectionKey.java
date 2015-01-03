/*
 */
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.ConnectionKey;

/**
 * Implementation of key used by Connection spool to index values.
 *
 * @author casper
 */
public class SimpleConnectionKey extends AbstractSpoolKeyImpl<String> implements ConnectionKey<String> {

    /**
     * Construct new instance of key from given string parameter value.
     *
     * @param keyValue string representation of key value used to provide information for spool key.
     */
    public SimpleConnectionKey(String keyValue) {
        super(keyValue);
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

    @Override
    public String toString() {
        String retValue = "";
        if (keyValue != null) {
            retValue = keyValue;
        }
        return retValue;
    }
}
