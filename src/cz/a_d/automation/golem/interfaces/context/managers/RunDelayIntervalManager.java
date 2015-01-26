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

import cz.a_d.automation.golem.interfaces.context.RunDelayInterval;
import java.util.List;

/**
 * Interface describing method specific to manger of delay.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <V> the type of value used in parameter spool.
 */
public interface RunDelayIntervalManager<T, V> extends ContextManager<T, RunDelayInterval<T>, V> {
    /**
     * Registering of new instance of managed feature. This method has specific parameters for result validation feature. It is providing
     * same functionality like general version.
     *
     * @param action      instance of action from action stream which will trigger loading registered result validator instance.
     * @param time        define timer live cycle configuration. Must be different from zero to setup timer correctly. For negative integer
     *                    be active in unlimited number of iteration. Positive integer make it active for number of time stored in value.
     * @param actionCount this parmeter is used for timer live cycle configuration. Must be different from zero to setup timer correctelly.
     *                    In case when is negative timer will live in unlimited number of iteration. In case when is positive timer will
     *                    live number of time stored in value.
     *
     * @return true in case when new instance of managed feature is configured properly, otherwise return false.
     */
    public boolean setup(T action, long actionCount, long time);

    @Override
    public RunDelayInterval<T> getCurrent();

    /**
     * Getter for accessing list of currently active instances of run delay.
     *
     * @return list of active delay currently processed by run delay manager.
     */
    public List<RunDelayInterval<T>> getActive();
}
