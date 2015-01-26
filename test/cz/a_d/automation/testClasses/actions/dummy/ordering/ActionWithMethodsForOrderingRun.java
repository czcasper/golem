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
package cz.a_d.automation.testClasses.actions.dummy.ordering;

import cz.a_d.automation.golem.annotations.methods.Run;

/**
 *
 * @author casper
 */
// TODO Documentation: Create JavaDoc minimaly on class level.
public class ActionWithMethodsForOrderingRun {

    @Run
    public void method07() {
    }

    @Run(order = Integer.MIN_VALUE)
    public void method00() {
    }

    @Run(order = Integer.MAX_VALUE)
    public void method11() {
    }

    @Run(order = -1_000_000)
    public void method01() {
    }

    @Run(order = -1)
    public void method06() {
    }

    @Run(order = -100_000)
    public void method02() {
    }

    @Run(order = -10)
    public void method05() {
    }

    @Run(order = -10_000)
    public void method03() {
    }

    @Run(order = -1000)
    public void method04() {
    }

    @Run(order = 1)
    public void method08() {
    }

    @Run(order = 1_000)
    public void method10() {
    }

    @Run(order = 100)
    public void method09() {
    }
}
