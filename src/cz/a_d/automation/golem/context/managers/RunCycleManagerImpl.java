/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.a_d.automation.golem.context.managers;

import cz.a_d.automation.golem.common.FastStack;
import cz.a_d.automation.golem.common.iterators.ResetableIterator;
import cz.a_d.automation.golem.context.RunContextImpl;
import cz.a_d.automation.golem.context.RunCycleImpl;
import cz.a_d.automation.golem.interfaces.context.RunCycle;
import cz.a_d.automation.golem.interfaces.context.managers.RunCycleManager;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @param <T>
 * @author casper
 * @param <C>
 * @param <V>
 */
public class RunCycleManagerImpl<T, C, V> extends AbstractContextManager<T, C, RunCycle<T>, V> implements RunCycleManager<T, V> {

    protected FastStack<RunCycle<T>> curentStack;
    /**
     *
     */
    protected FastStack<Integer> arrayIndexStack;
    /**
     *
     */
    protected FastStack<List<RunCycle<T>>> currentCycleStack;
    /**
     *
     */
    protected boolean zeroLenFlag;

    /**
     *
     * @param context
     */
    public RunCycleManagerImpl(RunContextImpl<T, C, V> context) {
        super(context);
        curentStack = new FastStack<>();
        currentCycleStack = new FastStack<>();
        arrayIndexStack = new FastStack<>();
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
//        current.reset();
        zeroLenFlag = current.isZeroLength();
    }

    /**
     *
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void afterNextInList() {
        try {
            current = (RunCycle<T>) current.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(RunCycleManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!zeroLenFlag) {
            current.updateIt(current.getStartAction());
            if (current.isZeroLength()) {
                ResetableIterator tmpIt = context.resetableIterator();
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

        if (!currentCycleStack.isEmpty()) {
            currentList = currentCycleStack.pop();
            index = arrayIndexStack.pop();

//            if (index <= currentList.size()) {
            T tmp = current.getEndAction();
            current = curentStack.pop();
            current.endCycleHandler(tmp);
            retValue = true;
//            }
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
    @SuppressWarnings("unchecked")
    @Override
    protected void loadManger(T action) {
        if ((current == null) || (current.getStartAction() != action)) {

            if (current != null) {
                curentStack.push(current);
                currentCycleStack.push(currentList);
                arrayIndexStack.push(index);
            }
            index = 0;
            currentList = managed.get(action);
            try {
                current = (RunCycle<T>) currentList.get(index++);
                if (current != null) {
                    current = (RunCycle<T>) current.clone();
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(RunCycleManagerImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @param action
     * @param params
     * @return
     */
    @Override
    protected RunCycleImpl<T> setupManager(T action, Object... params) {
        RunCycleImpl<T> retValue = null;
        if ((context != null) && (context.getActionStream() != null) && (params.length == 2) && (params instanceof Number[])) {
            Number[] parm = (Number[]) params;
            RunCycleImpl<T> cycle = new RunCycleImpl<>(context.getActionStream().getActionList(), context.resetableIterator());
            if (cycle.setupCycle(action, parm[0].longValue(), parm[1].intValue())) {
                retValue = cycle;
            }
        }
        return retValue;
    }
}
