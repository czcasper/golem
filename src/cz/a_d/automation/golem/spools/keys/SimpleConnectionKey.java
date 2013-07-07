/*
 */
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.ConnectionKey;

/**
 *
 * @author maslu02
 */
public class SimpleConnectionKey extends AbstractSpoolKeyImpl<String> implements ConnectionKey<String> {

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
