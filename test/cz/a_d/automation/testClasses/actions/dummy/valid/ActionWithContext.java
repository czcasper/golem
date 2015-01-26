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
package cz.a_d.automation.testClasses.actions.dummy.valid;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.fields.RunContext;
import cz.a_d.automation.golem.annotations.methods.Run;

/**
 *
 * @author casper
 */
@RunAction
public class ActionWithContext extends ValidActionWithNoRun {
    
    @RunContext
    private Integer dummyContext;
    
    @Run(order= Integer.MAX_VALUE)
    protected boolean applyContext(){
        return true;
    }

}
