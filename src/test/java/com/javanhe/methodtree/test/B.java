package com.javanhe.methodtree.test;

/**
 * Sample class used for testing purposes.
 */
public class B {

    public void b1() {
    }

    // anyone calling this method will indirectly call method b1
    public void b2() {
        b1();
    }

    public void b3() {
    }
}
