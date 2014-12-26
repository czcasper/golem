/*
 */
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.ActionStreamKey;

/**
 *
 * @author casper
 */
public class SimpleActionStreamKey extends AbstractSpoolKeyImpl<String> implements ActionStreamKey<String> {

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
