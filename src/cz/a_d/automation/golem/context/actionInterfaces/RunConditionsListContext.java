/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.context.actionInterfaces;

import cz.a_d.automation.golem.interfaces.context.RunCondition;
import java.util.List;

/**
 *
 * @author maslu02
 */
public interface RunConditionsListContext extends List<RunCondition<Object, Boolean>> {
    
}
