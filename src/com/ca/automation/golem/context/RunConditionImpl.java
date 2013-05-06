/*
 */
package com.ca.automation.golem.context;

import com.ca.automation.golem.interfaces.RunCondition;

/**
 *
 * @author maslu02
 */
public class RunConditionImpl<T, C> implements RunCondition<T, C>, Cloneable {

    protected boolean actionTest = true;
    protected C currentResult;
    protected C expectedResult;
    protected long activeCounter;
    protected T action;

    @Override
    public boolean hasNext() {
        return (activeCounter != 0);
    }

    @Override
    public T next() {
        T retValue = null;
        if ((actionTest) && (activeCounter > 0)) {
            activeCounter--;
        }

        if ((activeCounter != 0) && (currentResult.equals(expectedResult))) {
            retValue = action;
        }
        return retValue;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean setupCond(T action, C expect, long counter) {
        boolean retValue = false;
        if ((action != null) && (counter != 0)) {
            this.action = action;
            expectedResult = expect;
            activeCounter = counter;
        }
        return retValue;
    }

    @Override
    public long getActiveCounter() {
        return activeCounter;
    }

    @Override
    public void setActiveCounter(long activeCounter) {
        this.activeCounter = activeCounter;
    }

    @Override
    public C getExpectedResult() {
        return expectedResult;
    }

    @Override
    public void setExpectedResult(C expectedResult) {
        this.expectedResult = expectedResult;
    }

    @Override
    public void setActionTest(boolean actionTest) {
        this.actionTest = actionTest;
    }

    @Override
    public void setCurrentResult(C currentResult) {
        this.currentResult = currentResult;
    }
}
