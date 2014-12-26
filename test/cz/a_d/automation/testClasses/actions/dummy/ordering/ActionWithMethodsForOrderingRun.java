/*
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
