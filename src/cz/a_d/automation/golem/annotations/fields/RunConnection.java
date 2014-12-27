/*
 */
package cz.a_d.automation.golem.annotations.fields;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation interface defined on scope of field and used by Golem to manage connections in connection spool requested by action.
 *
 * @author casper
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RunConnection {
    /**
     * Name used to identify connection in spool.
     *
     * @return return name of connection used for searching connection object.
     */
    String name() default "";

    /**
     * Pointer to value which identify value in spool. Value in parameter map must be type of string or object which implements
     *
     * @class ConnectionKey interface.
     *
     * @return empty string if not defined, otherwise string which represent key
     */
    String pointer() default "";
}
