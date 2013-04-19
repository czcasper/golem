/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ca.automation.golem.context.actionInterfaces;

import java.util.List;
import java.util.Map;

/**
 * Interface representing element (node) of a run-tree. Contains methods for the
 * tree traversal.
 *
 * @author pasol01
 */
public interface RunTreeElement {

    /**
     * Returns {@code true} if the node is leaf.
     *
     * @param leaf 
     */
    
    public void addLeaf(Object leaf);
    /**
     *
     * @param leaves
     */
    public void addLeaves(List<Object> leaves);
    
    /**
     *
     * @param child
     */
    public void addChild(RunTreeElement child);
    /**
     *
     * @param children
     */
    public void addChildren(List<RunTreeElement> children);
    
    /**
     *
     * @return
     */
    public boolean hasLeaves();

    /**
     *
     * @return
     */
    public boolean hasChildren();
    
    /**
     *
     * @param childs
     */
    public void setChildren(List<RunTreeElement> childs);

    /**
     * Gets children of the node.
     *
     * @return children of the node
     */
    public List<RunTreeElement> getChildren();

    /**
     * Gets parent of the node.
     *
     * @return parent of the node
     */
    public RunTreeElement getParent();

    /**
     *
     * @param parent
     */
    public void setParent(RunTreeElement parent);

    /**
     * Gets parameters associated with the node.
     *
     * @return parameters associated with the node
     */
    public Map<String, Object> getParameters();

    /**
     *
     * @param parameters
     */
    public void setParameters(Map<String, Object> parameters);

    /**
     *
     * @return
     */
    public List<Object> getLeaves();

    /**
     *
     * @param leaves
     */
    public void setLeaves(List<Object> leaves);
}
