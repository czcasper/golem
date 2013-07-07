/*
 */
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.ActionStreamKey;

/**
 *
 * @author maslu02
 */
public class SimpleActionStreamKey extends AbstractSpoolKeyImpl<String> implements ActionStreamKey<String>, Cloneable {

    public SimpleActionStreamKey(String keyValue) {
        super(keyValue);
    }

    @Override
    public String toString() {
        return (keyValue!=null)?keyValue:"";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public boolean fromString(String key) {
        boolean retValue = false;
        if((key!=null)&&(!key.isEmpty())){
            keyValue = key;
            retValue = true;
        }
        return retValue;
    }
}
