/*
 */
package cz.a_d.automation.testClasses.actions.dummy.valid;

import cz.a_d.automation.golem.annotations.RunAction;
import cz.a_d.automation.golem.annotations.methods.Run;
import java.util.Objects;

/**
 *
 * @author maslu02
 */
// TOD Documentation: Create JavaDoc on class and method level.
@RunAction
public class ActionForTestingContext<T> {

    protected T value;

    public ActionForTestingContext() {
    }

    public ActionForTestingContext(T value) {
        this.value = value;
    }

    @Run
    public void dummyRun(){
        
    }
    
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ActionForTestingContext<T> other = (ActionForTestingContext<T>) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
    
}
