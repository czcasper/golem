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
public class SimpleActionInfoKey implements ActionInfoKey<Class<?>>, Cloneable {

    protected Class<?> key;

    public SimpleActionInfoKey(String key) throws ClassNotFoundException {
        this.key = (Class<?>) Class.forName(key);
    }

    public SimpleActionInfoKey(Class<?> key) {
        this.key = key;
    }

    @Override
    public boolean set(Class<?> key) {
        boolean retValue = false;
        if (key != null) {
            this.key = key;
            retValue = true;
        }
        return retValue;
    }

    @Override
    public Class<?> get() {
        return key;
    }

    @Override
    public boolean fromString(String key) {
        if (key == null) {
            return false;
        }
        if ((this.key != null) && (this.key.getCanonicalName().equals(key))) {
            return true;
        }
        try {
            this.key = Class.forName(key);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SimpleActionInfoKey.class.getName()).log(Level.SEVERE, null, ex);
            return true;
        }
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.key != null ? this.key.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SimpleActionInfoKey other = (SimpleActionInfoKey) obj;
        if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return key.getCanonicalName();
    }
}
