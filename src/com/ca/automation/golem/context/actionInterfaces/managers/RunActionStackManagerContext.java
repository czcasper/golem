/*
 */
package com.ca.automation.golem.context.actionInterfaces.managers;

import java.util.Collection;

/**
 *
 * @param <T> 
 * @author maslu02
 */
public interface RunActionStackManagerContext<T> {

    /**
     * Method for accesing array of active stack directly
     *
     * @return list of active ActionsStacks
     */
//    RunnerActiveStackList getActiveStacks();

    /*
     * This method safely initialize stack in runner manager under the action
     * object by name idendificator and collection of actions.
     *
     * @param action key action used for starting poping actions from stack
     * @param name property used for latest stack idendification. Is optional
     *          can be null
     * @param stackActions Non empty collection with actions
     *
     * @return true if stack is correctly initialized, otherwise false.
     */
//    boolean initStack(T action, String name, Collection<T> stackActions);

    /**
     * This method safely initialize stack in runner manager under the action
     * object by name idendificator and array of actions.
     *
     * @param action key action used for starting poping actions from stack
     * @param name property used for latest stack idendification. Is optional
     *          can be null
     * @param stackActions Non empty array with actions
     *
     * @return true if stack is correctly initialized, otherwise false.
     */
//    boolean initStack(T action, String name, T... stackActions);

    /**
     * Method for validating status of StackManager.
     *
     * @return true in case when there is some active Stack with actions, otherwise false
     */
//    boolean isStackActive();

    /**
     * Method for update curent step in Stack, if action is in active stacks then
     * is returned.
     *
     * @return action object or null if there is no actions in active stacks
     */
//    Object updateStackStep();
    
}
