package com.javanhe.methodtree.test.component;

/**
 * A sample abstract class used for testing purposes.
 */
public abstract class BaseComponent {

    public abstract String getMessage();

    public void saySomething() {
        System.out.println(getMessage());
    }
}
