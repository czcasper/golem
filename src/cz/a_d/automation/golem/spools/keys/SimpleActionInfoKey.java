/*
 */
package cz.a_d.automation.golem.spools.keys;

import cz.a_d.automation.golem.interfaces.spools.keys.ActionInfoKey;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of key used by Action information spool to index values.
 *
 * @author casper
 */
public class SimpleActionInfoKey extends AbstractSpoolKeyImpl<Class<?>> implements ActionInfoKey<Class<?>> {

    /**
     * Construct new instance of key from class name provided in form of string parameter.
     *
     * @param key name of class which will represent key value for spool. Must be valid class name and class must be loaded in class path.
     * @throws ClassNotFoundException in case when class name is not valid or class is not accessible for class loader.
     */
    public SimpleActionInfoKey(String key) throws ClassNotFoundException {
        super((Class<?>) Class.forName(key));
    }

    /**
     * Construct new instance of key from given class object.
     *
     * @param key instance of class which will be used to provide information for key.
     */
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
