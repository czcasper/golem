/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context.managers;

import com.ca.automation.golem.common.iterators.ResetableIterator;
import com.ca.automation.golem.context.RunContextImpl;
import com.ca.automation.golem.context.RunCycle;
import com.ca.automation.golem.context.actionInterfaces.managers.RunCycleManagerContext;
import java.util.List;
import java.util.Stack;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public class RunCycleManager<T> extends AbstractContextManager<T, RunCycle<T>> implements RunCycleManagerContext<T> {

    /**
     *
     */
    protected Stack<Integer> arrayIndexStack;
    /**
     *
     */
    protected Stack<List<RunCycle<T>>> currentCycleStack;
    /**
     *
     */
    protected boolean zeroLenFlag;

    /**
     *
     * @param context
     */
    public RunCycleManager(RunContextImpl<T> context) {
        super(context);
        currentCycleStack = new Stack<List<RunCycle<T>>>();
        arrayIndexStack = new Stack<Integer>();
    }

    /**
     *
     * @param action
     * @param repeatCount
     * @param actionCount
     * @return
     */
    @Override
    public boolean setup(T action, long repeatCount, int actionCount) {
        Object[] tmp = new Long[]{repeatCount, (long) actionCount};
        return setup(action, tmp);
    }

    /**
     *
     */
    @Override
    protected void beforeNextInList() {
        current.reset();
        zeroLenFlag = current.isZeroLength();
    }

    /**
     *
     */
    @Override
    protected void afterNextInList() {
        if (!zeroLenFlag) {
            current.updateIt(current.getStartAction());
            if (current.isZeroLength()) {
                ResetableIterator tmpIt = context.getIt();
                if (tmpIt.hasNext()) {
                    tmpIt.next();
                }
            }
        }
    }

    /**
     *
     * @return
     */
    @Override
    protected boolean currentFinilizer() {
        boolean retValue = false;
        current.reset();

        if (!currentCycleStack.isEmpty()) {
            currentList = currentCycleStack.pop();
            index = arrayIndexStack.pop();

            if (index <= currentList.size()) {
                T tmp = current.getEndAction();
                current = currentList.get(index - 1);
                current.endCycleHandler(tmp);
                retValue = true;
            }
        }

        return retValue;
    }

    /**
     *
     * @return
     */
    @Override
    public RunCycle<T> getCurrent() {
        return super.getCurrent();
    }

    /**
     * Method for finding cycles in processed actions.
     *
     * @param action currently proccesed action instance.
     */
    @Override
    protected void loadManger(T action) {
        if ((current == null) || (current.getStartAction() != action)) {
            if (current != null) {
                currentCycleStack.push(currentList);
                arrayIndexStack.push(index);
            }
            index = 0;
            currentList = managed.get(action);
            current = currentList.get(index++);
        }
    }

    /**
     *
     * @param action
     * @param params
     * @return
     */
    @Override
    protected RunCycle<T> setupManager(T action, Object ... params) {
        RunCycle<T> retValue = null;
        if ((context != null) && (params.length == 2) && (params instanceof Number[])) {
            Number[] parm = (Number[]) params;
            RunCycle<T> cycle = new RunCycle<T>(context.getSteps(), context.getIt());
            if (cycle.setupCycle(action, parm[0].longValue(), parm[1].intValue())) {
                retValue = cycle;
            }
        }
        return retValue;
    }
}
