/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
 */
package cz.a_d.automation.golem.interfaces.context.managers;

import cz.a_d.automation.golem.interfaces.context.RunActionStack;
import java.util.List;

/**
 * Interface describing method specific to manger of run action stack.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <V> the type of value used in parameter spool.
 */
public interface RunActionStackManager<T, V> extends ContextManager<T, RunActionStack<T>, V> {

    @Override
    public RunActionStack<T> getCurrent();

    /**
     * Getter for accessing list of currently active instances of stack.
     *
     * @return list of active stack currently processed by stack manager.
     */
    public List<RunActionStack<T>> getActive();
}
