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
package cz.a_d.automation.golem.interfaces.spools;

import cz.a_d.automation.golem.interfaces.ActionStream;
import cz.a_d.automation.golem.interfaces.spools.keys.ActionStreamKey;

/**
 * Interface describe Action stream spool. Spool used to store and reuse action stream definitions.
 *
 * @author casper
 * @param <A> the type of action managed by spool.
 * @param <V> the type of value used in ParameterSpool.
 */
public interface ActionStreamSpool<A, V> extends AbstractSpool<A, ActionStreamKey<?>, ActionStream<A, V>> {

    @Override
    public ActionStreamSpool<A, V> newInstance();
}
