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

import cz.a_d.automation.golem.annotations.methods.Init;

/**
 *
 * @author casper
 */
public class ActionWithMethodsForOrderingInit {
        @Init(order = 5)
        public void method00() {
        }

        @Init(order = 4)
        public void method01() {
        }

        @Init(order = 3)
        public void method02() {
        }

        @Init(order = 2)
        public void method03() {
        }

        @Init(order = 1)
        public void method04() {
        }

        @Init
        public void method05() {
        }

        @Init(order = -1)
        public void method06() {
        }

        @Init(order = -2)
        public void method07() {
        }

        @Init(order = -3)
        public void method08() {
        }

        @Init(order = -4)
        public void method09() {
        }

        @Init(order = -5)
        public void method10() {
        }

        @Init(order= -6)
        public void method11() {
        }

}
