/*
 */
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.ParameterKey;

/**
 * Implementation of key used by Parameter spool to index values.
 *
 * @author casper
 */
public class SimpleParameterKey extends AbstractSpoolKeyImpl<String> implements ParameterKey<String> {

    /**
     * Construct new instance of key from given string parameter value.
     *
     * @param key string representation of key value used to provide information for spool key.
     */
    public SimpleParameterKey(String key) {
        super(key);
    }

    @Override
    public boolean fromString(String key) {
        boolean retValue = false;
        if ((key != null) && (!key.isEmpty())) {
            this.keyValue = key;
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
