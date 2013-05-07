/*
 */
package com.ca.automation.golem.spools.actions;

import com.ca.automation.golem.interfaces.ActionStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maslu02
 */
public class ActionStreamSpool {

    private static ActionStreamSpool defaultInstance = null;
    private static Map<String, ActionStream<Object, String, Object>> storageMap;

    public static ActionStreamSpool getDefaultInstance() {
        if (defaultInstance == null) {
            defaultInstance = new ActionStreamSpool();
            ActionStreamSpool.storageMap = new LinkedHashMap<String, ActionStream<Object, String, Object>>(16, 0.75f, true);
        }
        return defaultInstance;
    }

    public boolean put(String key, ActionStream<Object, String, Object> stream) {
        boolean retValue = false;
        if (stream != null) {
            List<Object> tmpList = stream.getActionList();
            if ((tmpList != null) && (!tmpList.isEmpty())) {
                storageMap.put(key, stream);
                retValue = true;
            }
        }
        return retValue;
    }

    public boolean contains(String key) {
        return storageMap.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public ActionStream<Object, String, Object> get(String key) {
        ActionStream<Object, String, Object> retValue = null;
        try {
            retValue = (ActionStream<Object, String, Object>) storageMap.get(key).clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(ActionStreamSpool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retValue;
    }
}
