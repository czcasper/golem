/*
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
 * Implementation of interface specific to manager of run cycles.
 *
 * @author casper
 * @param <T> the type of action managed by content manager.
 * @param <V> the type of value used in parameter spool.
 * @param <C> the type of object used for validation of run results
 */
public class RunCycleManagerImpl<T, C, V> extends AbstractContextManager<T, C, RunCycle<T>, V> implements RunCycleManager<T, V> {

    /**
     * Stack of currently active instance of cycles. Stack is used to implement inner cycles.
     */
    protected FastStack<RunCycle<T>> curentStack;

    /**
     * Stack with index of active cycle in list of instance of cycles stored in stack. Stack is used to implement inner cycles registered on
     * same action.
     */
    protected FastStack<Integer> arrayIndexStack;

    /**
     * Stack of list of instance of cycles which are processed. Stack is used to implement inner cycles registered on same action.
     */
    protected FastStack<List<RunCycle<T>>> currentCycleStack;

    /**
     * Flag used to deal with cycles on top of one action. In this case there needs to be special logic in manager to work properly.
     */
    protected boolean zeroLenFlag;

    /**
     * Creating instance of manager of run cycle.
     *
     * @param context run context for which is this manager implementation registered. Must be different from null.
     */
    public RunCycleManagerImpl(RunContextImpl<T, C, V> context) {
        super(context);
        curentStack = new FastStack<>();
        currentCycleStack = new FastStack<>();
        arrayIndexStack = new FastStack<>();
    }

    @Override
    public boolean setup(T action, long repeatCount, int actionCount) {
        Object[] tmp = new Long[]{repeatCount, (long) actionCount};
        return setup(action, tmp);
    }

    // TODO find if comments shloud be removed or uncomented.
    @Override
    protected void beforeNextInList() {
//        current.reset();
        zeroLenFlag = current.isZeroLength();
    }

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

    @Override
    public RunCycle<T> getCurrent() {
        return super.getCurrent();
    }

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

    @Override
    protected RunCycleImpl<T,V> setupManager(T action, Object... params) {
        RunCycleImpl<T,V> retValue = null;
        if ((context != null) && (context.getActionStream() != null) && (params.length == 2) && (params instanceof Number[])) {
            Number[] parm = (Number[]) params;
            RunCycleImpl<T,V> cycle = new RunCycleImpl<>(context.getActionStream());
            if (cycle.setupCycle(action, parm[0].longValue(), parm[1].intValue())) {
                retValue = cycle;
            }
        }
        return retValue;
    }
}
