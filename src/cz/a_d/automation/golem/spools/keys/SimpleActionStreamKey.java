/*
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
