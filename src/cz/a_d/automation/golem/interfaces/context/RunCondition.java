/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.interfaces.context;

/**
 *
 * @author casper
 */
public interface RunCondition<T,C> extends CloneableIterator<T> {
    public boolean setupCond(T action, C expect, long counter);
    public long getActiveCounter();
    public void setActiveCounter(long activeCounter);
    public C getExpectedResult();
    public void setExpectedResult(C expectedResult);
    public void setCurrentResult(C currentResult);
    public void setActionTest(boolean actionTest);
}
