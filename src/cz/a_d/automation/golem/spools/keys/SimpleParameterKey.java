/*
 */
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.ParameterKey;

/**
 *
 * @author casper
 */
public class SimpleParameterKey extends AbstractSpoolKeyImpl<String> implements ParameterKey<String> {

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
