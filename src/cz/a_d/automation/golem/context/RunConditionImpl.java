/*
 */
package cz.a_d.automation.golem.context;

import cz.a_d.automation.golem.interfaces.context.RunCondition;
import java.util.NoSuchElementException;

/**
 * Condition for validation result of action methods. Allow mapping of condition validations for triggered by specific action and limited by
 * count of methods which will be validated for result collected from return value of tested method.
 *
 * @author casper
 * @param <T> the type of action used for triggering result validation.
 * @param <C> the type of method return value used for result validation.
 */
public class RunConditionImpl<T, C> implements RunCondition<T, C>, Cloneable {

    /**
     * Flag which allows to bypass decreasing of counter which tells how long will be result validation active.
     */
    protected boolean actionTest = true;
    /**
     * Storage for currently validated value returned by tested method. Needs to be defined before calling next method.
     */
    protected C currentResult;
    /**
     * Storage for expected value returned by tested method. Needs to be defined before calling next method.
     */
    protected C expectedResult;
    /**
     * Counter used to determine if validation is active. There are three different modes of counter. Zero is not active validation,
     * positive integer is used and decremented by every validation until is zero. Any negative integer means validation is active until end
     * of action stream.
     */
    protected long activeCounter;
    /**
     * Stored pointer to action from action stream which is trigger for starting result validation.
     */
    protected T action;

    @Override
    public boolean hasNext() {
        return (activeCounter != 0);
    }

    @Override
    public T next() {
        if (activeCounter == 0) {
            throw new NoSuchElementException("Condition doesnt have next element");
        }
        T retValue = null;
        if ((actionTest) && (activeCounter > 0)) {
            activeCounter--;
        }

        if ((activeCounter != 0) && (currentResult != null) && (currentResult.equals(expectedResult))) {
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
