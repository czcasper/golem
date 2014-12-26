/*
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
