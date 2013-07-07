/*
 */
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.ActionInfoKey;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maslu02
 */
public class SimpleActionInfoKey extends AbstractSpoolKeyImpl<Class<?>> implements ActionInfoKey<Class<?>> {

    public SimpleActionInfoKey(String key) throws ClassNotFoundException {
        super((Class<?>) Class.forName(key));
    }

    public SimpleActionInfoKey(Class<?> key) {
        super(key);
    }

    @Override
    public boolean fromString(String key) {
        if (key == null) {
            return false;
        }
        if ((this.keyValue != null) && (this.keyValue.getCanonicalName().equals(key))) {
            return true;
        }
        try {
            this.keyValue = Class.forName(key);
        } catch (IllegalArgumentException | ClassNotFoundException ex) {
            Logger.getLogger(SimpleActionInfoKey.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String retValue = "";
        if (keyValue != null) {
            retValue = keyValue.getName();
        }
        return retValue;
    }
}
