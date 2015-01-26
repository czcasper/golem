/* 
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 *  Copyright 2015 czcaspercz. All rights reserved.
 *  
 *  The contents of this file are subject to the terms of either the the Common Development and Distribution License 1.0 ("CDDL 1.0")
 *  You may not use this file except in compliance with the License. You can obtain a copy of the License at 
 *  
 *  http://opensource.org/licenses/CDDL-1.0
 *  
 *  See the License for the specific language governing permissions and limitations under the License.
 *  When distributing the software, include this License Header
 *  
 */
package cz.a_d.automation.golem.context;

import java.util.List;
import java.util.Map;

/**
 *
 * @author casper
 * @param <T>
 */
public class RunThreadControllerImpl<T,V> {

    protected T startAction;
    protected T endAction;
    
    protected int count;
    
    protected List<T> steps;
    
    protected List<Map<String,V>> parameters;
    
    
}
