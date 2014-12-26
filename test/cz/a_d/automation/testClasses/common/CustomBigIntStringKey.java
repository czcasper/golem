/*
 */
package cz.a_d.automation.testClasses.common;

import cz.a_d.automation.golem.spools.keys.AbstractSpoolKeyImpl;
import java.math.BigInteger;

/**
 *
 * @author casper
 */
public class CustomBigIntStringKey extends AbstractSpoolKeyImpl<BigInteger> {

    public CustomBigIntStringKey(BigInteger keyValue) {
        super(keyValue);
    }

    @Override
    public String toString() {
        String retValue ="";
        if(keyValue!=null){
            retValue = new String(keyValue.toByteArray());
        }
        return retValue;
    }

    @Override
    public boolean fromString(String key) {
        boolean retValue = false;
        if((key!=null)&&(!key.isEmpty())){
            keyValue = new BigInteger(key.getBytes());
            retValue=true;
        }
        return retValue;
    }

}
